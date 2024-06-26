# S3 Document Files Virtual Schema 3.0.6, released 2024-05-08

Code name: Improve error handling for extension

## Summary

This release improves error handling when creating a new Virtual Schema using the extension: the extension now checks if a schema with the same name exists and returns a helpful error message. This check is case-insensitive because Exasol's `CONNECTION` names are also case-insensitive.

## Bugfix

* #164: Improved error handling for creating Virtual Schema using the extension

## Dependency Updates

### Virtual Schema for Document Data in Files on AWS S3

#### Compile Dependency Updates

* Updated `software.amazon.awssdk:s3:2.25.27` to `2.25.45`

#### Runtime Dependency Updates

* Updated `org.slf4j:slf4j-jdk14:2.0.12` to `2.0.13`

#### Test Dependency Updates

* Updated `com.amazonaws:aws-java-sdk-s3:1.12.697` to `1.12.715`
* Updated `com.exasol:exasol-test-setup-abstraction-java:2.1.2` to `2.1.3`
* Updated `com.exasol:extension-manager-integration-test-java:0.5.8` to `0.5.11`
* Updated `com.exasol:small-json-files-test-fixture:0.1.9` to `0.1.10`
* Updated `com.exasol:udf-debugging-java:0.6.12` to `0.6.13`
* Updated `com.fasterxml.jackson.core:jackson-databind:2.17.0` to `2.17.1`

### Extension

#### Compile Dependency Updates

* Updated `@exasol/extension-manager-interface:0.4.1` to `0.4.2`

#### Development Dependency Updates

* Updated `typescript-eslint:^7.5.0` to `^7.8.0`
* Updated `typescript:^5.4.4` to `^5.4.5`
