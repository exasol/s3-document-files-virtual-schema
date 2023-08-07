# Virtual Schema for Document Data in Files on AWS S3 2.7.0, released 2023-08-07

Code name: MinIO Support

## Summary

This release adds support for [MinIO](https://min.io) and updates dependencies.

MinIO is a high-performance, S3 compatible object store. It is built for large scale AI/ML, data lake and database workloads. It runs on-prem and on any cloud (public or private) and from the data center to the edge. MinIO is software-defined and open source under GNU AGPL v3.

Enhanced support by VSS3 includes
* Updated requirements specification
* Dedicated Hands-On Guide for MinIO
* Integration tests based on [Localstack](https://github.com/localstack/localstack) to be executed for [MinIO](https://min.io), too

## Features

* #128: Added Hand-On Guide for min.io
* #125: Added integration tests for min.io

## Dependency Updates

### Virtual Schema for Document Data in Files on AWS S3

#### Compile Dependency Updates

* Updated `software.amazon.awssdk:s3:2.20.98` to `2.20.111`

#### Test Dependency Updates

* Updated `com.amazonaws:aws-java-sdk-s3:1.12.501` to `1.12.514`
* Updated `com.exasol:udf-debugging-java:0.6.9` to `0.6.10`
* Updated `nl.jqno.equalsverifier:equalsverifier:3.14.3` to `3.15`
* Updated `org.jacoco:org.jacoco.agent:0.8.9` to `0.8.10`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.9.3` to `5.10.0`
* Updated `org.junit.jupiter:junit-jupiter-params:5.9.3` to `5.10.0`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:1.2.3` to `1.3.0`
* Updated `com.exasol:project-keeper-maven-plugin:2.9.7` to `2.9.9`
* Updated `org.apache.maven.plugins:maven-assembly-plugin:3.5.0` to `3.6.0`
* Updated `org.apache.maven.plugins:maven-dependency-plugin:3.5.0` to `3.6.0`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.0.0` to `3.1.2`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.0.0` to `3.1.2`
* Updated `org.basepom.maven:duplicate-finder-maven-plugin:1.5.1` to `2.0.1`
* Updated `org.codehaus.mojo:flatten-maven-plugin:1.4.1` to `1.5.0`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.15.0` to `2.16.0`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.9` to `0.8.10`
