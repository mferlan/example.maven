# example.maven
Repository for OSGI / Maven playground

#SampleTest
POM-first project using Junit, mockito, assertj, commons-lang3 libraries that produces OSGI-fied jar
OSGI-fication is done using bnd maven plugin. http://njbartlett.name/2015/03/27/announcing-bnd-maven-plugin.html

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
  
http://www.vogella.com/tutorials/EclipseTycho/article.html

http://stackoverflow.com/questions/20842256/how-to-manage-tycho-eap-versionning-correctly

https://git.eclipse.org/c/orbit/orbit-recipes.git/tree/README.md

https://github.com/eclipse/ebr
    



