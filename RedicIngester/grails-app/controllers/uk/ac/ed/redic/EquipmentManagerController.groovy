package uk.ac.ed.redic

import java.util.Date;
import java.text.SimpleDateFormat
import java.text.ParsePosition

import org.apache.commons.logging.LogFactory;

class EquipmentManagerController {

	// Inject grailsApplication to get access to the config
	def grailsApplication

	private static final log = LogFactory.getLog(this)

	def index() { }

	def processFile(File f, String metadataType){
		int lineNum = 0;
		int numItems = 0;
		log.debug("Processing file...")

		f.eachLine { line ->

			// ignore header
			if (lineNum != 0){
				String [] cols = line.split("\t")
				
				// Mapping each piece of data to respective underlying schema
				EquipmentItem item = new EquipmentItem()
				switch(metadataType) {
					case 'Southampton data':
						item.approvalStatus = cols[0].equals("1")?true:false
						if (cols[1]) item.assetCost = new Float(cols[1])
						item.attachments = cols[2].equals("1")?true:false
						item.buildingID = cols[3]
						item.contact1Email = cols[4]
						item.contact1Name = cols[5]
						item.contact1Phone = cols[6]
						item.contact1URL = cols[7]


						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						item.created = df.parse(cols[8], new ParsePosition(0))

						item.description = cols[9]
						item.equipmentWebPage = cols[10]
						item.rowID = cols[11]
						item.faculty = cols[12]
						item.equipmentID = new Integer(cols[13])
						item.itemType = cols[14]
						item.level = cols[15]
						item.locationID = cols[16]
						item.modified = cols[17]
						item.name = cols[18]
						item.name1 = cols[19]
						item.orgUnitID = cols[20]
						item.propertyBag = cols[21]
						item.publishingLevel = cols[22]
						item.relatedFacilityID = cols[23]
						item.simpleName = cols[24]
						item.supplierWebPage = cols[25]
						item.taxonomy = cols[26]
						item.UrlPath = cols[27]
						item.uniqueID = cols[28]
						item.universityAssetCode = cols[29]
						break;
					case 'EPSRC data':
						item.name1 = cols[0];
						item.orgUnitID = cols[1]
						item.faculty = cols[2]
						item.taxonomy = cols[4];
						item.description = cols[5];
						item.equipmentWebPage = cols[6];
						def date = new Date();
						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						item.created = df.format(date);
						item.contact1Name = cols[7];
						item.contact1Phone = cols[8];
						item.contact1Email = cols[9];
						break;
					case 'Bath data':
						item.orgUnitID = cols[0];
						item.uniqueID = cols[1];
						item.name1 = cols[2];
						item.description = cols[3];
						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						item.created = df.parse(cols[5], new ParsePosition(0));
						item.faculty = cols[9];
						item.taxonomy = cols[7];
						item.contact1Name = cols[8]
						item.contact1Phone = cols[10];
						item.contact1URL = cols[11];
						item.contact1Email = cols[12];
						break;
				}
				try{
					if (!item.save(flush: true)) {
						item.errors.each { log.error(it) }
					} else {
						numItems++
					}
				} catch (Exception e){
					log.error(e.getMessage())

				}
			}
			lineNum++;
		}
		return numItems;
	}


	def uploadEquipmentData() {

		String tmpDir = grailsApplication.config.upload.dir

		log.debug("*******")
		log.debug("* uploadEquipmentData Config ")
		log.debug("* Upload Dir: ${tmpDir}")
		log.debug("*******")


		def fileUploaded, fileType, metadataType

		if (request.xhr){
			fileUploaded = request.getPart('file')
			fileType = params.'type';  
			metadataType = params.'metadata';
		} else {
			fileUploaded = request.getFile('file')
			fileType = params.'type'; //["type"];
			metadataType =  params.'metadata'; //['metadata'];
		}
		//println params
		log.debug("******* Processing File ********" + fileType + metadataType);

		// Processing of files tsv/xml format. tsv files get stored in the ingester database whereas xml files are directly synced to dspace
		if(fileType.equals('tsv')) {
			
			File uploadedFileObj = File.createTempFile("redic_", ".tsv", new File(tmpDir));
			fileUploaded.transferTo(uploadedFileObj);
			
			int numItems = processFile(uploadedFileObj, metadataType);

			// if ajax
			if (request.xhr) {
				render("${numItems} equipment items processed");
				redirect(action: "index");
			} else {
				flash.message = "${numItems} equipment items processed";
				redirect(action: "index");
			}
		}
		else if(fileType.equals('xml')) {
			File uploadedFileObj = File.createTempFile("xmlFile", ".xml", new File(tmpDir));
			fileUploaded.transferTo(uploadedFileObj);
			
			XMLController controller = new XMLController();
			controller.syncXML(uploadedFileObj, metadataType);
		}

	}

	// Delete all objects from the ingester database
	def deleteAll() {

		try{
			EquipmentItem.executeUpdate("delete EquipmentItem")
			flash.message = "All Equipment Items have been deleted"
		} catch (Exception e){
			flash.message = "Delete failed: ${e.getMessage()}"
		}

		redirect(action: "index")
	}

}
