# Virtual Schema for document data in files on AWS S3 2.0.0, released 2022-01-24

Code name: Unified Connection Definition

## Summary

This release changed the syntax of the connection definition. From now on you need to provide a JSON formatted connection definition. For details about the new connection definition please checkout the [user-guide](../user_guide/user_guide.md#user-content-creating-a-connection). This is a **breaking change** you need to update your connection definition in order to use this new version.

The new connection definition standardizes connection definition for accessing S3 from Exasol. In the future all our products that access S3 will support this definition.

The new connection definition allowed us to support all possible S3 bucket names (before there were limitations with bucket names containing dots) and also supports S3 path-style access.

## Features

* #64: Used unified connection definition
* #59: Added support for path-style syntax

## Bug Fixes

* #62: Fixed support for dots in bucket name

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:virtual-schema-common-document-files:4.0.0` to `5.0.0`

### Test Dependency Updates

* Updated `com.exasol:exasol-test-setup-abstraction-java:0.2.1` to `0.2.2`
* Updated `com.exasol:test-db-builder-java:3.2.1` to `3.2.2`
* Updated `com.exasol:udf-debugging-java:0.4.1` to `0.5.0`
* Updated `com.exasol:virtual-schema-common-document-files:4.0.0` to `5.0.0`
