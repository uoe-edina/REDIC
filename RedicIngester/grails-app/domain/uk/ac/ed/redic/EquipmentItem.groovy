package uk.ac.ed.redic

class EquipmentItem {

	Boolean approvalStatus	
	Float assetCost	
	Boolean attachments
	String buildingID
	String contact1Email	
	String contact1Name	
	String contact1Phone	
	String contact1URL	
	Date created
	String description	
	String equipmentWebPage	
	String rowID	
	String faculty	
	Integer equipmentID	
	String itemType	
	String level	
	String locationID	
	String modified	
	String name	
	String name1	
	String orgUnitID	
	String propertyBag	
	String publishingLevel	
	String relatedFacilityID	
	String simpleName	
	String supplierWebPage	
	String taxonomy	
	String UrlPath	
	String uniqueID	
	String universityAssetCode	
	String dspaceHandle
	
	
	Date dateAdded
	Date lastUpdated
	Date lastSynced
	
	def beforeInsert() {
		dateAdded = new Date()
		lastUpdated = dateAdded
	}
	def beforeUpdate() {
		lastUpdated = new Date()
	}
 
 	static constraints = {
 		lastSynced nullable:true
 		dateAdded nullable:true
 		lastUpdated nullable:true
		dspaceHandle nullable:true
 	}
 	
}
