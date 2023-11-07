# S3 Document Files Virtual Schema 2.8.3, released 2023-11-07

Code name: Refactoring of Extension

## Summary

This release moves common extension code to `extension-manager-interface` to simplify the extension. We added the following optional parameters to the extension, so you can also use them with Extension Manager:

* `MAX_PARALLEL_UDFS`: Maximum number of UDFs that are executed in parallel
* `DEBUG_ADDRESS`: Network address and port to which to send debug output
* `LOG_LEVEL`: Log level for debug output

## Refactoring

* #137: Moved common extension code to `extension-manager-interface`

## Dependency Updates

### Virtual Schema for Document Data in Files on AWS S3

#### Compile Dependency Updates

* Updated `software.amazon.awssdk:s3:2.21.6` to `2.21.15`

#### Test Dependency Updates

* Updated `com.amazonaws:aws-java-sdk-s3:1.12.572` to `1.12.581`
* Updated `com.exasol:extension-manager-integration-test-java:0.5.3` to `0.5.5`
* Updated `com.exasol:hamcrest-resultset-matcher:1.6.1` to `1.6.2`
* Updated `nl.jqno.equalsverifier:equalsverifier:3.15.2` to `3.15.3`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.10.0` to `5.10.1`
* Updated `org.junit.jupiter:junit-jupiter-params:5.10.0` to `5.10.1`
* Updated `org.mockito:mockito-core:5.6.0` to `5.7.0`

#### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:2.9.14` to `2.9.15`

### Extension

#### Compile Dependency Updates

* Updated `@exasol/extension-manager-interface:0.3.0` to `0.4.0`

#### Development Dependency Updates

* Updated `eslint:^8.46.0` to `^8.53.0`
* Updated `@typescript-eslint/parser:^6.3.0` to `^6.9.1`
* Updated `@types/jest:^29.5.3` to `^29.5.7`
* Updated `typescript:^5.1.6` to `^5.2.2`
* Updated `@typescript-eslint/eslint-plugin:^6.3.0` to `^6.9.1`
* Updated `jest:29.6.2` to `29.7.0`
* Updated `esbuild:^0.19.0` to `^0.19.5`
