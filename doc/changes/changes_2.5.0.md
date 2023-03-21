# S3 Document Files Virtual Schema 2.5.0, released 2023-03-21

Code name: Auto-inference for Parquet

## Summary

This release adds automatic schema inference for Parquet files. This means that you don't need to specify a `mapping` element in the EDML definition. Instead the mapping will be automatically detected. See the [EDML user guide](https://github.com/exasol/virtual-schema-common-document/blob/main/doc/user_guide/edml_user_guide.md#automatic-mapping-inference) for details.

## Features

* #108: Added auto-inference for Parquet files

## Dependency Updates

### Virtual Schema for Document Data in Files on AWS S3

#### Compile Dependency Updates

* Updated `com.exasol:virtual-schema-common-document-files:7.1.4` to `7.2.0`
* Removed `commons-net:commons-net:3.9.0`
* Updated `org.slf4j:slf4j-jdk14:2.0.6` to `2.0.7`
* Updated `software.amazon.awssdk:s3:2.20.6` to `2.20.28`

#### Test Dependency Updates

* Updated `com.amazonaws:aws-java-sdk-s3:1.12.409` to `1.12.431`
* Updated `com.exasol:java-class-list-verifier:0.2.1` to `0.2.2`
* Updated `com.exasol:virtual-schema-common-document-files:7.1.4` to `7.2.0`
* Updated `nl.jqno.equalsverifier:equalsverifier:3.13.2` to `3.14.1`
* Updated `org.mockito:mockito-core:5.1.1` to `5.2.0`

#### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:2.9.3` to `2.9.5`
* Updated `org.apache.maven.plugins:maven-assembly-plugin:3.4.2` to `3.5.0`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.1.0` to `3.2.1`
