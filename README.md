# Add OSGI metadata

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

## sample.service

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
	
## sample.consumer

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