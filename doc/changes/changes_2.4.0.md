# Virtual Schema for document data in files on AWS S3 2.4.0, released 2022-??-??

Code name: Support for Extension Manager

## Summary

This release allows installing an S3 Virtual Schema using the [extension manager](https://github.com/exasol/extension-manager). It also upgrades dependencies to fix CVE-2022-2047 and CVE-2022-38751.

## Features

* #70: Added validation that awsEndpointOverride starts without protocol
* #83: Added support for extension manager

## Bug Fixes

* #85: Fixed MFA-support in integration tests 

## Dependency Updates

### Compile Dependency Updates

* Added `ch.qos.reload4j:reload4j:1.2.22`
* Updated `com.exasol:error-reporting-java:0.4.1` to `1.0.0`
* Updated `com.exasol:virtual-schema-common-document-files:7.1.0` to `7.1.1`
* Updated `software.amazon.awssdk:s3:2.17.238` to `2.17.288`

### Test Dependency Updates

* Updated `com.amazonaws:aws-java-sdk-s3:1.12.267` to `1.12.317`
* Removed `com.brsanthu:migbase64:2.2`
* Added `com.exasol:bucketfs-java:2.4.0`
* Added `com.exasol:exasol-testcontainers:6.2.0`
* Added `com.exasol:extension-manager-integration-test-java:0.1.0`
* Updated `com.exasol:hamcrest-resultset-matcher:1.5.1` to `1.5.2`
* Updated `com.exasol:maven-project-version-getter:1.1.0` to `1.2.0`
* Added `com.exasol:parquet-io-java:1.3.3`
* Updated `com.exasol:performance-test-recorder-java:0.1.0` to `0.1.1`
* Updated `com.exasol:small-json-files-test-fixture:0.1.1` to `0.1.3`
* Updated `com.exasol:test-db-builder-java:3.3.3` to `3.4.0`
* Updated `com.exasol:virtual-schema-common-document-files:7.1.0` to `7.1.1`
* Removed `com.fasterxml.jackson.core:jackson-annotations:2.13.3`
* Removed `com.fasterxml.jackson.core:jackson-core:2.13.3`
* Updated `com.fasterxml.jackson.core:jackson-databind:2.13.3` to `2.14.0-rc1`
* Removed `io.swagger.core.v3:swagger-annotations:2.2.1`
* Removed `org.glassfish.jersey.core:jersey-client:2.36`
* Removed `org.glassfish.jersey.inject:jersey-hk2:2.36`
* Removed `org.glassfish.jersey.media:jersey-media-json-jackson:2.36`
* Removed `org.glassfish.jersey.media:jersey-media-multipart:2.36`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.8.2` to `5.9.1`
* Updated `org.junit.jupiter:junit-jupiter-params:5.8.2` to `5.9.1`
* Updated `org.mockito:mockito-core:4.6.1` to `4.8.0`
* Updated `org.testcontainers:junit-jupiter:1.17.3` to `1.17.5`
* Updated `org.testcontainers:localstack:1.17.3` to `1.17.5`
* Updated `org.yaml:snakeyaml:1.30` to `1.33`

### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:1.1.1` to `1.1.2`
* Updated `com.exasol:project-keeper-maven-plugin:2.4.6` to `2.8.0`
* Removed `io.swagger.codegen.v3:swagger-codegen-maven-plugin:3.0.34`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.0.0` to `3.1.0`
* Removed `org.codehaus.mojo:build-helper-maven-plugin:3.3.0`
