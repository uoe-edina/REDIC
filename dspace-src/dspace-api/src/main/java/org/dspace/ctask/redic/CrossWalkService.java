/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.ctask.redic;

import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.Source;
import org.apache.log4j.Logger;

public class CrossWalkService {

	/**
 	* A crosswalk service to translate a group of metadata records from one format into another
 	*
 	* @author Pratyusha Doddapaneni
 	*/


	// The LocalXslUriResolverService does the hard work of finding the XSL files which are referenced in include and import XSL commands
	XslUriResolverService localXslUriResolverService;

	private static Logger log = Logger.getLogger(CrossWalkService.class);

	private static TransformerFactory tfactory = TransformerFactory.newInstance();
	
	
	public static enum Crosswalk {
                CERIF ("REDICtoCERIFEquipment.xsl","redic-cerif"),
                REDIC ("CERIFtoREDICEquipment.xsl", "cerif-redic");

		private final String XSL_NAME;
		private String friendlyName;
		private Templates templates;

		Crosswalk(String n, String friendlyName){
			this.XSL_NAME = n;
			this.friendlyName = friendlyName;
			this.templates = null;
		}

		public static Crosswalk find(String str){
			for(Crosswalk v : values()){
				if( v.toString().equalsIgnoreCase(str)){
					return v;
				}
			}
			return null;
		}

		public String toString(){
			return this.friendlyName;
		}
		
		public Templates getTemplates(){
			return this.templates;
		}
		
		public void setTemplates(Templates t){
			this.templates = t;
		}
		
	}


	
	
	public String transform(Crosswalk xwalk, StreamSource xmlSource) throws Exception{
            localXslUriResolverService = new XslUriResolverService();
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                System.out.println("Here 1");
		StringWriter transformedXML = new StringWriter();
		TransformerFactory factory = TransformerFactory.newInstance();
                System.out.println("Here 2");
                System.out.println(xwalk.XSL_NAME);
		Source sourceXsl = localXslUriResolverService.resolve(xwalk.XSL_NAME, null);
                System.out.println("Here 3");
		if (sourceXsl == null){
			throw new Exception("Null XSL returned from localXslUriResolverService - cannot find XSL for " + xwalk.XSL_NAME);
		}

		Transformer transformer = factory.newTransformer(sourceXsl);

		transformer.setURIResolver(localXslUriResolverService);
		transformer.setOutputProperty("omit-xml-declaration", "yes");
                transformer.setParameter("date", format.format(calendar.getTime()));
		transformer.transform(xmlSource, new StreamResult(transformedXML));

		return transformedXML.toString();
	}


}
