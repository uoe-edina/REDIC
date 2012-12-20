package uk.ac.ed.redic

import java.net.MalformedURLException;
import java.security.MessageDigest

import org.purl.sword.base.ServiceDocument;
import org.purl.sword.client.Client
import org.purl.sword.client.ClientOptions;
import org.purl.sword.client.Client;
import org.purl.sword.client.Status;
import org.apache.commons.logging.LogFactory;


/*
 * Class providing common set of SWORD based helper functions 
 */
class SwordV1Service {

	// Inject grailsApplication to get access to the config
	def grailsApplication
	
	private static final String SWORD_USER_KEY = "sword.user"
	private static final String SWORD_PASS_KEY = "sword.user"
	private static final String SWORD_SD_URL_KEY = "sword.sd.url"
	
	private static final log = LogFactory.getLog(this);
	
	
	private void initialiseServer(Client client, String location, String username, String password)
	throws MalformedURLException
	{
		URL url = new URL(location);
		int port = url.getPort();
		if( port == -1 )
		{
			port = 80;
		}
 
		client.setServer(url.getHost(), port);
 
		if (username != null && username.length() > 0 &&
			password != null && password.length() > 0 )
		{
			log.info("Setting the username/password: " + username + " "
					+ password);
			client.setCredentials(username, password);
		}
		else
		{
			client.clearCredentials();
		}
	}

   
	// Credentials to perform the sword ingest
	private ClientOptions getCred(){
		String user = grailsApplication.config.sword.user
		print "USER :: " + user;
		String pass = grailsApplication.config.sword.pass
		
		log.debug("*******")
		log.debug("* SWORD Config ")
		log.debug("* User: ${user}")
		log.debug("* Pass: ${pass}")
		log.debug("*******")
		
		ClientOptions options = new ClientOptions()
		options.setUsername(user);
		options.setPassword(pass);
		
		return options
	}
	
	// Retrieves the SWORD service document from dspace
	private ServiceDocument getSD(Client client){
		
		String url = grailsApplication.config.sword.sd.url
		
		log.debug("*******")
		log.debug("* SWORD Config ")
		log.debug("* SD Url: ${url}")
		log.debug("*******")
		
		ClientOptions options = getCred()
		initialiseServer(client, url, options.getUsername(), options.getPassword());
 
		ServiceDocument document = client.getServiceDocument(url, "");
		Status status = client.getStatus();
		log.debug("SWORD service document call status is: " + status);
		
		if (status.getCode() == 200){
			return document
		} else {
				return null
		}
		
		
	}
	
	
   // Security layer for file upload
   def generateMD5(final file) {
	   MessageDigest digest = MessageDigest.getInstance("MD5")
	   file.withInputStream(){is->
	   byte[] buffer = new byte[8192]
	   int read = 0
		  while( (read = is.read(buffer)) > 0) {
				 digest.update(buffer, 0, read);
			 }
		 }
	   byte[] md5sum = digest.digest()
	   BigInteger bigInt = new BigInteger(1, md5sum)
	   return bigInt.toString(16)
	}
}
