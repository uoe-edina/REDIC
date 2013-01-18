/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.content.authority;

import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.httpclient.NameValuePair;

/**
 * Sample Equipment Title authority based on Skylighui
 *
 * WARNING: This is a very crude and incomplete implementation, done mainly
 *  as a proof-of-concept.  Any site that actually wants to use it will
 *  probably have to refine it (and give patches back to dspace.org).
 *
 * @see SkylightAPIProtocol
 * @author Pratyusha Doddapaneni
 * 
 */

public class SkylightEquipmentTitle extends SkylightAPIProtocol {
    
    private static final String RESULT = "record";
    private static final String LABEL = "dctitle";
    private static final String AUTHORITY = "id";
    
    public SkylightEquipmentTitle () {
        super();
    }

    
    @Override
    public Choices getMatches(String text, int collection, int start, int limit, String locale) {
        try {
            // punt if there is no query text
    if (text == null || text.trim().length() == 0)
    {
        return new Choices(true);
    }

        

    Choices result = query(RESULT, LABEL, AUTHORITY, text, start, limit);
    if (result == null)
    {
        result =  new Choices(true);
    }
    return result;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(SkylightEquipmentTitle.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Choices getMatches(String field, String text, int collection, int start, int limit, String locale) {
        return getMatches(text, collection, start, limit, locale);
    }
    
}
