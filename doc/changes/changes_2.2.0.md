# Virtual Schema for document data in files on AWS S3 2.2.0, released 2022-??-??

Code name: 2.2.0: Support for Extension Manager

## Summary

This release allows installing an S3 Virtual Schema using the [extension manager](https://github.com/exasol/extension-manager).

## Features

* #70: Added validation that awsEndpointOverride starts without protocol
* #83: Added support for extension manager

## Bug Fixes

* #85: Fixed MFA-support in integration tests 

## Dependency Updates

### Compile Dependency Updates

* Added `com.brsanthu:migbase64:2.2`
* Added `com.fasterxml.jackson.core:jackson-annotations:2.13.3`
* Added `com.fasterxml.jackson.core:jackson-core:2.13.3`
* Added `com.fasterxml.jackson.core:jackson-databind:2.13.3`
* Added `io.swagger.core.v3:swagger-annotations:2.2.1`
* Added `org.glassfish.jersey.core:jersey-client:2.36`
* Added `org.glassfish.jersey.inject:jersey-hk2:2.36`
* Added `org.glassfish.jersey.media:jersey-media-json-jackson:2.36`
* Added `org.glassfish.jersey.media:jersey-media-multipart:2.36`
* Updated `software.amazon.awssdk:s3:2.17.207` to `2.17.224`

### Test Dependency Updates

* Updated `com.amazonaws:aws-java-sdk-s3:1.12.236` to `1.12.253`
* Added `com.exasol:maven-project-version-getter:1.1.0`
* Updated `com.exasol:test-db-builder-java:3.3.2` to `3.3.3`
* Updated `com.exasol:udf-debugging-java:0.6.1` to `0.6.4`
* Updated `org.testcontainers:junit-jupiter:1.17.2` to `1.17.3`
* Updated `org.testcontainers:localstack:1.17.2` to `1.17.3`

### Plugin Dependency Updates

* Added `io.swagger.codegen.v3:swagger-codegen-maven-plugin:3.0.34`
* Updated `org.apache.maven.plugins:maven-clean-plugin:2.5` to `3.2.0`
* Added `org.codehaus.mojo:build-helper-maven-plugin:3.3.0`
* Added `org.codehaus.mojo:exec-maven-plugin:3.0.0`
