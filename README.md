#OSGI services

OSGI services are collaboration model between OSGI modules. Services can be declared
 - programatically using OSGI API
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
		
### Define a service
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

##Define a consumer of a service reference
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



##Required bundles
To use declarative services the following plug-ins must be available at runtime.
- org.eclipse.equinox.util - contains utility classes
 - org.eclipse.equinox.ds - is responsible for reading the component metadata and for creating and registering the services based this information
 - org.eclipse.osgi.services - service functionality used by declarative services
 
**If you use OSGi DS services outside Eclipse RCP applications, you need to ensure that the org.eclipse.equinox.ds plug-ins is started before any application plug-in which wants to consume a service.**

### Download Declarative Services dependencies
Download following bundles
 - eclipse.osgi.services 
<http://dist.wso2.org/maven2/org/eclipse/osgi/org.eclipse.osgi.services/3.2.0.v20090306-1900/org.eclipse.osgi.services-3.2.0.v20090306-1900.jar>
 - equinox declarative service
<http://dist.wso2.org/maven2/org/eclipse/equinox/org.eclipse.equinox.ds/1.1.0.v20090520-1800/org.eclipse.equinox.ds-1.1.0.v20090520-1800.jar>
- equinox utils (required by ds)
<http://dist.wso2.org/maven2/org/eclipse/equinox/org.eclipse.equinox.util/1.0.100.v20090520-1800/org.eclipse.equinox.util-1.0.100.v20090520-1800.jar>

Specified versions of dependencies are used due to compatibility with org.eclipse.osgi
Mentioned bundles have already been downloaded and pushed to osgi-server/bundles.

##Run OSGI server
Use existing setup in folder *osgi-server*
delete configuration directory if exists
execute `run-osgi-server.bat` to start server

	setibsl 4
	
	install file:bundles/sample.service-1.0.0.jar
	
	install file:bundles/sample.consumer-1.0.0.jar
	
	install file:bundles/commons-lang3-3.4.jar
	
	install file:bundles/org.eclipse.osgi.services.jar
	
	install file:bundles/org.eclipse.equinox.util.jar
	
	install file:bundles/org.eclipse.equinox.ds.jar
	
	setbsl 2 file:bundles/org.eclipse.equinox.ds.jar
	
	start file:bundles/org.eclipse.equinox.ds.jar
	
	start file:bundles/sample.service-1.0.0.jar
	
	start file:bundles/sample.consumer-1.0.0.jar
	
	list


 Search for registered service

			services (objectClass=de.atron.test.sample.MockedInterface)	
			
#Service Component Runtime Commands

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

