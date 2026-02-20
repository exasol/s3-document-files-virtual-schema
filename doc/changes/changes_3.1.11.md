# S3 Document Files Virtual Schema 3.1.11, released 2026-02-20

Code name: S3 Virtual Schema file mapping failure if there is more than one table definition in the mapping

## Summary

This release fixes S3 Virtual Schema file mapping failure if there is more than one table definition in the mapping

## Bugfixes

* #201: S3 Virtual Schema for PARQUET files fails if there is more than one table definition in the mapping

## Dependency Updates

### Virtual Schema for Document Data in Files on AWS S3

#### Compile Dependency Updates

* Updated `com.exasol:virtual-schema-common-document-files:8.1.12` to `8.1.14`

#### Test Dependency Updates

* Updated `com.exasol:exasol-test-setup-abstraction-java:2.1.10` to `2.1.11`
* Updated `com.exasol:virtual-schema-common-document-files:8.1.12` to `8.1.14`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.5` to `2.0.6`
* Updated `com.exasol:project-keeper-maven-plugin:5.4.4` to `5.4.6`
* Updated `org.apache.maven.plugins:maven-compiler-plugin:3.14.1` to `3.15.0`
* Updated `org.apache.maven.plugins:maven-dependency-plugin:3.9.0` to `3.10.0`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.20.1` to `2.21.0`
