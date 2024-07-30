# S3 Document Files Virtual Schema 3.1.1, released 2024-07-30

Code name: Fix CVE-2024-25638 in `dnsjava:dnsjava:jar:3.4.0:compile`

## Summary

This release fixes vulnerability CVE-2024-25638 in `dnsjava:dnsjava:jar:3.4.0:compile`.

## Security

* #170: Fixed vulnerability CVE-2024-25638 in `dnsjava:dnsjava:jar:3.4.0:compile`

## Dependency Updates

### Virtual Schema for Document Data in Files on AWS S3

#### Compile Dependency Updates

* Updated `com.exasol:virtual-schema-common-document-files:8.1.0` to `8.1.2`
* Updated `software.amazon.awssdk:s3:2.26.3` to `2.26.26`

#### Test Dependency Updates

* Updated `com.amazonaws:aws-java-sdk-s3:1.12.744` to `1.12.766`
* Updated `com.exasol:virtual-schema-common-document-files:8.1.0` to `8.1.2`
* Updated `com.fasterxml.jackson.core:jackson-databind:2.17.1` to `2.17.2`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.10.2` to `5.10.3`
* Updated `org.junit.jupiter:junit-jupiter-params:5.10.2` to `5.10.3`
* Updated `org.testcontainers:junit-jupiter:1.19.8` to `1.20.0`
* Updated `org.testcontainers:localstack:1.19.8` to `1.20.0`

### Extension

#### Development Dependency Updates

* Updated `eslint:^9.5.0` to `^9.8.0`
* Updated `ts-jest:^29.1.5` to `^29.2.3`
* Updated `typescript:^5.4.5` to `^5.5.4`
* Updated `esbuild:^0.21.5` to `^0.23.0`
