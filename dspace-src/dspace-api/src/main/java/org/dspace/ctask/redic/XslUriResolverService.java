/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.ctask.redic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.logging.Level;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;
import org.apache.log4j.Logger;

class XslUriResolverService implements URIResolver {

    /**
    * Resolver service to provide xml and schema files to the crosswalk service
    * @author Pratyusha Doddapaneni
    */

    /*  @Override
     public Source resolve(String href, String base) throws TransformerException {
     throw new UnsupportedOperationException("Not supported yet.");
     } */
    private static Logger log = Logger.getLogger(XslUriResolverService.class);

    public Source resolve(String fileName, String base) throws TransformerException {
        Source result = null;
        result = new StreamSource(XslUriResolverService.class.getResourceAsStream(fileName));
        return result;

    }
    
}
