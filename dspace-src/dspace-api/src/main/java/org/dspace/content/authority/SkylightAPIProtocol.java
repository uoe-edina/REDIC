/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.content.authority;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import org.apache.log4j.Logger;

import org.dspace.core.ConfigurationManager;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.util.EncodingUtil;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.HttpException;

/**
 * Choice Authority based on Skylightui  - for Publishers and Journals
 * See the subclasses  SHERPARoMEOPublisher and SHERPARoMEOJournalTitle
 * for actual choice plugin implementations.  This is a superclass
 * containing all the common protocol logic.
 *
 * Reads these DSpace Config properties:
 *
 *    # contact URL for server
 *    sherpa.romeo.url = http://www.sherpa.ac.uk/romeoapi11.php
 *
 * WARNING: This is a very crude and incomplete implementation, done mainly
 *  as a proof-of-concept.  Any site that actually wants to use it will
 *  probably have to refine it (and give patches back to dspace.org).
 *
 * @see SHERPARoMEOPublisher
 * @see SHERPARoMEOJournalTitle
 * @author Larry Stone  
 * @author Pratyusha
 * 
 * contact URL of the server providing authority
 * skylight.url = http://localhost/skylightui
 * 
 */
public abstract class SkylightAPIProtocol implements ChoiceAuthority
{
    private static Logger log = Logger.getLogger(SkylightAPIProtocol.class);

    // contact URL from configuration
    private static String url = null;

    public SkylightAPIProtocol()
    {
        if (url == null)
        {
            url = ConfigurationManager.getProperty("skylight.url");

            // sanity check
            if (url == null)
            {
                throw new IllegalStateException("Missing DSpace configuration keys for Skylight Query");
            }
        }
    }

    // this implements the specific RoMEO API args and XML tag naming
    public abstract Choices getMatches(String text, int collection, int start, int limit, String locale);

    public Choices getBestMatch(String field, String text, int collection, String locale)
    {
        return getMatches(field, text, collection, 0, 2, locale);
    }

    // XXX FIXME just punt, returning value, never got around to
    //  implementing a reverse query.
    public String getLabel(String field, String key, String locale)
    {
        return key;
    }

    // NOTE - ignore limit and start for now
    protected Choices query(String result, String label, String authority,
                            String args, int start, int limit) throws UnsupportedEncodingException
    {
        String encodedArgs = new String(args.getBytes("UTF-8"), "UTF-8");
        HttpClient hc = new HttpClient();
        String srUrl = url + "/search/" + encodedArgs + ".xml?field=" + label;
        GetMethod get = new GetMethod(srUrl);

        log.info("Trying Skylight Query, URL="+srUrl);

        try
        {
            int status = hc.executeMethod(get);
            log.info("Status" + status);
            if (status == 200)
            {
                SAXParserFactory spf = SAXParserFactory.newInstance();
                SAXParser sp = spf.newSAXParser();
                XMLReader xr = sp.getXMLReader();
                SRHandler handler = new SRHandler(result, label, authority);

                // XXX FIXME: should turn off validation here explicitly, but
                //  it seems to be off by default.
                xr.setFeature("http://xml.org/sax/features/namespaces", true);
                xr.setContentHandler(handler);
                xr.setErrorHandler(handler);
                InputSource src = new InputSource(get.getResponseBodyAsStream());
                log.info("Source from sklightui :: " + src.toString());
                xr.parse(src);
                
                int confidence;
                if (handler.total == 0)
                {
                    confidence = Choices.CF_NOTFOUND;
                }
                else if (handler.total == 1)
                {
                    confidence = Choices.CF_UNCERTAIN;
                }
                else
                {
                    confidence = Choices.CF_AMBIGUOUS;
                }
                return new Choices(handler.result, start, handler.total, confidence, false);
            }
        }
        catch (HttpException e)
        {
            log.error("Skylight query failed: ", e);
            return null;
        }
        catch (IOException e)
        {
            log.error("Skylight query failed: ", e);
            return null;
        }
        catch (ParserConfigurationException  e)
        {
            log.warn("Failed parsing SHERPA/RoMEO result: ", e);
            return null;
        }
        catch (SAXException  e)
        {
            log.warn("Failed parsing Skylight result: ", e);
            return null;
        }
        finally
        {
            get.releaseConnection();
        }
        return null;
    }

    // SAX handler to grab seach results (name value pairs) from result
    private static class SRHandler
        extends DefaultHandler
    {
        private Choice result[] = null;
        int rindex = 0; // result index
        int total = 0;

        // name of element containing a result, e.g. <journal>
        private String resultElement = null;

        // name of element containing the label e.g. <name>
        private String labelElement = null;

        // name of element containing the authority value e.g. <issn>
        private String authorityElement = null;

        protected String textValue = null;

        public SRHandler(String result, String label, String authority)
        {
            super();
            resultElement = result;
            labelElement = label;
            authorityElement = authority;
        }

        // NOTE:  text value MAY be presented in multiple calls, even if
        // it all one word, so be ready to splice it together.
        // BEWARE:  subclass's startElement method should call super()
        // to null out 'value'.  (Don't you miss the method combination
        // options of a real object system like CLOS?)
        public void characters(char[] ch, int start, int length)
            throws SAXException
        {
            String newValue = new String(ch, start, length);
            if (newValue.length() > 0)
            {
                if (textValue == null)
                {
                    textValue = newValue;
                }
                else
                {
                    textValue += newValue;
                }
            }
        }

        // if this was the FIRST "numhits" element, it's size of results:
        public void endElement(String namespaceURI, String localName,
                                 String qName)
            throws SAXException
        {
            log.info("namespaceURI "+ namespaceURI +"localName" + localName + "qname" + qName);
            if (localName.equals("numhits"))
            {
                String stotal = textValue.trim();
                if (stotal.length() > 0)
                {
                    total = Integer.parseInt(stotal);
                    result = new Choice[total];
                    if (total > 0)
                    {
                        result[0] = new Choice();
                        log.info("Got "+total+" records in results.");
                    }
                }
            }
            else if (localName.equals(resultElement))
            {
                // after start of result element, get next hit ready
                if (++rindex < result.length)
                {
                    result[rindex] = new Choice();
                }
            }
            else if (localName.equals(labelElement) && textValue != null)
            {
                // plug in label value
                result[rindex].value = textValue.trim();
                result[rindex].label = result[rindex].value; 
            }
            else if (authorityElement != null && localName.equals(authorityElement) && textValue != null)
            {
                // plug in authority value
                result[rindex].authority = textValue.trim();
            }
            else if (localName.equals("message") && textValue != null)
            {
                // error message
                log.info("Skylight response error message: " + textValue.trim());
            }
        }

        // subclass overriding this MUST call it with super()
        public void startElement(String namespaceURI, String localName,
                                 String qName, Attributes atts)
            throws SAXException
        {
            textValue = null;
        }

        public void error(SAXParseException exception)
            throws SAXException
        {
            throw new SAXException(exception);
        }

        public void fatalError(SAXParseException exception)
            throws SAXException
        {
            throw new SAXException(exception);
        }
    }
}
