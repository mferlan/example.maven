
#Run stand-alone OSGI server
1. Download org.eclipse.osgi </br>
 `http://dist.wso2.org/maven2/org/eclipse/osgi/org.eclipse.osgi/3.4.2.R34x_v20080826-1230/org.eclipse.osgi-3.4.2.R34x_v20080826-1230.jar`
2. Rename downloaded jar to org.eclipse.osgi.jar and copy it to a new place, e.g., c:\temp\osgi-server. Start your OSGi server via the following command.
3. Open command like and </br>
`cd c:/temp/osgi-server` </br>
 Run stand-alone OSGI server</br>
`java -jar org.eclipse.osgi.jar -console`

#Install sample bundles to OSGI server
4. Build sample project using </br>
`mvn clean package`
5. Copy sample *sample.consumer-1.0.0.jar* to *C:/temp/osgi-server/bundles* </br>
   Copy sample *sample.service-1.0.0.jar* to *C:/temp//osgi-server/bundles* </br></br>
   Download dependency commons-lang3 </br>
	`http://central.maven.org/maven2/org/apache/commons/commons-lang3/3.4/commons-lang3-3.4.jar` </br>
   	and place it to C:\temp\osgi-server\bundles
6. You can use "install URL" to install a bundle from a certain URL. For example to install your bundle from "c:\temp\osgi-server\bundles" use:
	`install file:bundles\sample.service-1.0.0.jar`</br>
	`install file:bundles\sample.consumer-1.0.0.jar`</br>
	`install file:bundles\commons-lang3-3.4.jar`</br>
You can start then the bundle with start and the id. </br>
**TIP:**You can remove all installed bundles with the -clean parameter.
 
# Inspect osgi environement

Execute some common console commands

 - List all bundle
 		
			ss

 - Start desired bundles

			start <bundle_id>
	
 - Information about package

			packages de.atron.test.sample
	

# OSGI services

Download following bundles
 
 - osgi.services 
	
			http://dist.wso2.org/maven2/org/eclipse/osgi/org.eclipse.osgi.services/3.2.0.v20090306-1900/org.eclipse.osgi.services-3.2.0.v20090306-1900.jar
 
 - osgi declarative service (+util)
		
			http://dist.wso2.org/maven2/org/eclipse/equinox/org.eclipse.equinox.ds/1.1.0.v20090520-1800/org.eclipse.equinox.ds-1.1.0.v20090520-1800.jar
	
			http://dist.wso2.org/maven2/org/eclipse/equinox/org.eclipse.equinox.util/1.0.100.v20090520-1800/org.eclipse.equinox.util-1.0.100.v20090520-1800.jar
	
Copy them inside bundles folder, rename them and strip version and install them and start 'org.eclipse.equinox.ds'
	
	install file:bundles\org.eclipse.osgi.services.jar
	install file:bundles\org.eclipse.equinox.util.jar
	install file:bundles\org.eclipse.equinox.ds.jar
	
Execute console commands to diagnose components
 - List all commands

			help
 - List all component with command
 
			list
 - Inspect component using

			component <component_id>
 - Search for registered service

			services (objectClass=de.atron.test.sample.MockedInterface)	
 - Execute divide command

			divide 15 3