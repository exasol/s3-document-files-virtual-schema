# s3-document-files-virtual-schema 0.2.0, released 2020-XX-XX
 
Code name: 

## Summary

This Release implements the changes for the virtual-schema-common-document-files 3.0.0 release.
By that this dialect now supports:
* A `SOURCE_REFERENCE` column that contains the name of the S3 URI of the document
* Selection on the `SOURCE_REFERENCE` column.

The changes also cause an API change for the UDF definition. See the [user guide](../user_guide/user_guide.md).

## Features / Enhancements

* #4: Use Virtual-Schema-Common-Document-Files-3.0.0

## Dependency Updates:

* Updated `com.exasol:exasol-testcontainers` from 3.2.0 to 3.3.0
* Added `com.exasol:test-db-builder-java` 2.0.0
* Added `com.exasol:udf-debugging-java` 0.2.0
* Added `com.exasol:hamcrest-resultset-matcher` 1.2.1
* Updated `com.exasol:project-keeper-maven-plugin` from 0.1.0 to 0.2.0
