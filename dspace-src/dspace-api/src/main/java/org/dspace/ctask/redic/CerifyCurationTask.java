/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.ctask.redic;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.xml.transform.stream.StreamSource;
import org.apache.log4j.Logger;
import org.dspace.content.Bundle;
import org.dspace.authorize.AuthorizeException;
import org.dspace.content.Bitstream;
import org.dspace.content.DSpaceObject;
import org.dspace.content.Item;
import org.dspace.core.Context;
import org.dspace.curate.CurationTask;
import org.dspace.curate.Curator;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.dspace.ctask.redic.XMLValidator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.dspace.content.BitstreamFormat;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * A curation job to perform the crosswalk to convert equipment data into CERIF equipment data
 *
 * @author Pratyusha Doddapaneni
 */

public class CerifyCurationTask implements CurationTask {
    // invoking curator

    protected Curator curator = null;
    // curator-assigned taskId
    protected String taskId = null;
    // logger
    private static Logger log = Logger.getLogger(CerifyCurationTask.class);
    // The status of the link checking of this item
    private int status = Curator.CURATE_UNSET;
    // The results of link checking this item
    private List<String> results = null;
    private CrossWalkService service;
    private String redicSchema = "EquipmentSchema.xsd";
    private String cerifSchema = "CERIF_1.5_1.xsd";
    private XMLValidator validator;

    @Override
    public void init(Curator curator, String taskId) throws IOException {
        this.curator = curator;
        this.taskId = taskId;
        validator = new XMLValidator();
    }

    public boolean checkCerifFile(Bundle bundle) {
        try {
            if (bundle.getBitstreamByName("cfEquip.xml").equals(null)) {
            return true;
        } else {
            return false;
        }
        }
        catch (NullPointerException ex) {
            log.info("Cerif file not present, proceeding with transformation..");
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
                    org.jdom.Document document = builder.build(bundle.getBitstreamByName("redic_metadata.xml").retrieve());
                    org.jdom.Element rootElement = document.getRootElement();
                    String contact_name = rootElement.getChild("contact1Name").getText();
                    // String date = rootElement.getAttribute("date").getValue();
                    String description = rootElement.getChild("description").getChild("div").getText();
                    String taxonomy = rootElement.getChild("taxonomy").getText();
                    redicItem.addMetadata("dc", "contributor", "author", null, contact_name);
                    //   redicItem.addMetadata("dc", "date", "available", null, date);
                    redicItem.addMetadata("dc", "description", "abstract", null, description);
                    redicItem.addMetadata("dc", "subject", null, null, taxonomy);
                }
            }
        } catch (JDOMException ex) {
            java.util.logging.Logger.getLogger(CerifyCurationTask.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String addExtraElements(String result) {
        String str = null;
        try {


            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = df.format(new Date());

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new InputSource(new ByteArrayInputStream(result.getBytes("utf-8"))));

            Element rootElement = doc.getDocumentElement();
            rootElement.setAttribute("date", formattedDate);
            
            StringWriter buffer = new StringWriter();
            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult newResult = new StreamResult(buffer);
            transformer.transform(source, newResult);

            str = buffer.toString();
            

        } catch (TransformerException ex) {
            java.util.logging.Logger.getLogger(CerifyCurationTask.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            java.util.logging.Logger.getLogger(CerifyCurationTask.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            java.util.logging.Logger.getLogger(CerifyCurationTask.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(CerifyCurationTask.class.getName()).log(Level.SEVERE, null, ex);
        }
        return str;
    }

    @Override
    public int perform(DSpaceObject dso) throws IOException {

        // Add date as attribute
        if (dso instanceof Item) { 
            try {
                Item item = (Item) dso;

                boolean changed = false;
                for (Bundle bundle : item.getBundles()) {
                    if ("ORIGINAL".equals(bundle.getName())) {
                        service = new CrossWalkService();
                        //Bundle[] bundles = item.getBundles("ORIGINAL");

                        // Validate the incoming REDIC xml file
                        status = validator.validateXML(item, redicSchema, "redic");

                        if (checkCerifFile(bundle)) {
                            StreamSource source = new StreamSource(bundle.getBitstreamByName("redic_metadata.xml").retrieve());
                            //new StreamSource(CerifyCurationTask.class.getResourceAsStream("redic_metadata.xml"));
                            String result = service.transform(CrossWalkService.Crosswalk.CERIF, source);


                            // Inserting the generated xml file into the current bundle                    

                            Bitstream bstream;
                            
                            bstream = bundle.createBitstream(new ByteArrayInputStream(result.getBytes()));
                            bstream.setName("cfEquip.xml");

                            // Inserting metadata into the DC fields
                            insertDCMetadata(item);

                            changed = true;
                        } else {
                            log.info("Cerif conversion already done");
                            status = Curator.CURATE_SUCCESS;
                            status = validator.validateXML(item, cerifSchema, "cerif");
                            return status;
                        }

                    }

                    if (changed) {
                        // Check validity of the xml file against CERIF schema
                        status = validator.validateXML(item, cerifSchema, "cerif");
                        item.update();
                        status = Curator.CURATE_SUCCESS;
                    }
                }

            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(CerifyCurationTask.class.getName()).log(Level.SEVERE, null, ex);
            } catch (AuthorizeException ae) {
                // Something went wrong
                logDebugMessage(ae.getMessage());
                status = Curator.CURATE_ERROR;
            } catch (SQLException sqle) {
                // Something went wrong
                logDebugMessage(sqle.getMessage());
                status = Curator.CURATE_ERROR;
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(CerifyCurationTask.class.getName()).log(Level.SEVERE, null, ex);
                status = Curator.CURATE_FAIL;
                return status;
            }
        }
        return status;

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
