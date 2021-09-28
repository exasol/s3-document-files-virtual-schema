# Virtual Schema for document data in files on AWS S3 1.3.0, released 2021-09-28

Code name: New Mapping Types

## Summary

This release integrates the new features from the virtual-schema-common-document 6.1.0:

> In this release we added the following new mapping types:
>
> * `toDoubleMapping`
> * `toBoolMapping`
> * `toDateMapping`
> * `toTimestampMapping`
>
> In order to use the new features, please update you EDML definitions to version `1.3.0` (no breaking changes).

For details see the [changelog of the virtual-schema-common-document](https://github.com/exasol/virtual-schema-common-document/blob/main/doc/changes/changes_6.1.0.md).

## Bug Fixes

* #27: Fixed sonar security hotspot

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:virtual-schema-common-document-files:2.1.0` to `2.2.0-SNAPSHOT`
* Updated `org.mockito:mockito-core:3.11.2` to `3.12.4`
* Updated `org.slf4j:slf4j-jdk14:1.7.31` to `1.7.32`
* Updated `software.amazon.awssdk:s3:2.16.95` to `2.17.47`

### Runtime Dependency Updates

* Removed `org.jacoco:org.jacoco.agent:0.8.5`

### Test Dependency Updates

* Updated `com.amazonaws:aws-java-sdk-s3:1.12.17` to `1.12.75`
* Updated `com.exasol:exasol-test-setup-abstraction-java:0.1.1` to `0.2.0`
* Updated `com.exasol:hamcrest-resultset-matcher:1.4.0` to `1.5.0`
* Updated `com.exasol:test-db-builder-java:3.2.0` to `3.2.1`
* Updated `com.exasol:virtual-schema-common-document-files:2.1.0` to `2.2.0-SNAPSHOT`
* Added `org.jacoco:org.jacoco.agent:0.8.7`
* Updated `org.jacoco:org.jacoco.core:0.8.5` to `0.8.7`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.7.2` to `5.8.1`
* Updated `org.junit.jupiter:junit-jupiter-params:5.7.2` to `5.8.1`
* Updated `org.testcontainers:junit-jupiter:1.15.3` to `1.16.0`
* Updated `org.testcontainers:localstack:1.15.3` to `1.16.0`

### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:0.5.0` to `0.6.0`
* Updated `com.exasol:project-keeper-maven-plugin:0.10.0` to `1.2.0`
* Added `org.apache.maven.plugins:maven-assembly-plugin:3.3.0`
* Updated `org.apache.maven.plugins:maven-jar-plugin:2.4` to `3.2.0`
* Removed `org.apache.maven.plugins:maven-shade-plugin:3.2.4`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.5` to `0.8.7`
