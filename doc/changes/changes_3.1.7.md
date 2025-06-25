# S3 Document Files Virtual Schema 3.1.7, released 2025-06-25

Code name: Improve query plan logging

## Summary

This release implements logging for `loadFiles` method of the class `S3FileFinder`. This allows improving logging around the paths that lead to the creation of the query plan.

## Features

* #190: Add more logging around the paths that lead to the creation of the query plan.

## Dependency Updates

### Virtual Schema for Document Data in Files on AWS S3

#### Compile Dependency Updates

* Updated `com.exasol:virtual-schema-common-document-files:8.1.9` to `8.1.10`

#### Test Dependency Updates

* Updated `com.exasol:virtual-schema-common-document-files:8.1.9` to `8.1.10`

#### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:5.1.0` to `5.2.2`
