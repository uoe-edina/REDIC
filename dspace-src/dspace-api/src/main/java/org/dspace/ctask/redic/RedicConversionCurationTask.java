/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */


package org.dspace.ctask.redic;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import javax.xml.transform.stream.StreamSource;
import org.apache.log4j.Logger;
import org.dspace.authorize.AuthorizeException;
import org.dspace.content.Bitstream;
import org.dspace.content.Bundle;
import org.dspace.content.DSpaceObject;
import org.dspace.content.Item;
import org.dspace.core.Context;
import org.dspace.curate.CurationTask;
import org.dspace.curate.Curator;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * A curation job to perform the crosswalk to convert CERIF equipment data into basic equipment data 
 *
 * @author Pratyusha Doddapaneni
 */


public class RedicConversionCurationTask implements CurationTask {

    // invoking curator
    protected Curator curator = null;
    // curator-assigned taskId
    protected String taskId = null;
    // logger
    private static Logger log = Logger.getLogger(RedicConversionCurationTask.class);
    // The status of the link checking of this item
    private int status = Curator.CURATE_UNSET;
    // The results of link checking this item
    private List<String> results = null;
    private CrossWalkService service;
    private XMLValidator validator;
    private String redicSchema = "EquipmentSchema.xsd";
    private String cerifSchema = "CERIF_1.5_1.xsd";

    @Override
    public void init(Curator curator, String taskId) throws IOException {
        this.curator = curator;
        this.taskId = taskId;
        validator = new XMLValidator();
    }

    public boolean checkRedicFile(Bundle bundle) {
        try {
        if (bundle.getBitstreamByName("redic_metadata.xml").equals(null)) {
           return true;
        } else {
            return false;
        }
        }
        catch(NullPointerException e) {
            log.info("Redic file not present, proceeding with transformation..");
            return true;
        }
    }

    public void insertDCMetadata(Item redicItem) throws SQLException, AuthorizeException {
        //       DocumentBuilderFactory builderFactory =
        //             DocumentBuilderFactory.newInstance();
        SAXBuilder builder = new SAXBuilder();
        //       builder = builderFactory.newDocumentBuilder();
        try {
            for (Bundle bundle : redicItem.getBundles()) {
                if ("ORIGINAL".equals(bundle.getName())) {
                    Document document = builder.build(bundle.getBitstreamByName("redic_metadata.xml").retrieve());
                    Element rootElement = document.getRootElement();
                    String contact_name = rootElement.getChild("contact1Name").getText();
                    // String date = rootElement.getAttribute("date").getValue();
                    String description = rootElement.getChild("description").getText();
                    //String taxonomy = rootElement.getChild("taxonomy").getText();
                    redicItem.addMetadata("dc", "contributor", "author", null, contact_name);
                    //   redicItem.addMetadata("dc", "date", "available", null, date);
                    redicItem.addMetadata("dc", "description", "abstract", null, description);
                    //redicItem.addMetadata("dc", "subject", null, null, taxonomy);
                }
            }
        }  catch (JDOMException ex) {
            java.util.logging.Logger.getLogger(RedicConversionCurationTask.class.getName()).log(Level.SEVERE, null, ex);
        } catch(NullPointerException e) {
            log.info("Redic file not present, proceeding with transformation..");
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(RedicConversionCurationTask.class.getName()).log(Level.SEVERE, null, ex);
        }

    }


    @Override
    public int perform(DSpaceObject dso) throws IOException {

        if (dso instanceof Item) {
            try {
                Item item = (Item) dso;

                boolean changed = false;
                for (Bundle bundle : item.getBundles()) {
                    
                    if ("ORIGINAL".equals(bundle.getName())) {
                        service = new CrossWalkService();
                        //Bundle[] bundles = item.getBundles("ORIGINAL");
                        
                        
                        status = validator.validateXML(item, cerifSchema, "cerif");
                        
                        if (checkRedicFile(bundle)) {
                            StreamSource source = new StreamSource(bundle.getBitstreamByName("cfEquip.xml").retrieve());
                            String result = service.transform(CrossWalkService.Crosswalk.REDIC, source);
                            Bitstream bstream;
                            bstream = bundle.createBitstream(new ByteArrayInputStream(result.getBytes()));
                            bstream.setName("redic_metadata.xml");
                            insertDCMetadata(item);
                            changed = true;
                        } else {
                            log.info("Redic conversion already done");
                            status = Curator.CURATE_SUCCESS;
                            status = validator.validateXML(item, redicSchema, "redic");
                            return Curator.CURATE_SUCCESS;
                        }
                    }
                    if (changed) {
                        // Check validity of the xml file against REDIC schema
                        status = validator.validateXML(item, redicSchema, "redic");
                        item.update();
                        status = Curator.CURATE_SUCCESS;
                    }
                }

            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(CerifyCurationTask.class.getName()).log(Level.SEVERE, null, ex);
            } catch (AuthorizeException ae) {
                logDebugMessage(ae.getMessage());
                status = Curator.CURATE_ERROR;
            } catch (SQLException sqle) {
                logDebugMessage(sqle.getMessage());
                status = Curator.CURATE_ERROR;
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(CerifyCurationTask.class.getName()).log(Level.SEVERE, null, ex);

                return Curator.CURATE_FAIL;
            }
        }
        return Curator.CURATE_SUCCESS;

    }

    private void logDebugMessage(String message) {
        if (log.isDebugEnabled()) {
            log.debug(message);
        }
    }
    
    @Override
    public int perform(Context ctx, String id) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
