# S3 Document Files Virtual Schema 2.5.2, released 2023-04-??

Code name:

## Summary

## Refactoring

* #114: Migrated to AWS CDK v2

## Dependency Updates

### Virtual Schema for Document Data in Files on AWS S3

#### Compile Dependency Updates

* Updated `software.amazon.awssdk:s3:2.20.28` to `2.20.46`

#### Test Dependency Updates

* Updated `com.amazonaws:aws-java-sdk-s3:1.12.431` to `1.12.449`
* Updated `com.exasol:exasol-test-setup-abstraction-java:2.0.0` to `2.0.1`
* Removed `com.exasol:exasol-testcontainers:6.5.1`
* Updated `com.exasol:extension-manager-integration-test-java:0.2.2` to `0.2.3`
* Updated `com.exasol:hamcrest-resultset-matcher:1.5.2` to `1.5.3`
* Updated `com.exasol:small-json-files-test-fixture:0.1.4` to `0.1.5`
* Updated `org.jacoco:org.jacoco.agent:0.8.8` to `0.8.9`
* Updated `org.mockito:mockito-core:5.2.0` to `5.3.0`
* Updated `org.testcontainers:junit-jupiter:1.17.6` to `1.18.0`
* Updated `org.testcontainers:localstack:1.17.6` to `1.18.0`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:1.2.2` to `1.2.3`
* Updated `com.exasol:project-keeper-maven-plugin:2.9.6` to `2.9.7`
* Updated `org.apache.maven.plugins:maven-compiler-plugin:3.10.1` to `3.11.0`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.2.1` to `3.3.0`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.0.0-M8` to `3.0.0`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.0.0-M8` to `3.0.0`
* Added `org.basepom.maven:duplicate-finder-maven-plugin:1.5.1`
* Updated `org.codehaus.mojo:flatten-maven-plugin:1.3.0` to `1.4.1`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.14.2` to `2.15.0`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.8` to `0.8.9`
