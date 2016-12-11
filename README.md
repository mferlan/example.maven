#Hybrid approach

- pure maven build
- PDE development

To achieve this, MANIFEST and DS xml must be generated automatically.
We are gonna use **maven-resources-plugin** to copy generated files to locations recognisable by PDE.

			<plugin>
				<artifactId>maven-resources-plugin</artifactId>
				<version>3.0.1</version>
				<executions>
					<execution>
						<id>copy-manifests</id>
						<!-- here the phase you need -->
						<phase>process-classes</phase>
						<goals>
							<goal>copy-resources</goal>
						</goals>
						<configuration>
							<outputDirectory>${basedir}/META-INF</outputDirectory>
							<resources>
								<resource>
									<directory>${basedir}/target/classes/META-INF</directory>
									<filtering>false</filtering>
								</resource>
							</resources>
						</configuration>
					</execution>
					<execution>
						<id>copy-ds</id>
						<!-- here the phase you need -->
						<phase>process-classes</phase>
						<goals>
							<goal>copy-resources</goal>
						</goals>
						<configuration>
							<outputDirectory>${basedir}/OSGI-INF</outputDirectory>
							<resources>
								<resource>
									<directory>${basedir}/target/classes/OSGI-INF</directory>
									<filtering>false</filtering>
								</resource>
							</resources>
						</configuration>
					</execution>
				</executions>
			</plugin>

That's it.
Now we can use our projects with PDE tooling.
 - Open and view MANIFEST with supported editors
 - create launch configurations
 - easily debug code in OSGI environment
 
Problems:
 how to reference third party dependencies
 
 **we need a target platform** 
   
Differences between maven project and PDE project:
 - adds Plugin Nature
 - classic java structure <br>
 		+ src<br>
 		+ bin
 - test classes are separated into special project - fragment.
 - Manifest must be maintained manually with nice editor support
 - Declarative service XML must be maintained manually.  (eclipse 4.7 supports annotations in DS)

- target platform - pool of bundles and features. similar as maven repository.
	Defined target based on running installation of eclipse with addition of 3rd party dependencies downloaded using maven copy-dependencies goal.

 

## set up target platform

The first thing you should do when setting up your workspace to develop manifest-first projects is to set up target platform.

**What is target platform?** 
The set of plug-ins which you can use for your development or your build process to resolve the project dependencies is defined by the plug-ins in your workspace in addition with the plug-ins defined by your target platform. By default running installation of eclipse is set as target platform.
A target definition file allows us to define target platform and is typically shared between the developers to ensure that everyone is using the same basis for development.


Our project dependencies consists of
 - workspace projects
 	+ de.atron.testing.sample.service
 	+ de.atron.testing.sample.consumer
 	+ de.atron.testing.sample.consumer.test
 - equinox core dependencies
 - third party dependencies
 	+ commons-lang3 version 3.4
 	+ junit
 	+ mockito
 	+ assertj
 	and their transitive dependencies
 	
Default target platform ( running eclipse jars ) is not good enough. It will provide us equinox core dependencies, but what about commons-lang3 and test dependencies.

Tutorial how to create a target definition using Eclipse IDE and apply it to your workspace is described in 
<http://www.vogella.com/tutorials/EclipseTargetPlatform/article.html>.
 It is good practice to develop and build against a specific target definition. This way it can be ensured that dependencies and their versions doesn’t change during the development. 
 Eclipse allows us to define target definition based on directories. So the base for our target definition we will use our installation
 
### How to include third party dependencies

We will use `maven-dependency-plugin` and its goal `copy-dependencies`. All dependencies from sample.parent except equinox related are copied to new project `de.atron.testing.sample.dependencies`.

Run `generate-sources` goal on  project `de.atron.testing.sample.dependencies` to prepare data.
Open  `de.atron.testing.sample.target/tycho.sample.target` in Eclipse IDE and click on `Set as target platform`.


 