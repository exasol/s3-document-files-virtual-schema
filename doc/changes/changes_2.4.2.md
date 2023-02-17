# Virtual Schema for Document Data in Files on AWS S3 2.4.2, released 2023-??-??

Code name: Fix vulnerabilities in dependencies

## Summary

This release replaces test dependency `org.yaml:snakeyaml` to avoid vulnerabilities.

## Features

* #106: Replaced `org.yaml:snakeyaml` test dependency

## Dependency Updates

### Virtual Schema for Document Data in Files on AWS S3

#### Compile Dependency Updates

* Removed `ch.qos.reload4j:reload4j:1.2.24`
* Updated `com.exasol:error-reporting-java:1.0.0` to `1.0.1`
* Removed `io.netty:netty-codec:4.1.86.Final`
* Removed `org.eclipse.jetty:jetty-client:11.0.13`
* Updated `software.amazon.awssdk:s3:2.18.3` to `2.20.6`

#### Test Dependency Updates

* Updated `com.amazonaws:aws-java-sdk-s3:1.12.370` to `1.12.409`
* Updated `com.exasol:exasol-test-setup-abstraction-java:1.1.1` to `2.0.0`
* Updated `com.exasol:exasol-testcontainers:6.4.1` to `6.5.1`
* Updated `com.exasol:extension-manager-integration-test-java:0.2.0` to `0.2.2`
* Updated `com.exasol:small-json-files-test-fixture:0.1.3` to `0.1.4`
* Updated `com.exasol:test-db-builder-java:3.4.1` to `3.4.2`
* Updated `com.exasol:udf-debugging-java:0.6.6` to `0.6.8`
* Updated `nl.jqno.equalsverifier:equalsverifier:3.12.3` to `3.13.2`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.9.1` to `5.9.2`
* Updated `org.junit.jupiter:junit-jupiter-params:5.9.1` to `5.9.2`
* Updated `org.mockito:mockito-core:4.10.0` to `5.1.1`
* Removed `org.yaml:snakeyaml:1.33`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:1.2.1` to `1.2.2`
* Updated `com.exasol:project-keeper-maven-plugin:2.9.1` to `2.9.3`
* Updated `org.apache.maven.plugins:maven-dependency-plugin:3.3.0` to `3.5.0`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.0.0-M7` to `3.0.0-M8`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.0.0-M7` to `3.0.0-M8`
* Updated `org.codehaus.mojo:exec-maven-plugin:3.0.0` to `3.1.0`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.13.0` to `2.14.2`

### Extension

#### Development Dependency Updates

* Updated `eslint:^8.20.0` to `^8.34.0`
* Updated `@typescript-eslint/parser:^5.31.0` to `^5.52.0`
* Updated `ts-jest:^28.0.7` to `^29.0.5`
* Updated `@types/jest:^28.1.6` to `^29.4.0`
* Updated `typescript:^4.7.4` to `^4.9.5`
* Updated `@typescript-eslint/eslint-plugin:^5.31.0` to `^5.52.0`
* Updated `jest:28.1.3` to `29.4.3`
* Updated `esbuild:^0.14.50` to `^0.17.8`
