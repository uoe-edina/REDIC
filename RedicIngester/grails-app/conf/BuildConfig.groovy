println "******************************************"
println "In BuildConfig.groovy"

/*
		NOTE
		----

		Config.groovy has the ability to support custom config files via grails.config.locations. These are then loaded and
		merged with the config. Unfortunately not every Grails variable can be specified in Config.groovy or a custom
		config file. The config vars which are set as part of the BuildConfig (ie this script), are available in the
		hash buildProps - see "_GrailsSettings.groovy" in the Grails install dir and closure getPropertyValue.
		BuildConfig.groovy is parsed *BEFORE* Config.groovy and as such setting build vars in Config.groovy serves no
		purpose.
		An important example of a var which cannot be set in Config.groovy is 'grails.server.port.http' the HTTP server port.
		To allow custom setting of all vars in a custom config file, the below code will load the same
		custom config file as the Config.groovy script does but pulls out variables which should be set in the
		buildProps hash and sets them accordingly. This has the drawback of making the startup time slightly slower as
		the same file is parsed twice but has the advantage of making these variables easily configurable.

*/
import java.net.InetAddress
currentLocalHostName = InetAddress.getLocalHost().getHostName()

String customConfig = null
// basedir will be set if a grails command such as run-app is used to start the app. If the app however is built into a WAR and
// deployed into a servlet container, basedir will be null! (remember basedir is an ant property and only available as part of the
// build process - when running under a servlet container, ant is not in the mix!)
//NOTE Gwaller 31/1/12 Used to be 'baseDir' in Grails 1 now 'basedir' in Grails 2
if (basedir){
	customConfig = "${basedir}/etc/${currentLocalHostName}-config.groovy"
} else {
	// assume we are running under a servlet container

	/* We cannot use the ServletContextHolder here to get the real path to the root of the webapp i.e. real path for "/".
	   This is probably due to the fact that this script is used during the build process and at that time the
	   ServletContext will not exist. It could also be a change since Grails 2.0.
	   
		The only way therefore to reliably establish a real path to a config file embedded in the expanded WAR is to
		find the path to this compiled class and then work ou a relative path from that.
		
		The compiled class will be expanded to <container>/webapps/<app name>/WEB-INF/classes/BuildConfig.class
	*/

	// Below gets the path to the source code for this class - notice the string replace of the class name to just get the path to the containing directory
	// i.e. pathToWEBINFClasses will be '<container>/webapps/<app name>/WEB-INF/classes/' including the trailing '/'
	String pathToWEBINFClasses = getClass().getProtectionDomain().getCodeSource().getLocation().getFile().replace(getClass().getSimpleName() + ".class", "");

	// Note lack of / at end of pathToWEBINFClasses - this will already have the /
	customConfig = "${pathToWEBINFClasses}../../${currentLocalHostName}-config.groovy";
}

if (customConfig){
	println "Parsing config file file:${customConfig}"
	def fLocation = "file:${customConfig}"
	def customConfigObj = new ConfigSlurper().parse(fLocation.toURL())
	
	// Pull out the props to set in the hash buildProps
	grails.server.port.http = customConfigObj.grails.server.port.http
	grails.server.port.https = customConfigObj.grails.server.port.https
	grails.server.host = customConfigObj.grails.server.host
	
	/*
	 * The below closure is only relevant when a WAR file is being built to deploy to a Servlet Container.
	 * We need to copy the custom config to the staging dir (where the WAR file is constructed) so that the
	 * config file is packaged up inside the WAR (and can be read by the above lines when the app is running).
	 * DO NOT CHANGE THE PATH BELOW WITHOUT MAKING AN EQUIVALENT CHANGE ABOVE (AND IN Config.groovy)
	 */
	
	// Now ensure we have a closure set for grails.war.resources to ensure we copy the custom config to the stagingDir used to build the war
	// Note the syntax here for a copy - this is (I think) an AntBuilder call which does an Ant copy. See _GrailsWar.groovy around line 279 (on grails2.0)
	grails.war.resources = { stagingDir ->
		copy(file:"${customConfig}", tofile:"${stagingDir}/redic-config.groovy")
		mkdir(dir:"${stagingDir}/logs")
	  }
}

println "******************************************"

/*
 * Build Directories
 */
grails.project.work.dir = "build"
grails.project.class.dir = "build/classes"
grails.project.test.class.dir = "build/test-classes"
grails.project.test.reports.dir = "build/test-reports"
grails.project.war.file = "dist/${appName}-${appVersion}.war"










grails.servlet.version = "2.5" // Change depending on target container compliance (2.5 or 3.0)
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.target.level = 1.6
grails.project.source.level = 1.6
//grails.project.war.file = "target/${appName}-${appVersion}.war"

grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "error" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    checksums true // Whether to verify checksums on resolve

    repositories {
        inherits true // Whether to inherit repository definitions from plugins
        grailsPlugins()
        grailsHome()
        grailsCentral()
        mavenCentral()

        // uncomment these to enable remote dependency resolution from public Maven repositories
        //mavenCentral()
        //mavenLocal()
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.

		runtime 'postgresql:postgresql:9.1-901.jdbc4'
		// for sword v2
		compile (group:'org.apache.abdera', name:'abdera-client', version:'1.1.1')
		compile (group:'xom', name:'xom', version:'1.2.5')
		
		// for sword v1
		compile (group:'org.swordapp', name:'sword-common', version:'1.1')
    }

    plugins {
        runtime ":hibernate:$grailsVersion"
        runtime ":jquery:1.7.1"
        runtime ":resources:1.1.5"

        build ":tomcat:$grailsVersion"
    }
}
