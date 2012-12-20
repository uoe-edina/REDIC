package uk.ac.ed.redic

import groovy.xml.StreamingMarkupBuilder
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import org.purl.sword.client.ClientOptions
import org.purl.sword.client.PostMessage
import org.purl.sword.client.SWORDClient;

import java.security.MessageDigest;
import org.apache.commons.logging.LogFactory;
import org.purl.sword.client.*
import org.purl.sword.base.DepositResponse;
import org.purl.sword.base.SWORDEntry;
import org.purl.sword.base.ServiceDocument;

/**
 * Controller for preparing a SWORD ingest package from the objects in the ingester database
 * and performing a SWORD ingest of the item into dspace
 */

class XMLController {

	static defaultAction = "serviceDocument"

	// Inject grailsApplication to get access to the config   
	def grailsApplication

	private static final log = LogFactory.getLog(this)
	private static final String SWORD_USER_KEY = "sword.user"
	private static final String SWORD_PASS_KEY = "sword.user"
	private static final String SWORD_SD_URL_KEY = "sword.sd.url"

	private SWORDClient client;

	def swordV1Service;

	public XMLController(){
		client = new Client();
	}

	/**
	 * Controller entry point - will return the service document in XML format or HTTP error if a problem occurs
	 * @return service document in XML format or HTTP 500 with error message
	 */
	def serviceDocument() {
		ServiceDocument sd = swordV1Service.getSD(client);

		if (!sd){
			response.status = 500
			render """ERROR Service Document was null
		   """
		} else {
			String serviceDocXml = sd.toString()

			// render some text with encoding and content type
			render(text: serviceDocXml, contentType: "text/xml", encoding: "UTF-8")
		}
	}

  /*
   * Standalone xml writer to write an xml strcuture into a file
   */
  def writeFile(fileName, xml) {
        def xmlFile = new File(fileName)
        def writer = xmlFile.newWriter()
     
        def builder = new StreamingMarkupBuilder()
        def Writable writable = builder.bind xml
        writable.writeTo(writer)
    }


	def syncXML(File fileUploaded, String metadataType) {

		String tmpDir = grailsApplication.config.upload.dir;
		File tmpFile;
		def records, $name, $descr;
		String fileName = tmpDir + "tmpFile.xml";

		// Mapping of EPSRC and Bath data in the xml file to base schema
		switch (metadataType) {
			case 'EPSRC data':
				records = new groovy.util.XmlSlurper().parse(fileUploaded);
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				def xml = {
					equipment {
						name1 (records.'technique'.text())
						orgUnitID(records.'organisation'.text())
						faculty (records.'department'.text())
						taxonomy (records.'researchArea'.text())
						description (records.'equipmentDetails'.text())
						equipmentWebPage (records.'researchGroupWebPage'.text())
						created (df.format(new Date()))
						contact1Name (records.'fullName'.text())
						contact1Phone (records.'phoneNumber'.text())
						contact1Email (records.'email'.text())
					}
				}				
				writeFile(fileName, xml);
				break;

			case 'Bath data':
				records = new groovy.util.XmlSlurper().parse(fileUploaded);
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				def xml = {
					equipment {
						orgUnitID(records.'equipmentItem'.'institution'.text())
						uniqueID(records.'equipmentItem'.'uid'.text())
						name1(records.'equipmentItem'.'equipmentTitle'.text())
						description(records.'equipmentItem'.'equipmentDescription'.text())						
						created(df.format(new Date()))
						faculty(records.'equipmentItem'.'owner'.text())
						taxonomy (records.'equipmentItem'.'recordType'.text())
						contact1Name (records.'equipmentItem'.'responsiblePerson'.text())
						contact1Phone(records.'equipmentItem'.'phone'.text())
						contact1URL (records.'equipmentItem'.'website'.text())
						contact1Email (records.'equipmentItem'.'email'.text())
						
					}
				}
				
				writeFile(fileName, xml);
				break;
		}

		if(metadataType.equals('Cerif data')){
			//File uploadedFileObj = File.createTempFile("cerif_", ".xml", new File(tmpDir));
			//fileUploaded.transferTo(uploadedFileObj);
			tmpFile = File.createTempFile("cerif_", ".zip", new File(tmpDir));
			// Extract title/description from cerif xml
			records = new groovy.util.XmlSlurper().parse(fileUploaded);
			$name = records.'cfEquip'.'cfName'.text();
			$descr = records.'cfEquip'.'cfDescr'.text();
		}
		else if (metadataType.equals('Southampton data')){
			//File uploadedFileObj = File.createTempFile("redic_", ".xml", new File(tmpDir));
			//fileUploaded.transferTo(uploadedFileObj);
			tmpFile = File.createTempFile("redic_", ".zip", new File(tmpDir));
			records = new groovy.util.XmlSlurper().parse(fileUploaded);
			$name = records.'name1'.text();
			$descr = records.'description'.text();
		}
		else {
			tmpFile = File.createTempFile("redic_", ".zip", new File(tmpDir));
			records = new groovy.util.XmlSlurper().parse(fileName);
			$name = records.'name1'.text();
			$descr = records.'description'.text();
		}


		// Create a Zip file for the package
		ZipOutputStream zo = new ZipOutputStream(new FileOutputStream(tmpFile))

		// Add ZIP entry for the mets.xml manifest
		String metsXML;
		if(metadataType.equals("Cerif data")) {
			metsXML = g.render(template:"mets_cerif" , contentType:"text/xml", model:[name1:$name, description:$descr]);
		}
		else {
			metsXML = g.render(template:"mets_redic" , contentType:"text/xml", model:[name1:$name, description:$descr]);
		}
		zo.putNextEntry(new ZipEntry("mets.xml"));
		zo.write(metsXML.getBytes(), 0, metsXML.length());
		// Complete the entry
		zo.closeEntry();
		// Add ZIP entry for the redic XML file
		FileInputStream fis; 
		// NOTE: do not change the name of the metadata file - this is referenced in the mets template
		if(metadataType.equals("Cerif data")){
			fis = new FileInputStream(fileUploaded);
			zo.putNextEntry(new ZipEntry("cfEquip.xml"));
		}
		else if (metadataType.equals("Southampton data")) {
			fis = new FileInputStream(fileUploaded);
			zo.putNextEntry(new ZipEntry("redic_metadata.xml"));
		}
		else {
			fis = new FileInputStream(fileName);
			zo.putNextEntry(new ZipEntry("redic_metadata.xml"));
		}
		// Transfer bytes from the file to the ZIP file
		int len;
		int totalBytes = 0;
		// Create a buffer for reading the files
		byte[] buf = new byte[1024];
		while ((len = fis.read(buf)) > 0)
		{
			totalBytes += len;
			zo.write(buf, 0, len);
		}
		// Complete the entry
		fis.close();
		//zo.write(redicXML.getBytes(), 0, redicXML.length());
		zo.closeEntry();

		// Tidy up - close the Zip stream
		zo.close()

		String url = grailsApplication.config.sword.col;
		PostMessage message = new PostMessage();
		message.setFilepath(tmpFile.getAbsolutePath());
		print message.getFilepath();
		message.setDestination(url);
		message.setFiletype("application/zip");
		message.setUseMD5(false);
		message.setVerbose(true);
		message.setNoOp(false);
		message.setUserAgent(ClientConstants.SERVICE_NAME);
		message.setFormatNamespace(grailsApplication.config.sword.packaging)

		try{
			ClientOptions options = swordV1Service.getCred()
			swordV1Service.initialiseServer(client, url, options.getUsername(), options.getPassword());
			log.debug("The message is" + message);
			DepositResponse response = client.postFile(message);
			log.debug("The status is: " + client.getStatus());

			// Clean up the file system
			tmpFile.delete();

			String handle = ""
			if (response != null){
				// iterate over the data and output it
				SWORDEntry entry = response.getEntry();

				handle = entry.getId()
				log.debug("Item handle: " + handle);
				//  item.dspaceHandle = handle
				//  item.save()

				log.debug(response.toString())
			} else {
				throw new Exception("Response from post was null")
			}

			flash.message = "Item stored in DSpace with handle: ${handle}"
			redirect(controller: "equipmentManager", action: "index");



		} catch (Exception e){
			log.error("Caught exception: ${e.getMessage()}")
			throw e;
		}

	}

}
