# S3 Document Files Virtual Schema 2.6.1, released 2023-05-08

Code name: CSV Performance Regression Tests

## Summary

This release updates performance regression rests for CSV files to use all data types (string, boolean, integer, double, date and timestamp) instead of only string. Please note that this might influence comparability of test results. Additionally the test names in the test report changed. They now use suffix `()` instead of `(TestInfo)`.

## Tests

* #118: Added CSV data type performance regression tests

## Dependency Updates

### Virtual Schema for Document Data in Files on AWS S3

#### Compile Dependency Updates

* Updated `com.exasol:virtual-schema-common-document-files:7.3.0` to `7.3.2`
* Updated `software.amazon.awssdk:s3:2.20.54` to `2.20.61`

#### Test Dependency Updates

* Updated `com.amazonaws:aws-java-sdk-s3:1.12.457` to `1.12.464`
* Updated `com.exasol:small-json-files-test-fixture:0.1.5` to `0.1.6`
* Updated `com.exasol:virtual-schema-common-document-files:7.3.0` to `7.3.2`
