# S3 Document Files Virtual Schema 2.8.4, released 2023-??-??

Code name: Adapt to Exasol v8

## Summary

This release adapts the virtual schema to Exasol v8.

## Features

* #140: Adapted to Exasol v8

## Dependency Updates

### Virtual Schema for Document Data in Files on AWS S3

#### Compile Dependency Updates

* Updated `com.exasol:virtual-schema-common-document-files:7.3.6` to `7.3.7`
* Updated `software.amazon.awssdk:s3:2.21.16` to `2.21.23`

#### Test Dependency Updates

* Updated `com.amazonaws:aws-java-sdk-s3:1.12.582` to `1.12.589`
* Updated `com.exasol:exasol-test-setup-abstraction-java:2.0.4` to `2.1.0`
* Updated `com.exasol:extension-manager-integration-test-java:0.5.5` to `0.5.6`
* Updated `com.exasol:virtual-schema-common-document-files:7.3.6` to `7.3.7`
* Updated `org.testcontainers:junit-jupiter:1.19.1` to `1.19.2`
* Updated `org.testcontainers:localstack:1.19.1` to `1.19.2`

#### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:2.9.15` to `2.9.16`
* Updated `org.apache.maven.plugins:maven-dependency-plugin:3.6.0` to `3.6.1`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.1.2` to `3.2.2`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.1.2` to `3.2.2`
