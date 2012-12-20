package uk.ac.ed.redic

import java.security.MessageDigest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.logging.LogFactory;

import org.purl.sword.client.*
import org.purl.sword.base.DepositResponse;
import org.purl.sword.base.SWORDEntry;
import org.purl.sword.base.ServiceDocument;


/** 
 * Controller for preparing a SWORD ingest package from the objects in the ingester database
 * and performing a SWORD ingest of the item into dspace
 */
class SwordController {

	static defaultAction = "serviceDocument";
   // Inject grailsApplication to get access to the config
   def grailsApplication
   
   private static final log = LogFactory.getLog(this)
   
   public Client client;
   
   def swordV1Service;
   
   public SwordController (){
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
   
 
  def syncItem() {
	 // print "syncITEM!!!";
	  //swordService = new SwordService();
	  def itemId = params.item
	  EquipmentItem item = EquipmentItem.get(itemId)

	  String tmpDir = grailsApplication.config.upload.dir
	  File tmpFile = File.createTempFile("redic_", ".zip", new File(tmpDir));
	  			 
	  // Create a Zip file for the package 
	  ZipOutputStream zo = new ZipOutputStream(new FileOutputStream(tmpFile))
	  
	  // Add ZIP entry for the mets.xml manifest
	  String metsXML = g.render(template:"mets" , contentType:"text/xml", bean:item)
	  zo.putNextEntry(new ZipEntry("mets.xml"));
	  zo.write(metsXML.getBytes(), 0, metsXML.length());
	  // Complete the entry
	  zo.closeEntry();
	  
	  // Add ZIP entry for the redic XML file
	  String redicXML = g.render(template:"equipmentItem" , contentType:"text/xml", bean:item);
	  // "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	  // NOTE: do not change the name of the metadata file - this is referenced in the mets template
	  zo.putNextEntry(new ZipEntry("redic_metadata.xml"));
      zo.write(redicXML.getBytes(), 0, redicXML.length());
	  // Complete the entry
	  zo.closeEntry();
	 
	  // Tidy up - close the Zip stream
	  zo.close()
	  
	  String url = grailsApplication.config.sword.col
	  PostMessage message = new PostMessage();
	  message.setFilepath(tmpFile.getAbsolutePath());
	  message.setDestination(url);
	  message.setFiletype("application/zip");
	  message.setUseMD5(false);
	  message.setVerbose(true);
	  message.setNoOp(false);
	  message.setUserAgent(ClientConstants.SERVICE_NAME);
	  message.setFormatNamespace(grailsApplication.config.sword.packaging)

	  try{
		  ClientOptions options = swordV1Service.getCred();
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
			  item.dspaceHandle = handle
			  item.save()
			  			  
			  log.debug(response.toString())
		  } else {
		  	throw new Exception("Response from post was null")
		  }
		  
		  flash.message = "Item stored in DSpace with handle: ${handle}"
		  redirect(controller: "equipmentItem", action: "list")
		  
		  
		
	  } catch (Exception e){
	  	log.error("Caught exception: ${e.getMessage()}")
	  	throw e;
	  }

  }
  
}
