# OSGI example

The purpose of git repository is to demonstrate how to start working with OSGI.
This repository contains a number of branches that show the different stages of evolution in designing such an application.

---
  
##Branches
###00-initial
The `00-initial branch`  contains "classic" maven project separated into modules:
 - sample.service
 - sample.consumer
 
 
 Module *sample.service* contains interface and implementation consumed by class defined in module *sample.consumer*.

 Module *sample.consumer* also contains defined unit tests for consumer class where referenced service mocked.
 
Projects have standard maven structure:
 + src
   * main
     - java
     - resources
   * test
     - java
     - resources


###01-add-osgi-metadata
The `01-add-osgi-metadata` branch  contains "classic" maven projects, described in 00-initial branch, enriched with OSGI metadata.

OSGi metadata is simply a number of extra headers in the Jar's META-INF/MANIFEST.MF file. While quite a large number of OSGi headers exist, the following would typically be the ones that you'd find in a library Jar file that provides OSGi metadata.
	
	Bundle-ManifestVersion: 2
	Bundle-SymbolicName: de.atron.test.sample.consumer
	Bundle-Version: 1.0.0
	Export-Package: de.atron.test.sample.consumer;version="1.0.0"
	Import-Package: de.atron.test.sample.service;version="[1.0, 2)"

 According to OSGI Alliance, the manifest is a readable format but it was never intended to be human writable except for emergencies. This is something that OSGI Alliance approach differs from Eclipse community approach where they value MANIFEST-first development (MANIFEST.MF is maintained with source code, with help of UI editors).

For maintaining MANIFEST.MF we will use maven plugin

	<plugin>
		<groupId>biz.aQute.bnd</groupId>
		<artifactId>bnd-maven-plugin</artifactId>
		<version>3.3.0</version>
	</plugin>

To attach OSGI metadata to our bundles we need to add following plugin configuration to our projects:

	<plugin>
		<groupId>biz.aQute.bnd</groupId>
		<artifactId>bnd-maven-plugin</artifactId>
		<version>3.3.0</version>
		<executions>
			<execution>
				<goals>
					<goal>bnd-process</goal>
				</goals>
			</execution>
		</executions>
	</plugin>
	<plugin>
		<groupId>org.apache.maven.plugins</groupId>
		<artifactId>maven-jar-plugin</artifactId>
		<configuration>
			<useDefaultManifestFile>true</useDefaultManifestFile>
		</configuration>
	</plugin>

Let's take a look at our modules

#### sample.service

Module *sample.service* defines interface `de.atron.test.sample.MockedInterface` and its implementation `de.atron.test.sample.impl.MockedService`. Implementation and interface are separated into each own packages so this makes things simpler. 
This module's API is:
	- package `de.atron.test.sample`

By default, *bnd-maven-plugin* does not export any package.
We need to export this package so that other bundles can consume it, and we do it by adding file to root named **bnd.bnd** containing single line:

	Export-Package: de.atron.test.sample
	
This results with following generated MANIFEST.MF

	Manifest-Version: 1.0
	Bundle-ManifestVersion: 2
	Bundle-Name: sample.service
	Bundle-SymbolicName: sample.service
	Bundle-Version: 0.1.0.201610241941
	Import-Package: de.atron.test.sample
	Require-Capability: osgi.ee;filter:="(&(osgi.ee=JavaSE)(version=1.7))"
	Export-Package: de.atron.test.sample;version="0.1.0"
	Private-Package: de.atron.test.sample.impl
	Archiver-Version: Plexus Archiver
	Built-By: ferlan
	Tool: Bnd-3.3.0.201609221906
	Bnd-LastModified: 1477338070195
	Created-By: 1.8.0_51 (Oracle Corporation)
	Build-Jdk: 1.8.0_51
	
#### sample.consumer

Module *sample.consumer* defines single class referencing `de.atron.test.sample.MockedInterface`.
This module does not have any public API and *bnd-maven-plugin* will automatically calculate required dependencies. Our module has runtime dependencies to following maven artifact
 - sample.service
 - commons-lang3
 
 Test scope dependencies are not taken into consideration when generating manifest.mf 
 
This results with following generated MANIFEST.MF
 
	Manifest-Version: 1.0
	Bundle-ManifestVersion: 2
	Bundle-Name: sample.consumer
	Bundle-SymbolicName: sample.consumer
	Bundle-Version: 0.1.0.201610241941
	Require-Capability: osgi.ee;filter:="(&(osgi.ee=JavaSE)(version=1.7))"
	Import-Package: de.atron.test.sample;version="[0.1,1)",org.apache.comm
	 ons.lang3;version="[3.4,4)"
	Private-Package: de.atron.test.sample.consumer
	Tool: Bnd-3.3.0.201609221906
	Archiver-Version: Plexus Archiver
	Built-By: ferlan
	Bnd-LastModified: 1477338073745
	Created-By: 1.8.0_51 (Oracle Corporation)
	Build-Jdk: 1.8.0_51

	
More information about this plugin and how to use it can be read on  <http://njbartlett.name/2015/03/27/announcing-bnd-maven-plugin.html>. This plugin is based on *bnd* tool( <http://bnd.bndtools.org/> ).

###02-run-osgi-ee
The `02-run-osgi-ee`  will demonstrate how to build our `sample`  project and run it into standalone osgi server (Equinox)


1. Download org.eclipse.osgi </br>
<http://dist.wso2.org/maven2/org/eclipse/osgi/org.eclipse.osgi/3.4.2.R34x_v20080826-1230/org.eclipse.osgi-3.4.2.R34x_v20080826-1230.jar> </br>
We are downloading this specific version of equinox framework, because it contains embedded console. 
Later versions are are built without it. Console was extracted to interface *org.eclipse.equinox.console* while implementation is apache-felix-gogo  consisting out of 3 bundles. For simplicity, because we are manually installing jars, we will use this old version Equinox.
2. Rename downloaded jar to org.eclipse.osgi.jar and copy it to a new place, e.g., c:\temp\osgi-server. Start your OSGi server via the following command.
3. Open command like and </br>
`cd c:/temp/osgi-server` </br>
 Run stand-alone OSGI server</br>
`java -jar org.eclipse.osgi.jar -console`

 
####The OSGi console
The OSGi console is like a command-line shell. In this console you can type a command to perform an OSGi action. This can be useful to analyze problems on the OSGi layer of your application.

Use, for example, the command `ss` to get an overview of all bundles, their status and bundle-id.

 Example console output:
	
		id      State       Bundle
		0       ACTIVE      org.eclipse.osgi_3.4.2.R34x_v20080826-1230


 - `help`   Lists the available commands.
 
#####Install sample bundles to OSGI server
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
 
##### Other commands
  
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


#### osgi-server 
All jars are being pushed to git repository in folder *osgi-server*. So  all examples can be tested in that folder

###03-osgi-services
The `03-osgi-services`  demonstrates how to register and consume osgi service. For now we only have interface and class that references that interface, but no defined dependency injection.

Osgi services are collaboration model between OSGI modules. Services can be declared
 - programatically using OSG API
 - Declarative Service
 - Spring DM
 - Blueprint

For our use case, the simplest variant is using declarative services.
The OSGi declarative services (DS) functionality allows you to define and consume services via metadata (XML) without any dependency in your source code to the OSGi framework.
*bnd-maven-plugin* supports the use of annotations in your source code to generate this meta-data at build time automatically.

The OSGi service component is responsible for starting the service (service component). Service components consist of an XML description (component description) and an object (component instance). The component description contains all information about the service component, e.g., the class name of the component instance and the service interface it provides. Plug-ins typically define component descriptions in a directory called OSGI-INF.

A reference to the component description file is entered in the MANIFEST.MF file via the Service-Component property. If the OSGi runtime finds such a reference, the org.eclipse.equinox.ds plug-in creates the corresponding service.

For us, as we are going to use annotation to define components this is not so important.
To use annotations both *sample.service* and *sample.consumer* module must declared dependency to org.eclipse.osgi.services that contains osgi annotations (package org.osgi.service.component.annotations).

		<dependency>
			<groupId>org.eclipse.osgi</groupId>
			<artifactId>org.eclipse.osgi.services</artifactId>
		</dependency>
		
#### Define a service
Annotate implementation class with annotation @Component  and that is it. 

	@Component(name="mocked-service")
	public class MockedService implements MockedInterface {
	
	    @Override
	    public double divide(double dividend, double divisor) {
	        return dividend / divisor;
	    }
	}

Maven plugin will generate component descriptor inside OSGI-INF called **mocked-service.xml**

	<?xml version="1.0" encoding="UTF-8"?>
	<component name="mocked-service">
	  <implementation class="de.atron.test.sample.impl.MockedService"/>
	  <service>
	    <provide interface="de.atron.test.sample.MockedInterface"/>
	  </service>
	</component>

Manifest will be updated and Service-Component added:

	Service-Component: OSGI-INF/mocked-service.xml

####Define a consumer of a service reference
Annotate MockedInterface consumer with @Component. And setter for MockedInterface with
@Reference

	@Component
	public class TestedService {
		
	    private MockedInterface mocked;
	    
	    @Reference
	    public void setMocked(MockedInterface mocked) {
			this.mocked = mocked;
		}
		...
	}
 
Maven plugin will generate component descriptor inside OSGI-INF called **de.atron.test.sample.consumer.TestedService.xml** (default name, as no specific name was used for component)
	
	<?xml version="1.0" encoding="UTF-8"?>
	<scr:component xmlns:scr="http://www.osgi.org/xmlns/scr/v1.1.0" name="de.atron.test.sample.consumer.TestedService">
	  <implementation class="de.atron.test.sample.consumer.TestedService"/>
	  <reference name="Mocked" interface="de.atron.test.sample.MockedInterface" bind="setMocked"/>
	</scr:component>
	

Manifest will be updated and Service-Component added:

	Service-Component: OSGI-INF/de.atron.test.sample.consumer.TestedService.xml



####Required bundles
To use declarative services the following plug-ins must be available at runtime.
- org.eclipse.equinox.util - contains utility classes
 - org.eclipse.equinox.ds - is responsible for reading the component metadata and for creating and registering the services based this information
 - org.eclipse.osgi.services - service functionality used by declarative services
 
**If you use OSGi DS services outside Eclipse RCP applications, you need to ensure that the org.eclipse.equinox.ds plug-ins is started before any application plug-in which wants to consume a service.**

##### Download Declarative Services dependencies
Download following bundles
 - eclipse.osgi.services 
<http://dist.wso2.org/maven2/org/eclipse/osgi/org.eclipse.osgi.services/3.2.0.v20090306-1900/org.eclipse.osgi.services-3.2.0.v20090306-1900.jar>
 - equinox declarative service
<http://dist.wso2.org/maven2/org/eclipse/equinox/org.eclipse.equinox.ds/1.1.0.v20090520-1800/org.eclipse.equinox.ds-1.1.0.v20090520-1800.jar>
- equinox utils (required by ds)
<http://dist.wso2.org/maven2/org/eclipse/equinox/org.eclipse.equinox.util/1.0.100.v20090520-1800/org.eclipse.equinox.util-1.0.100.v20090520-1800.jar>

Specified versions of dependencies are used due to compatibility with org.eclipse.osgi
Mentioned bundles have already been downloaded and pushed to osgi-server/bundles.

####Run OSGI server
Use existing setup in folder *osgi-server*
delete configuration directory if exists
execute `run-osgi-server.bat` to start server

	setibsl 4
	
	install file:bundles\sample.service-1.0.0.jar
	
	install file:bundles\sample.consumer-1.0.0.jar
	
	install file:bundles\commons-lang3-3.4.jar
	
	install file:bundles\org.eclipse.osgi.services.jar
	
	install file:bundles\org.eclipse.equinox.util.jar
	
	install file:bundles\org.eclipse.equinox.ds.jar
	
	setbsl 2 file:bundles\org.eclipse.equinox.ds.jar
	
	start file:bundles\org.eclipse.equinox.ds.jar
	
	start file:bundles\sample.service-1.0.0.jar
	
	start file:bundles\sample.consumer-1.0.0.jar
	
	list


 Search for registered service

			services (objectClass=de.atron.test.sample.MockedInterface)	
			
####Service Component Runtime Commands

 - `list/ls [-c] [bundle id]` - Lists all components; add -c to display the complete info for each component;use *[bundle id]* to list the components of the specified bundle
 - `component/comp <component id>` - Prints all available information about t
he specified component;*<component id>* - The ID of the component as displayed by
 the list command
 - `enable/en <component id>` - Enables the specified component;*<component id>* - The ID of the component as displayed by
 the list command
 - `disable/dis <component id>` - Disables the specified component; *<component id>* - The ID of the component as displayed by
 the list command
 - `enableAll/enAll [bundle id]` - Enables all components; use *[bundle id]* to
 enable all components of the specified bundle  
- `disableAll/disAll [bundle id]` - Disables all components; use *[bundle id]* to disable all components of the specified bundle


###04-launch-product
The `04-launch-product`  demonstrates how to use tools to create executable. Running osgi server and installing bundles with scripts is cumbersome (especially maintaining them).
Although, `bnd` toolchain uses different approach in defining runtime environment, we will use PDE approach and its [product configuration](http://help.eclipse.org/mars/index.jsp?topic=%2Forg.eclipse.pde.doc.user%2Fguide%2Ftools%2Feditors%2Fproduct_editor%2Fconfiguration.htm).

Equinox OSGI server built using Tycho maven plugin to export product definition containing only equinox core bundles. Product is plugin based, so that we can update it and add sample.consumer as dependencies. Edit equinox.application.product using notepad and in plugins tab add

	<plugins>
	  <plugin id="sample.consumer"/>
	    ...

Also, configure start level

	<configurations>
      <plugin id="sample.service" autoStart="true" startLevel="4" />
      <plugin id="sample.consumer" autoStart="true" startLevel="4" />
	...

For maven build to run successfully and resolve dependencies we need to update pom.xml to add dependency to sample.consumer

	<dependencies>
		<dependency>
			<groupId>de.atron.testing</groupId>
			<artifactId>sample.consumer</artifactId>
			<version>1.0.0</version>
		</dependency>
	</dependencies>

Transitive dependencies will be included.

####POM dependencies consider
OSGi bundles from Maven repositories can be added to the target platform in the following way:
 - Specify a dependency to the OSGi bundle artifact in the POM's <dependencies> section.
 - Set the configuration parameter `pomDependencies=consider` on the target-platform-configuration plugin

This configuration has the following effect:
 - First, Maven resolves the GAV dependencies according to the normal Maven rules. This results in a list of artifacts consisting of the specified artifacts and their transitive Maven dependencies.
 - Tycho then checks each of these artifacts, and if the artifact is an OSGi bundle, it is added to the target platform. Other artifacts are ignored. OSGi bundles which become part of the target platform in this way are then available to resolve the project's OSGi dependencies.
	
Install sample project

	mvn -f sample/pom.xml clean install
Build equinox.application

	mvn -f equinox.application/pom.xml clean verify
Under equinox.application/target/products/.../ find eclipse.exe and execute it. Try console commands to analyze system.

###05-console-command-provider
The `05-console-command-provider` demonstrates how to provide your own commands to equinox console.

Osgi Framework defines possibility to extend console commands by registering OSGI service that implements CommandProvider interface.

The interface contains only methods for the help. The console should use inspection to find the commands. All public commands, starting with a '_' and taking a CommandInterpreter as parameter will be found. E.g.

	 public Object _hello( CommandInterpreter intp ) {
	 	return "hello " + intp.nextArgument();
	 }
 
So we defined class in module *sample.service*

	@Component
	public class MockedServiceCommandProvider implements CommandProvider {
	
		private MockedInterface mockedService;
	
		@Reference(service = MockedInterface.class)
		public void setMockedService(MockedInterface mockedService) {
			this.mockedService = mockedService;
		}
	
		@Override
		public String getHelp() {
			StringBuilder builder = new StringBuilder();
			builder.append("Mocked Interface service commands");
			builder.append("\n");
			builder.append("divide - Enter two values, divident and divisor, and you will get the result of division");
			builder.append("\n");
			return builder.toString();
		}
	
		public Object _divide(CommandInterpreter intp) {
			String dividendStr = intp.nextArgument();
			if (dividendStr == null) {
				intp.println("Please enter two values, divident and divisor");
				return null;
			}
	
			String divisorStr = intp.nextArgument();
			if (divisorStr == null) {
				intp.println("Please enter two values, divident and divisor");
				return null;
			}
	
			double dividend = Double.parseDouble(dividendStr);
			double divisor = Double.parseDouble(divisorStr);
			double result = mockedService.divide(dividend, divisor);
			intp.println("Result of division is: " + result);
			return result;
		}
	
	}
	
#### Test command interpreter

Install sample project

	mvn -f sample/pom.xml clean install
Build equinox.application

	mvn -f equinox.application/pom.xml clean verify
Under equinox.application/target/products/.../ find eclipse.exe and execute it. Try console commands to analyze system.

Execute console command
	
	divide 15 4
	

---

This is now second part of tutorial that will focus how to develop MANIFEST first approach OSGI bundles.

---
###06-manifest-first
The `06-manifest-first`  demonstrates how to create MANIFEST first approach OSGI bundles.

Differences:
 - adds Plugin Nature
 - classic java structure <br>
 		+ src<br>
 		+ bin
 - test classes are separated into special project - fragment.
 - Manifest must be maintained manually with nice editor support
 - Declarative service XML must be maintained manually.  (eclipse 4.7 supports annotations in DS)

- target platform - pool of bundles and features. similar as maven repository.
	Defined target based on running installation of eclipse with addition of 3rd party dependencies downloaded using maven copy-dependencies goal.

- product based on equinox.application just added newly created bundles with their dependencies


#testing.sample.releng
MANIFEST-first project using Junit, mockito, assertj,commons-lang3 libraries.
SampleTest has been splitted into 2 main projects
 - testing.sample  - main project with implementation
 - testing.sample.test - fragment containing test classes
 
 testing.sample.releng itself is:
    - parent pom for all projects
    - multimodule project listing all modules
    - tycho configuration project - enables and configures tycho plugin
    - releng means Release engineering and not sure if it is correct name, as it has bigger role
    - projects are organized hierarchical, but for this case flat structure would be better
    
There are additional projects
- org.apache.commons.lang3 - osgification of commons-lang3 available as project. commons-lang3.jar downloaded thru maven but under source version control
   It is enabled with profile "include-dependencies". Alternative to this was to get dependency thru maven repository directly (profile "pom-dependencie") but 
   has couple of limitations e.g. violation DRY principle  (dependency in pom.xml and MANIFEST.MF) and problem referencing in development (PDE development)
- testing.sample.target - project listing target definition files for projects
        - testing.sample.target  - definition file specifies p2 software site and directory for third-party dependencies. Enabled with profile "pom-dependencies-with-target"
                Idea is that dependencies for build are declared in parent pom.xml and for development as a directory in testing.sample.target.
                But this still violates DRY principle. As is problematic when library is not OSGI compliant.
        - testing.sample2.target - definition file works only with p2 software sites. Site for third party libraries is created and mainteined with project testing.sample.p2.repository
- testing.sample.p2.repository - is a maven project with single goal of listing all third-party libraries without official p2 repository.
        Project uses p2-maven-plugin (https://github.com/reficio/p2-maven-plugin)  to OSGI-fy dependencies and create p2 site out of them.
        The only thing that is left is to publish it to some server and use it in testing.sample2.target
        
        
  TODO:
  testing.sample.releng contains profiles for different attempts to cope with problem of referencing third party dependencies in MANIFEST-first approach of programming OSGI bundles.
  Better approach would be to use different branches for different scenarios
  

#Couple of links:
  
<http://www.vogella.com/tutorials/EclipseTycho/article.html>

<http://stackoverflow.com/questions/20842256/how-to-manage-tycho-eap-versionning-correctly>

<https://git.eclipse.org/c/orbit/orbit-recipes.git/tree/README.md>

<https://github.com/eclipse/ebr>

<http://njbartlett.name/2015/03/27/announcing-bnd-maven-plugin.html>

<http://bnd.bndtools.org/>

<http://www.reficio.org/2013/12/02/eclipse-rcp-dependency-management-done-right-with-p2-maven-plugin/>
    



