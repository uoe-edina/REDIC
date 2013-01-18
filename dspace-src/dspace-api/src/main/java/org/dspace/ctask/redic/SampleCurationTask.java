/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.ctask.redic;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.dspace.curate.Curator;
import org.dspace.content.Collection;
import org.dspace.content.DSpaceObject;
import org.dspace.content.Item;
import org.dspace.core.Context;
import org.dspace.curate.CurationTask;
import org.dspace.handle.HandleManager;

/**
 * A sample curation task to extract the collection name of the item
 * 
 * @author Pratyusha Doddapaneni
 */

public class SampleCurationTask implements CurationTask {

    // invoking curator
    protected Curator curator = null;
    // curator-assigned taskId
    protected String taskId = null;
    // logger
    private static Logger log = Logger.getLogger(SampleCurationTask.class);


  // The status of the link checking of this item
    private int status = Curator.CURATE_UNSET;

    // The results of link checking this item
    private List<String> results = null;



    @Override
    public void init(Curator curator, String taskId) throws IOException
    {
        this.curator = curator;
        this.taskId = taskId;
    }  


    /**
     * Extracts the name of the collection.
     *
     * @param dso The DSpaceObject to be checked
     * @return The curation task status of the checking
     * @throws java.io.IOException thrown when something goes wrong
     */
    @Override
    public int perform(DSpaceObject dso) throws IOException
    {

        if (dso instanceof Item)
        {
            try {
                Item item = (Item)dso;
                Collection[] collections = item.getCollections();
                String collectionName = collections[0].getName();
                System.out.println("Colletion Name :: "+ collectionName);
                return Curator.CURATE_SUCCESS;
            } catch (SQLException ex) {
                java.util.logging.Logger.getLogger(SampleCurationTask.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return Curator.CURATE_FAIL;
    }

    
    /**
     * Extracts the name of the collection.
     * 
     * @param ctx DSpace context object
     * @param id persistent ID for DSpace object
     * @return status code
     * @throws Exception
     */
    @Override
    public int perform(Context ctx, String id) throws IOException {
        try {
            DSpaceObject dso = HandleManager.resolveToObject(ctx, id);
            if (dso instanceof Item)
            {
                try {
                    Item item = (Item)dso;
                    Collection[] collections = item.getCollections();
                    String collectionName = collections[0].getName();
                    System.out.println("Colletion Name :: "+ collectionName);
                    return Curator.CURATE_SUCCESS;
                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(SampleCurationTask.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (IllegalStateException ex) {
            java.util.logging.Logger.getLogger(SampleCurationTask.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(SampleCurationTask.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Curator.CURATE_FAIL; 
    }
}

