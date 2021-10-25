# Virtual Schema for document data in files on AWS S3 1.4.0, released 2021-10-25

Code name: Regression Tests

## Summary

In this release we added regression tests that help us to monitor the performance of the adapter cross releases.

## Features

* #35: Added GitHub workflow to run regression tests
* #41: Added cache for s3 uploads during tests

## Documentation

* #33: Added hands-on guide for parquet files
* #31: Clarified connection string documentation

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:virtual-schema-common-document-files:2.2.0` to `2.2.1`
* Updated `org.mockito:mockito-core:3.12.4` to `4.0.0`
* Updated `software.amazon.awssdk:s3:2.17.47` to `2.17.66`

### Test Dependency Updates

* Updated `com.amazonaws:aws-java-sdk-s3:1.12.75` to `1.12.94`
* Updated `com.exasol:exasol-test-setup-abstraction-java:0.2.0` to `0.2.1`
* Updated `com.exasol:hamcrest-resultset-matcher:1.5.0` to `1.5.1`
* Updated `com.exasol:udf-debugging-java:0.4.0` to `0.4.1`
* Updated `com.exasol:virtual-schema-common-document-files:2.2.0` to `2.2.1`
* Updated `org.testcontainers:junit-jupiter:1.16.0` to `1.16.2`
* Updated `org.testcontainers:localstack:1.16.0` to `1.16.2`

### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:1.2.0` to `1.3.1`
