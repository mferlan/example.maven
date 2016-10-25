#Convert projects to plug-ins (MANIFEST first approach)

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

- product based on equinox.application just added newly created bundles with their dependencies
 

## set up target platform


The set of plug-ins which you can use for your development or your build process to resolve the project dependencies is defined by the plug-ins in your workspace in addition with the plug-ins defined by your target platform. By default running running installation of eclipse is set as target platform.
A target definition file allows us to define target platform and is typically shared between the developers to ensure that everyone is using the same basis for development.


### How to get started with target platform
<http://www.vogella.com/tutorials/EclipseTargetPlatform/article.html>

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
 	

 It is good practice to develop and build against a specific target definition. This way it can be ensured that dependencies and their versions doesn’t change during the development. 
 Eclipse allows us to define target definition based on directories. So the base for our target definition we will use our installation
 
### How to include third party dependencies

We will use `maven-dependency-plugin` and its goal `copy-dependencies`. All dependencies from sample.parent except equinox related are copied to new project `de.atron.testing.sample.dependencies`.

Run `generate-sources` goal on  project `de.atron.testing.sample.dependencies` to prepare data.
Open  `de.atron.testing.sample.target/tycho.sample.target` in Eclipse IDE and click on `Set as target platform`.


## How to create projects

### How to convert sample.service
Now that our target platform is configured, create new projects using Eclipse IDE:

	`File -> New -> Plug-in Project`
	
 - enter project name e.g. de.atron.testing.sample.service and leave other fields as default -> Next
 - Uncheck `This plugin will make contributions to UI`
 - do not create Activator
 - Finish
 

Copy content of a `sample/sample.service/src/main/java` to `de.atron.testing.sample.service/src`.
Open META-INF/MANIFEST.MF in Eclipse IDE and under runtime export only `de.atron.test.sample` package.
For simlicity sake, we will 
	
	Require-Bundle: org.eclipse.osgi;bundle-version="3.10.100", 
	 org.eclipse.osgi.services;bundle-version="3.5.0"

### How to convert sample.consumer
Test classes are not kept in bundle. For test classes special project called fragment is created. Fragment extends bundle's classpath. It uses same classloader as host bundle, therefore it has access to all its internal implementations that are subject to tests.

For creating host bundle from service.consumer project we will follow the same procedure as with de.atron.testing.sample.service, just use appropriate name: de.atron.testing.sample.service

And instead of exporting `de.atron.test.sample` package we will import it using dependencies tab in Manifest Editor.
Asside from de.atron.test.sample package we need to import `org.apache.commons.lang3 package`.


For creating [test fragment](http://www.vogella.com/tutorials/EclipseFragmentProject/article.html) we will open

	`File -> New -> Other -> Plug-in Development -> Fragment Project`
	
Procedure is similar like creating new plugin, it just requires to define Host bundle.In our case it would be 
de.atron.testing.sample.consumer

Copy content of a `sample/sample.consumer/src/test/java` to `de.atron.testing.sample.consumer.test/src`.
Add require bundle to test dependencies:

	Require-Bundle: org.junit;bundle-version="4.12.0",
	 org.assertj.core;bundle-version="3.4.1",
	 org.mockito.mockito-core;bundle-version="1.10.19",
	 org.objenesis;bundle-version="2.1.0",
	 org.hamcrest.core;bundle-version="1.3.0"


 Run TestedServiceTest as Junit and this will run test.
 

## How to create a product

Copy equinox.application product and using Content tab in Product Editor add 
 - remove sample.consumer dependency (marked as error)
 - add de.atron.testing.sample.service
 - add de.atron.testing.sample.consumer
 - add org.apache.commons.lang3
 
Product can be exported to file and becomes executable that can run osgi server or can be used to launch application in IDE.
Launch it as Eclipse application in IDE and execute some equinox commands to analysze state.
Notice that services are present


## How to declare services

see git projects OSGI-INF folder and manifest.
They need to be added manually

<http://www.vogella.com/tutorials/OSGiServices/article.html#tutorial-define-a-declarative-osgi-service>
 