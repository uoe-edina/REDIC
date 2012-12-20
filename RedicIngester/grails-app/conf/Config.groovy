// locations to search for config files that get merged into the main config
// config files can either be Java properties files or ConfigSlurper scripts

// grails.config.locations = [ "classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]

// if (System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }


import java.net.InetAddress
currentLocalHostName = InetAddress.getLocalHost().getHostName()

String customConfig = null
String logsdir = ""
// basedir will be set if a grails command such as run-app is used to start the app. If the app however is built into a WAR and
// deployed into a servlet container, basedir will be null! (remember basedir is an ant property and only available as part of the
// build process - when running under a servlet container, ant is not in the mix!)
//NOTE Gwaller 31/1/12 Used to be 'baseDir' in Grails 1 now 'basedir' in Grails 2
if (basedir){
	customConfig = "${basedir}/etc/${currentLocalHostName}-config.groovy"
	logsdir = "${basedir}/logs"
} else {
	// assume we are running under a servlet container

	/* We cannot use the ServletContextHolder here to get the real path to the root of the webapp i.e. real path for "/".
	   ServletContextHolder is null - it could also be a change since Grails 2.0?
	   
		The only way therefore to reliably establish a real path to a config file embedded in the expanded WAR is to
		find the path to this compiled class and then work ou a relative path from that.
		
		The compiled class will be expanded to <container>/webapps/<app name>/WEB-INF/classes/BuildConfig.class
	*/

	// Below gets the path to the source code for this class - notice the string replace of the class name to just get the path to the containing directory
	// i.e. pathToWEBINFClasses will be '<container>/webapps/<app name>/WEB-INF/classes/' including the trailing '/'
	String pathToWEBINFClasses = getClass().getProtectionDomain().getCodeSource().getLocation().getFile().replace(getClass().getSimpleName() + ".class", "");

	// Note lack of / at end of pathToWEBINFClasses - this will already have the /
	customConfig = "${pathToWEBINFClasses}../../suncat-config.groovy"; // NOTE THE NAME OF THE CONFIG FILE - IF THIS IS CHANGED IT SHOULD ALSO BE CHANGED IN BuildConfig.groovy TOO.

	logsdir = "${pathToWEBINFClasses}../../logs"
	
}

if (customConfig){
	println "Adding config file location to grails.config.locations: file:${customConfig}"
	grails.config.locations = [ "file:${customConfig}"]
	
}






grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination
grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.use.accept.header = false
grails.mime.types = [ html: ['text/html','application/xhtml+xml'],
                      xml: ['text/xml', 'application/xml'],
                      text: 'text/plain',
                      js: 'text/javascript',
                      rss: 'application/rss+xml',
                      atom: 'application/atom+xml',
                      css: 'text/css',
                      csv: 'text/csv',
                      all: '*/*',
                      json: ['application/json','text/json'],
                      form: 'application/x-www-form-urlencoded',
                      multipartForm: 'multipart/form-data'
                    ]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// What URL patterns should be processed by the resources plugin
grails.resources.adhoc.patterns = ['/images/*', '/css/*', '/js/*', '/plugins/*']


// The default codec used to encode data with ${}
grails.views.default.codec = "none" // none, html, base64
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"
// enable Sitemesh preprocessing of GSP pages
grails.views.gsp.sitemesh.preprocess = true
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []
// whether to disable processing of multi part requests
grails.web.disable.multipart=false

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

// enable query caching by default
grails.hibernate.cache.queries = true

// set per-environment serverURL stem for creating absolute links
environments {
    development {
        grails.logging.jul.usebridge = true
    }
    production {
        grails.logging.jul.usebridge = false
        // TODO: grails.serverURL = "http://www.changeme.com"
    }
}

// log4j configuration
log4j = {
    // Example of changing the log pattern for the default console
    // appender:
    //
    //appenders {
    //    console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
    //}
	
	appenders {
		rollingFile name: "applog", maxFileSize: "2MB", file: "${logsdir}/redic.log"
	}

    error  'org.codehaus.groovy.grails.web.servlet',  //  controllers
           'org.codehaus.groovy.grails.web.pages', //  GSP
           'org.codehaus.groovy.grails.web.sitemesh', //  layouts
           'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
           'org.codehaus.groovy.grails.web.mapping', // URL mapping
           'org.codehaus.groovy.grails.commons', // core / classloading
           'org.codehaus.groovy.grails.plugins', // plugins
           'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
           'org.springframework',
           'org.hibernate',
           'net.sf.ehcache.hibernate'
		   
		   
	   environments {
		   development {
			   // Debug level for custom classes in development
			   debug 'uk.ac'
			   debug 'org.swordapp.client'
			   
			   root {
				   // The below configures error logging and above to the default appender (i.e. the console)
				   error 'stdout', 'applog'
			   }
		   }
		   production {
			   // Only log error level in production
			   error  'uk.ac'
			  
			   root {
				   // The below configures error logging and above to the default appender (i.e. the console)
				   error 'applog'
			   }
		   }
		   test {
			   // Debug level for custom classes in development
			   debug 'uk.ac'
			   
			   root {
				   // The below configures error logging and above to the default appender (i.e. the console)
				   error 'stdout', 'applog'
			   }
		   }
		   
	   }
		   
		   
}
