/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.ctask.redic;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.dspace.authorize.AuthorizeException;
import org.dspace.content.Bitstream;
import org.dspace.content.Bundle;
import org.dspace.content.DSpaceObject;
import org.dspace.content.Item;
import org.dspace.core.ConfigurationManager;
import org.dspace.curate.AbstractCurationTask;
import org.dspace.curate.Curator;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.*;
import org.xml.sax.SAXException;

/**
 * Service to perform validation of XML records
 * @author pratyusha
 */
public class XMLValidator extends AbstractCurationTask {
    
    private static Logger log = Logger.getLogger(XMLValidator.class);
    private int status = Curator.CURATE_UNSET;
    
    
    public int validateXML(Item item, String schemaName, String xmlType) throws SQLException, IOException {
        InputStream inputstream = null;
        try {
            Source schemaFile = new StreamSource(XMLValidator.class.getResourceAsStream(schemaName)); //"EquipmentSchema.xsd"));
            Bundle bundle = item.getBundles("ORIGINAL")[0];
	    String xmlFileName = (xmlType.equals("redic")) ? "redic_metadata.xml" : "cfEquip.xml";
            Bitstream bitstream = bundle.getBitstreamByName(xmlFileName);
            inputstream = bitstream.retrieve();
            Source xmlFile = new StreamSource(inputstream);
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(schemaFile);
            log.info("Validating " + bitstream.getName() + " . . . ");
            Validator validator = schema.newValidator();
            validator.validate(xmlFile);
            log.info("XML file conforms to "+xmlType);
            System.out.println("VALID");
            return 0;
        } catch (AuthorizeException ex) {
            java.util.logging.Logger.getLogger(XMLValidator.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (SAXException exc) {
            status = Curator.CURATE_FAIL;
                log.error(exc.getLocalizedMessage());
                System.out.println("INVALID");
        }
        finally {
            try {
                inputstream.close();
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(XMLValidator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return status;
        
    }

    @Override
    public int perform(DSpaceObject dso) throws IOException {
       try {
        status = Curator.CURATE_SKIP;
        log.info("The target dso is ::" + dso.getName());
        if (dso instanceof Item)
        {
            status = Curator.CURATE_SUCCESS;
            Item item = (Item)dso;
	    System.out.println("Validating REDIC xml file");
	    status = this.validateXML(item, "EquipmentSchema.xsd", "redic");
	}
        }
	catch (SQLException ex) {
                log.error(ex.getLocalizedMessage());
            } 
	return status;
    }
}


