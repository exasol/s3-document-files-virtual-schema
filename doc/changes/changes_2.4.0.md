# Virtual Schema for document data in files on AWS S3 2.4.0, released 2022-??-??

Code name: Support for Extension Manager

## Summary

This release allows installing an S3 Virtual Schema using the [extension manager](https://github.com/exasol/extension-manager). It also upgrades dependencies to fix CVE-2022-2047.

## Features

* #70: Added validation that awsEndpointOverride starts without protocol
* #83: Added support for extension manager

## Bug Fixes

* #85: Fixed MFA-support in integration tests 

## Dependency Updates

### Compile Dependency Updates

* Updated `software.amazon.awssdk:s3:2.17.238` to `2.17.240`

### Test Dependency Updates

* Updated `com.amazonaws:aws-java-sdk-s3:1.12.267` to `1.12.269`
* Updated `io.swagger.core.v3:swagger-annotations:2.2.1` to `2.2.2`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.8.2` to `5.9.0`
* Updated `org.junit.jupiter:junit-jupiter-params:5.8.2` to `5.9.0`

### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:1.1.1` to `1.1.2`
* Updated `com.exasol:project-keeper-maven-plugin:2.4.6` to `2.7.0`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.0.0` to `3.1.0`
