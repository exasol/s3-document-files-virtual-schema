# Virtual Schema for document data in files on AWS S3 2.2.0, released 2022-??-??

Code name: 2.2.0: Support for Extension Manager

## Summary

This release allows installing an S3 Virtual Schema using the [extension manager](https://github.com/exasol/extension-manager).

## Features

* #70: Added validation that awsEndpointOverride starts without protocol
* #83: Added support for extension manager

## Bug Fixes

* #85: Fixed MFA-support in integration tests 

## Dependency Updates

### Compile Dependency Updates

* Added `com.google.code.gson:gson:2.9.0`
* Added `com.squareup.okhttp:logging-interceptor:2.7.5`
* Added `com.squareup.okhttp:okhttp:2.7.5`
* Added `io.gsonfire:gson-fire:1.8.5`
* Added `io.swagger:swagger-annotations:1.6.6`

### Test Dependency Updates

* Added `com.exasol:maven-project-version-getter:1.1.0`
* Added `javax.annotation:javax.annotation-api:1.3.2`

### Plugin Dependency Updates

* Added `io.swagger:swagger-codegen-maven-plugin:2.4.27`
* Updated `org.apache.maven.plugins:maven-clean-plugin:2.5` to `3.2.0`
* Added `org.codehaus.mojo:build-helper-maven-plugin:3.3.0`
* Added `org.codehaus.mojo:exec-maven-plugin:3.0.0`
