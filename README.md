#equinox.applicaton

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

##POM dependencies consider
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

