# Virtual Schema for document data in files on AWS S3 1.5.0, released 2021-11-23

Code name: Improved Parquet Loading Performance

## Summary

In this release, we updated the base of this implementation (virtual-schema-common-document-files). By that, we improved the performance for loading parquet files. The improvements will give you a speed-up when you load a few (<200) files.

## Features

* #48: Added regression test for many small JSON files
* #52: Added performance test for few big Parquet files

## Bug Fixes

* #47: Fixed upload of regression test results

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:virtual-schema-common-document-files:2.2.1` to `3.0.0`

### Test Dependency Updates

* Added `com.exasol:small-json-files-test-fixture:0.1.1`
* Updated `com.exasol:virtual-schema-common-document-files:2.2.1` to `3.0.0`
* Added `org.yaml:snakeyaml:1.29`

### Plugin Dependency Updates

* Added `org.projectlombok:lombok-maven-plugin:1.18.20.0`
