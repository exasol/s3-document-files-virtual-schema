# S3 Document Files Virtual Schema 3.1.0, released 2024-06-17

Code name: Configure column names for automatic mapping inference

## Summary

This release allows configuring the mapping of column names for the automatic mapping inference in Parquet and CSV files. Before, the virtual schema always converted source column names to `UPPER_SNAKE_CASE` to create the Exasol column names. This is now configurable with EDML property `autoInferenceColumnNames`. This property supports the following values:
* `CONVERT_TO_UPPER_SNAKE_CASE`: Convert column names to `UPPER_SNAKE_CASE` (default).
* `KEEP_ORIGINAL_NAME`: Do not convert column names, use column name from source.

See the [EDML user guide](https://github.com/exasol/virtual-schema-common-document/blob/main/doc/user_guide/edml_user_guide.md#column-name-conversion) for details.

## Features

* #168: Added option to keep original column name for auto inference

## Dependency Updates

### Virtual Schema for Document Data in Files on AWS S3

#### Compile Dependency Updates

* Updated `com.exasol:virtual-schema-common-document-files:8.0.4` to `8.1.0`
* Updated `software.amazon.awssdk:s3:2.25.45` to `2.26.3`

#### Test Dependency Updates

* Updated `com.amazonaws:aws-java-sdk-s3:1.12.715` to `1.12.744`
* Updated `com.exasol:exasol-test-setup-abstraction-java:2.1.3` to `2.1.4`
* Updated `com.exasol:extension-manager-integration-test-java:0.5.11` to `0.5.12`
* Updated `com.exasol:java-class-list-verifier:0.2.6` to `0.2.7`
* Updated `com.exasol:virtual-schema-common-document-files:8.0.4` to `8.1.0`
* Updated `org.mockito:mockito-core:5.11.0` to `5.12.0`
* Updated `org.testcontainers:junit-jupiter:1.19.7` to `1.19.8`
* Updated `org.testcontainers:localstack:1.19.7` to `1.19.8`

#### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:4.3.2` to `4.3.3`

### Extension

#### Compile Dependency Updates

* Updated `@exasol/extension-manager-interface:0.4.2` to `0.4.3`

#### Development Dependency Updates

* Updated `eslint:^8.57.0` to `^9.5.0`
* Updated `ts-jest:^29.1.2` to `^29.1.5`
* Updated `typescript-eslint:^7.8.0` to `^8.0.0-alpha.30`
* Updated `esbuild:^0.20.2` to `^0.21.5`
