# S3 Document Files Virtual Schema 2.8.2, released 2023-10-25

Code name: Validate EDML for duplicate table names

## Summary

This release upgrades to [virtual-schema-common-document-files 7.3.6](https://github.com/exasol/virtual-schema-common-document-files/releases/tag/7.3.6). This new version validates that the given EDML mapping uses unique values for each `destinationTable` entry as duplicate values led to unexpected behavior.

## Features

* #134: Added validation for duplicate `destinationTable` entries

## Dependency Updates

### Virtual Schema for Document Data in Files on AWS S3

#### Compile Dependency Updates

* Updated `com.exasol:virtual-schema-common-document-files:7.3.4` to `7.3.6`
* Updated `software.amazon.awssdk:s3:2.20.156` to `2.21.6`

#### Test Dependency Updates

* Updated `com.amazonaws:aws-java-sdk-s3:1.12.559` to `1.12.572`
* Updated `com.exasol:extension-manager-integration-test-java:0.5.1` to `0.5.3`
* Updated `com.exasol:virtual-schema-common-document-files:7.3.4` to `7.3.6`
* Updated `com.fasterxml.jackson.core:jackson-databind:2.15.2` to `2.15.3`
* Updated `org.jacoco:org.jacoco.agent:0.8.10` to `0.8.11`
* Updated `org.mockito:mockito-core:5.5.0` to `5.6.0`
* Updated `org.testcontainers:junit-jupiter:1.19.0` to `1.19.1`
* Updated `org.testcontainers:localstack:1.19.0` to `1.19.1`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:1.3.0` to `1.3.1`
* Updated `com.exasol:project-keeper-maven-plugin:2.9.12` to `2.9.14`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.4.0` to `3.4.1`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.16.0` to `2.16.1`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.10` to `0.8.11`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.1.2184` to `3.10.0.2594`
