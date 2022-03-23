# Virtual Schema for document data in files on AWS S3 2.1.0, released 2022-??-??

Code name: Prepared for Java UDF startup time improver

## Summary

In this release we prepared this Virtual Schema for Java UDF startup time improver. That allows you to optimize it with the [java-udf-startup-time-improver](https://github.com/exasol/java-udf-startup-time-improver/).

## Features

* #74: Added class list generation

## Dependency Updates

### Compile Dependency Updates

* Added `com.exasol:java-class-list-verifier:0.2.0`
* Updated `org.mockito:mockito-core:4.1.0` to `4.4.0`
* Updated `org.slf4j:slf4j-jdk14:1.7.32` to `1.7.36`
* Updated `software.amazon.awssdk:s3:2.17.66` to `2.17.151`

### Test Dependency Updates

* Updated `com.amazonaws:aws-java-sdk-s3:1.12.94` to `1.12.180`
* Updated `com.exasol:test-db-builder-java:3.2.2` to `3.3.1`
* Updated `com.exasol:udf-debugging-java:0.5.0` to `0.6.0`
* Updated `org.testcontainers:junit-jupiter:1.16.2` to `1.16.3`
* Updated `org.testcontainers:localstack:1.16.2` to `1.16.3`
* Updated `org.yaml:snakeyaml:1.29` to `1.30`

### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:1.3.4` to `2.0.0`
* Updated `org.apache.maven.plugins:maven-dependency-plugin:2.8` to `3.2.0`
* Added `org.codehaus.mojo:flatten-maven-plugin:1.2.7`
