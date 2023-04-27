# S3 Document Files Virtual Schema 2.6.0, released 2023-??-??

Code name: Auto-inference for CSV

## Summary

This release adds automatic schema inference for CSV files. This means that you don't need to specify a `mapping` element in the EDML definition. Instead VSBFS will automatically detect the mapping from the CSV files. See the [EDML user guide](https://github.com/exasol/virtual-schema-common-document/blob/main/doc/user_guide/edml_user_guide.md#automatic-mapping-inference) for details.

## Features

* #116: Added automatic schema inference for CSV files

## Dependency Updates

### Virtual Schema for Document Data in Files on AWS S3

#### Compile Dependency Updates

* Updated `com.exasol:virtual-schema-common-document-files:7.2.0` to `7.3.0`
* Removed `org.slf4j:slf4j-jdk14:2.0.7`
* Updated `software.amazon.awssdk:s3:2.20.28` to `2.20.54`

#### Runtime Dependency Updates

* Added `org.slf4j:slf4j-jdk14:2.0.7`

#### Test Dependency Updates

* Updated `com.amazonaws:aws-java-sdk-s3:1.12.431` to `1.12.457`
* Updated `com.exasol:exasol-test-setup-abstraction-java:2.0.0` to `2.0.1`
* Removed `com.exasol:exasol-testcontainers:6.5.1`
* Updated `com.exasol:hamcrest-resultset-matcher:1.5.2` to `1.6.0`
* Updated `com.exasol:small-json-files-test-fixture:0.1.4` to `0.1.5`
* Updated `com.exasol:virtual-schema-common-document-files:7.2.0` to `7.3.0`
* Updated `com.fasterxml.jackson.core:jackson-databind:2.14.2` to `2.15.0`
* Updated `org.jacoco:org.jacoco.agent:0.8.8` to `0.8.9`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.9.2` to `5.9.3`
* Updated `org.junit.jupiter:junit-jupiter-params:5.9.2` to `5.9.3`
* Updated `org.mockito:mockito-core:5.2.0` to `5.3.1`
* Updated `org.testcontainers:junit-jupiter:1.17.6` to `1.18.0`
* Updated `org.testcontainers:localstack:1.17.6` to `1.18.0`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:1.2.2` to `1.2.3`
* Updated `com.exasol:project-keeper-maven-plugin:2.9.6` to `2.9.7`
* Updated `org.apache.maven.plugins:maven-compiler-plugin:3.10.1` to `3.11.0`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.2.1` to `3.3.0`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.0.0-M8` to `3.0.0`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.0.0-M8` to `3.0.0`
* Added `org.basepom.maven:duplicate-finder-maven-plugin:1.5.1`
* Updated `org.codehaus.mojo:flatten-maven-plugin:1.3.0` to `1.4.1`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.14.2` to `2.15.0`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.8` to `0.8.9`

### Extension

#### Development Dependency Updates

* Updated `eslint:^8.34.0` to `^8.38.0`
* Updated `@typescript-eslint/parser:^5.52.0` to `^5.58.0`
* Updated `ts-jest:^29.0.5` to `^29.1.0`
* Updated `@types/jest:^29.4.0` to `^29.5.0`
* Updated `typescript:^4.9.5` to `^5.0.4`
* Updated `@typescript-eslint/eslint-plugin:^5.52.0` to `^5.58.0`
* Updated `jest:29.4.3` to `29.5.0`
* Updated `esbuild:^0.17.8` to `^0.17.16`
