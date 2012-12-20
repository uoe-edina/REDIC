<?xml version="1.0" encoding="utf-8" standalone="no"?>
<mets ID="sort-mets_mets" OBJID="sword-mets" LABEL="DSpace SWORD Item"
	PROFILE="DSpace METS SIP Profile 1.0" xmlns="http://www.loc.gov/METS/"
	xmlns:xlink="http://www.w3.org/1999/xlink"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.loc.gov/METS/ http://www.loc.gov/standards/mets/mets.xsd"
	xmlns:dc="http://purl.org/dc/elements/1.1/">

	<metsHdr CREATEDATE="2007-09-01T00:00:00">
		<agent ROLE="CUSTODIAN" TYPE="ORGANIZATION">
			<name>REDIC</name>
		</agent>
	</metsHdr>
	
	<dmdSec ID="sword-mets-dmd-1" GROUPID="sword-mets-dmd-1_group-1">
		<mdWrap LABEL="SWAP Metadata" MDTYPE="DC" MIMETYPE="text/xml">

			<xmlData>
				
				<dc:title>${it?.name1}</dc:title>
				<dc:description>${it?.description}</dc:description>
  				
			</xmlData>
		</mdWrap>
	</dmdSec>

	<fileSec>
		<fileGrp ID="sword-mets-fgrp-1" USE="CONTENT">
			<file GROUPID="sword-mets-fgid-0" ID="sword-mets-file-1"
				MIMETYPE="application/xml">
				<FLocat LOCTYPE="URL" xlink:href="redic_metadata.xml" />
			</file>
		</fileGrp>
	</fileSec>
	
	<structMap ID="sword-mets-struct-1" LABEL="structure"
		TYPE="LOGICAL">
		<div ID="sword-mets-div-1" DMDID="sword-mets-dmd-1" TYPE="SWORD Object">
			<div ID="sword-mets-div-2" TYPE="File">
				<fptr FILEID="sword-mets-file-1" />
			</div>
		</div>
	</structMap>

</mets>