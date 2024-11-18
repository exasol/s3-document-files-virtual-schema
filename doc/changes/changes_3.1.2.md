# S3 Document Files Virtual Schema 3.1.2, released 2024-11-18

Code name: Fix CVE-2024-47561 and CVE-2024-47535

## Summary

This release fixes the following vulnerabilities:
* CVE-2024-47535 in `io.netty:netty-common:jar:4.1.111.Final:compile`
* CVE-2024-47561 in `org.apache.avro:avro:jar:1.11.3:compile`

## Security

* #173: Fixed CVE-2024-47535 in `io.netty:netty-common:jar:4.1.111.Final:compile`
* #174: Fixed CVE-2024-47561 in `org.apache.avro:avro:jar:1.11.3:compile`

## Dependency Updates

### Virtual Schema for Document Data in Files on AWS S3

#### Compile Dependency Updates

* Updated `com.exasol:virtual-schema-common-document-files:8.1.2` to `8.1.5`
* Updated `software.amazon.awssdk:s3:2.26.26` to `2.29.15`

#### Runtime Dependency Updates

* Updated `org.slf4j:slf4j-jdk14:2.0.13` to `2.0.16`

#### Test Dependency Updates

* Updated `com.amazonaws:aws-java-sdk-s3:1.12.766` to `1.12.778`
* Updated `com.exasol:exasol-test-setup-abstraction-java:2.1.4` to `2.1.5`
* Updated `com.exasol:hamcrest-resultset-matcher:1.6.5` to `1.7.0`
* Updated `com.exasol:small-json-files-test-fixture:0.1.10` to `0.1.11`
* Updated `com.exasol:test-db-builder-java:3.5.4` to `3.6.0`
* Updated `com.exasol:virtual-schema-common-document-files:8.1.2` to `8.1.5`
* Updated `com.fasterxml.jackson.core:jackson-databind:2.17.2` to `2.18.1`
* Updated `nl.jqno.equalsverifier:equalsverifier:3.16.1` to `3.17.3`
* Updated `org.hamcrest:hamcrest:2.2` to `3.0`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.10.3` to `5.11.3`
* Updated `org.junit.jupiter:junit-jupiter-params:5.10.3` to `5.11.3`
* Updated `org.mockito:mockito-core:5.12.0` to `5.14.2`
* Updated `org.testcontainers:junit-jupiter:1.20.0` to `1.20.3`
* Updated `org.testcontainers:localstack:1.20.0` to `1.20.3`

#### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:4.3.3` to `4.4.0`
* Added `com.exasol:quality-summarizer-maven-plugin:0.2.0`
* Updated `io.github.zlika:reproducible-build-maven-plugin:0.16` to `0.17`
* Updated `org.apache.maven.plugins:maven-dependency-plugin:3.6.1` to `3.8.0`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.2.5` to `3.5.1`
* Updated `org.apache.maven.plugins:maven-install-plugin:2.4` to `3.1.3`
* Updated `org.apache.maven.plugins:maven-jar-plugin:3.4.1` to `3.4.2`
* Updated `org.apache.maven.plugins:maven-resources-plugin:2.6` to `3.3.1`
* Updated `org.apache.maven.plugins:maven-site-plugin:3.3` to `3.9.1`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.2.5` to `3.5.1`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.16.2` to `2.17.1`

### Extension

#### Development Dependency Updates

* Updated `eslint:^9.8.0` to `9.14.0`
* Updated `ts-jest:^29.2.3` to `^29.2.5`
* Updated `@types/jest:^29.5.12` to `^29.5.14`
* Updated `typescript-eslint:^8.0.0-alpha.30` to `^8.14.0`
* Updated `typescript:^5.5.4` to `^5.6.3`
* Updated `esbuild:^0.23.0` to `^0.24.0`
