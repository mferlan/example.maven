#Run stand-alone OSGI server
1. Download org.eclipse.osgi </br>
<http://dist.wso2.org/maven2/org/eclipse/osgi/org.eclipse.osgi/3.4.2.R34x_v20080826-1230/org.eclipse.osgi-3.4.2.R34x_v20080826-1230.jar> </br>
We are downloading this specific version of equinox framework, because it contains embedded console. 
Later versions are are built without it. Console was extracted to interface *org.eclipse.equinox.console* while implementation is apache-felix-gogo  consisting out of 3 bundles. For simplicity, because we are manually installing jars, we will use this old version Equinox.
2. Rename downloaded jar to org.eclipse.osgi.jar and copy it to a new place, e.g., c:\temp\osgi-server. Start your OSGi server via the following command.
3. Open command like and </br>
`cd c:/temp/osgi-server` </br>
 Run stand-alone OSGI server</br>
`java -jar org.eclipse.osgi.jar -console`

 
#The OSGi console
The OSGi console is like a command-line shell. In this console you can type a command to perform an OSGi action. This can be useful to analyze problems on the OSGi layer of your application.

Use, for example, the command `ss` to get an overview of all bundles, their status and bundle-id.

 Example console output:
	
		id      State       Bundle
		0       ACTIVE      org.eclipse.osgi_3.4.2.R34x_v20080826-1230


 - `help`   Lists the available commands.
 
#Install sample bundles to OSGI server
4. Build sample project using </br>
`mvn clean package`
5. Copy sample *sample.consumer-1.0.0.jar* to *C:/temp/osgi-server/bundles* </br>
   Copy sample *sample.service-1.0.0.jar* to *C:/temp//osgi-server/bundles* </br></br>
   Download dependency commons-lang3 </br>
	<http://central.maven.org/maven2/org/apache/commons/commons-lang3/3.4/commons-lang3-3.4.jar> </br>
   	and place it to C:\temp\osgi-server\bundles
6. You can use "install URL" to install a bundle from a certain URL. For example to install your bundle from "c:\temp\osgi-server\bundles" use:</br>
	`install file:bundles\commons-lang3-3.4.jar`</br>
	`install file:bundles\sample.service-1.0.0.jar`</br>
	`install file:bundles\sample.consumer-1.0.0.jar`</br>
You can start then the bundle with start and the id. </br>
**TIP:**You can remove all installed bundles with the -clean parameter.
 
# Other commands
  
 - Lists the installed bundles and their status.

			ss		
 Example console output:

			id      State       Bundle
			0       ACTIVE      org.eclipse.osgi_3.4.2.R34x_v20080826-1230
			1       INSTALLED   org.apache.commons.lang3_3.4.0
			2       INSTALLED   sample.service_1.0.0
			3       INSTALLED   sample.consumer_1.0.0

 - Lists bundles and their status that have sample within their name.
 
			ss sample
Example console output:

			id      State       Bundle
			2       INSTALLED   sample.service_1.0.0
			3       INSTALLED   sample.consumer_1.0.0

 
 - Starts the bundle with the <bundle-id> ID.  (`start <bundle-id>`)
 
			start 3
 			
 This should automatically resolved dependent bundles.

			id      State       Bundle
			0       ACTIVE      org.eclipse.osgi_3.4.2.R34x_v20080826-1230
			1       RESOLVED    org.apache.commons.lang3_3.4.0
			2       RESOLVED    sample.service_1.0.0
			3       ACTIVE      sample.consumer_1.0.0			

- Shows information about the bundle with the <bundle-id> ID, including the registered and used services. (`bundle <bundle_id>`) 

			bundle 3
			
- Information about package

			packages de.atron.test.sample
	
Example output

			osgi> packages de.atron.test.sample
			de.atron.test.sample; version="1.0.0"<file:bundles/sample.service-1.0.0.jar [2]>
			
			  file:bundles/sample.consumer-1.0.0.jar [3] imports


# osgi-server 
All jars are being pushed to git repository in folder *osgi-server*. So  all examples can be tested in that folder

# equinox.applicaton
Equinox OSGI server built using Tycho maven plugin to export product definition containing only equinox core bundles.
Product is plugin based, so that we can update it and add sample.consumer as dependencies.
Edit *equinox.application.product* using notepad and in plugins tab add

	<plugins>
      <plugin id="sample.consumer"/>
		...
   			   
Also, update pom.xml to add dependency to sample.consumer

	<dependency>
		<groupId>de.atron.testing</groupId>
		<artifactId>sample.consumer</artifactId>
		<version>1.0.0</version>
	</dependency>
	
Install sample project

	 mvn -f sample/pom.xml clean install
	 
Build equinox.application

	mvn -f equinox.application/pom.xml clean verify
	
Under equinox.application/target/products/.../ find eclipse.exe and execute it.
Try console commands to analyze system.