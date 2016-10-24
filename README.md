# Introduction

This repository contains "classic" maven project separated into modules:
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

# Goal
Goal of this repository is to enrich these project so that they are *"OSGI enabled"*.