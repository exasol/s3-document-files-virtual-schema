# Virtual Schema for document data in files on AWS S3 1.1.0, released 2021-??-??

Code name: Parquet support

## Features

* #17: Updated vs-common-document-files-dependency

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:error-reporting-java:0.2.2` to `0.4.0`
* Updated `com.exasol:virtual-schema-common-document-files:1.0.0` to `2.0.0-SNAPSHOT`
* Updated `org.mockito:mockito-core:3.6.28` to `3.11.2`
* Updated `org.slf4j:slf4j-jdk14:1.7.30` to `1.7.31`
* Updated `software.amazon.awssdk:s3:2.15.51` to `2.16.95`

### Test Dependency Updates

* Updated `com.amazonaws:aws-java-sdk-s3:1.11.924` to `1.12.17`
* Added `com.exasol:exasol-test-setup-abstraction-java:0.1.0`
* Removed `com.exasol:exasol-testcontainers:3.4.0`
* Updated `com.exasol:hamcrest-resultset-matcher:1.3.0` to `1.4.0`
* Updated `com.exasol:test-db-builder-java:2.0.0` to `3.2.0`
* Updated `com.exasol:udf-debugging-java:0.3.0` to `0.4.0-SNAPSHOT`
* Updated `com.exasol:virtual-schema-common-document-files:1.0.0` to `2.0.0-SNAPSHOT`
* Updated `junit:junit:4.13.1` to `4.13.2`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.7.0` to `5.7.2`
* Updated `org.junit.jupiter:junit-jupiter-params:5.7.0` to `5.7.2`
* Updated `org.testcontainers:junit-jupiter:1.15.1` to `1.15.3`
* Updated `org.testcontainers:localstack:1.15.1` to `1.15.3`

### Plugin Dependency Updates

* Updated `com.exasol:artifact-reference-checker-maven-plugin:0.3.1` to `0.4.0`
* Added `com.exasol:error-code-crawler-maven-plugin:0.5.0`
* Updated `com.exasol:project-keeper-maven-plugin:0.3.0` to `0.9.0`
* Added `io.github.zlika:reproducible-build-maven-plugin:0.13`
* Removed `org.apache.maven.plugins:maven-assembly-plugin:3.3.0`
* Added `org.apache.maven.plugins:maven-shade-plugin:3.2.4`
