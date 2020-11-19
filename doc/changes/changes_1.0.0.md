# s3-document-files-virtual-schema 1.0.0, released 2020-11-19
 
Code name: Production ready release

## Summary

This release implements the changes for the virtual-schema-common-document-files 3.0.0 release.
By that this dialect now supports:
* A `SOURCE_REFERENCE` column that contains the name of the S3 URI of the document
* Selection on the `SOURCE_REFERENCE` column.

The changes also cause an API change for the UDF definition. See the [user guide](../user_guide/user_guide.md).

Known issues:

* Certain virtual-schema queries can cause a database crash. For details see [#41](https://github.com/exasol/virtual-schema-common-document-files/issues/41).

## Features / Enhancements

* #4: Use Virtual-Schema-Common-Document-Files-3.0.0
* #6: Improved errors messages for wrong connection strings

## Documentation

* #7: Added hands-on guide

## Dependency Updates:

* Updated `com.exasol:exasol-testcontainers` from 3.2.0 to 3.3.1
* Updated `org.testcontainers:junit-jupiter` from 1.14.3 to 1.15.0
* Updated `org.testcontainers:localstack` from 1.14.3 to 1.15.0
* Added `com.exasol:test-db-builder-java` 2.0.0
* Added `com.exasol:udf-debugging-java` 0.2.0
* Added `com.exasol:hamcrest-resultset-matcher` 1.2.1
* Updated `com.exasol:project-keeper-maven-plugin` from 0.1.0 to 0.3.0
* Added `com.exasol:error-reporting-java` 0.2.0
* Updated `org.mockito:mockito-core` from 3.5.13 to 3.6.0
* Updated `software.amazon.awssdk:bom` from 2.15.2 to 2.15.31
* Updated `com.amazonaws:aws-java-sdk-s3` from 1.11.875 to 1.11.903
* Updated `com.exasol:org.junit.jupiter` from
* Updated `org.junit.jupiter:junit-jupiter-params` from 5.6.2 to 5.7.0
* Updated `org.junit.jupiter:junit-jupiter-engine` from  5.6.2 to 5.7.0

