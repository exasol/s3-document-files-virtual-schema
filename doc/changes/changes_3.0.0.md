# S3 Document Files Virtual Schema 3.0.0, released 2023-12-13

Code name: Support Exasol 8

## Summary

This release adds support for Exasol 8. This brings the following changes:

### Remove support for `TIMESTAMP WITH LOCAL TIME ZONE`

This release removes support for data type `TIMESTAMP WITH LOCAL TIME ZONE`. This type caused problems with the stricter type checks enabled by default in Exasol 8, causing pushdown queries for document based virtual schemas to fail with the following error:

```
Data type mismatch in column number 5 (1-indexed).Expected TIMESTAMP(3) WITH LOCAL TIME ZONE, but got TIMESTAMP(3).
```

We fixed this error by removing support `TIMESTAMP WITH LOCAL TIME ZONE` completely.

###  Support `ALTER VIRTUAL SCHEMA SET`

This release adds support for `ALTER VIRTUAL SCHEMA SET`. This will allow changing properties like `MAPPING` of a virtual schema without dropping and re-creating it:

```sql
-- Update EDML mapping of the virtual schema
ALTER VIRTUAL SCHEMA MY_VIRTUAL_SCHEMA SET MAPPING = '...';

-- Enable remote logging or change the log level
ALTER VIRTUAL SCHEMA MY_VIRTUAL_SCHEMA SET DEBUG_ADDRESS = 'host:3000' LOG_LEVEL = 'FINEST';
ALTER VIRTUAL SCHEMA MY_VIRTUAL_SCHEMA SET LOG_LEVEL = 'INFO';
```

See the [documentation for `ALTER SCHEMA`](https://docs.exasol.com/db/latest/sql/alter_schema.htm) for details.

## Features

* #140: Added support for Exasol 8

## Dependency Updates

### Virtual Schema for Document Data in Files on AWS S3

#### Compile Dependency Updates

* Updated `com.exasol:virtual-schema-common-document-files:7.3.6` to `8.0.0`
* Updated `software.amazon.awssdk:s3:2.21.16` to `2.21.44`

#### Test Dependency Updates

* Updated `com.amazonaws:aws-java-sdk-s3:1.12.582` to `1.12.613`
* Updated `com.exasol:exasol-test-setup-abstraction-java:2.0.4` to `2.1.0`
* Updated `com.exasol:extension-manager-integration-test-java:0.5.5` to `0.5.7`
* Updated `com.exasol:hamcrest-resultset-matcher:1.6.2` to `1.6.3`
* Updated `com.exasol:small-json-files-test-fixture:0.1.7` to `0.1.8`
* Updated `com.exasol:test-db-builder-java:3.5.1` to `3.5.3`
* Updated `com.exasol:virtual-schema-common-document-files:7.3.6` to `8.0.0`
* Updated `com.fasterxml.jackson.core:jackson-databind:2.15.3` to `2.16.0`
* Updated `nl.jqno.equalsverifier:equalsverifier:3.15.3` to `3.15.4`
* Updated `org.mockito:mockito-core:5.7.0` to `5.8.0`
* Updated `org.testcontainers:junit-jupiter:1.19.1` to `1.19.3`
* Updated `org.testcontainers:localstack:1.19.1` to `1.19.3`

#### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:2.9.15` to `2.9.17`
* Updated `org.apache.maven.plugins:maven-dependency-plugin:3.6.0` to `3.6.1`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.1.2` to `3.2.2`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.1.2` to `3.2.2`
* Updated `org.codehaus.mojo:exec-maven-plugin:3.1.0` to `3.1.1`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.16.1` to `2.16.2`
