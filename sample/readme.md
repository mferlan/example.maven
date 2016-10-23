
#Install sample jars in Equinox
1. Download org.eclipse.osgi

	http://dist.wso2.org/maven2/org/eclipse/osgi/org.eclipse.osgi/3.4.2.R34x_v20080826-1230/org.eclipse.osgi-3.4.2.R34x_v20080826-1230.jar

2. Rename downloaded jar to org.eclipse.osgi.jar and copy it to a new place, e.g., c:\temp\osgi-server. Start your OSGi server via the following command.
3. Open command like and 
	
	cd c:/temp/osgi-server

 Run stand-alone OSGI server

	java -jar org.eclipse.osgi.jar -console

4. Build sample project using 
	
	mvn clean package

5. Copy sample 'sample.consumer-1.0.0.jar' to C:/temp/osgi-server/bundles
   Copy sample 'sample.service-1.0.0.jar' to sample/osgi-server/bundles
   Download dependecy commons-lang3
   
	http://central.maven.org/maven2/org/apache/commons/commons-lang3/3.4/commons-lang3-3.4.jar
   	
   	and place it to C:\temp\osgi-server\bundles

6. You can use "install URL" to install a bundle from a certain URL. For example to install your bundle from "c:\temp\osgi-server\bundles" use:

	install file:bundles\sample.service-1.0.0.jar
	
	install file:bundles\sample.consumer-1.0.0.jar
	
	install file:bundles\commons-lang3-3.4.jar
You can start then the bundle with start and the id.

firstbundle 1
firstbundle 2
firstbundle 3
TIP:You can remove all installed bundles with the -clean parameter.

List all bundles
	
	ss

Start desired bundles

	start <bundle_id>
	
Information about package

	packages de.atron.test.sample

7. download: 
 - osgi.services 
	
	http://dist.wso2.org/maven2/org/eclipse/osgi/org.eclipse.osgi.services/3.2.0.v20090306-1900/org.eclipse.osgi.services-3.2.0.v20090306-1900.jar
 - osgi declarative service (+util)
		
	http://dist.wso2.org/maven2/org/eclipse/equinox/org.eclipse.equinox.ds/1.1.0.v20090520-1800/org.eclipse.equinox.ds-1.1.0.v20090520-1800.jar
	
	http://dist.wso2.org/maven2/org/eclipse/equinox/org.eclipse.equinox.util/1.0.100.v20090520-1800/org.eclipse.equinox.util-1.0.100.v20090520-1800.jar
	
8. copy them inside bundles folder, rename them and strip version and install them and start 'org.eclipse.equinox.ds'
	
	install file:bundles\org.eclipse.osgi.services.jar
	install file:bundles\org.eclipse.equinox.util.jar
	install file:bundles\org.eclipse.equinox.ds.jar
	
9. List all component with command
 
	list
10. Inspect component using

	component <component_id>

11. Search for registered service

	services (objectClass=de.atron.test.sample.MockedInterface)