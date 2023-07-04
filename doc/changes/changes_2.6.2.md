# S3 Document Files Virtual Schema 2.6.2, released 2023-07-04

Code name: Update dependencies on top of 2.6.1

## Summary

This release upgrades dependencies to fix the following vulnerabilities:

* `io.netty:netty-handler` (runtime dependency)
  * CVE-2023-34462, severity CWE-770: Allocation of Resources Without Limits or Throttling (6.5)
* `org.xerial.snappy:snappy-java` (compile dependency)
  * CVE-2023-34453, severity CWE-190: Integer Overflow or Wraparound (7.5)
  * CVE-2023-34454, severity CWE-190: Integer Overflow or Wraparound (7.5)
  * CVE-2023-34455, severity CWE-770: Allocation of Resources Without Limits or Throttling (7.5)

## Security

* #122: Updated dependencies

## Dependency Updates

### Virtual Schema for Document Data in Files on AWS S3

#### Compile Dependency Updates

* Updated `com.exasol:virtual-schema-common-document-files:7.3.2` to `7.3.3`
* Updated `software.amazon.awssdk:s3:2.20.61` to `2.20.98`

#### Test Dependency Updates

* Updated `com.amazonaws:aws-java-sdk-s3:1.12.464` to `1.12.501`
* Updated `com.exasol:exasol-test-setup-abstraction-java:2.0.1` to `2.0.2`
* Updated `com.exasol:extension-manager-integration-test-java:0.2.2` to `0.4.0`
* Updated `com.exasol:small-json-files-test-fixture:0.1.6` to `0.1.7`
* Updated `com.exasol:udf-debugging-java:0.6.8` to `0.6.9`
* Updated `com.exasol:virtual-schema-common-document-files:7.3.2` to `7.3.3`
* Updated `com.fasterxml.jackson.core:jackson-databind:2.15.0` to `2.15.2`
* Updated `nl.jqno.equalsverifier:equalsverifier:3.14.1` to `3.14.3`
* Updated `org.mockito:mockito-core:5.3.1` to `5.4.0`
* Updated `org.testcontainers:junit-jupiter:1.18.0` to `1.18.3`
* Updated `org.testcontainers:localstack:1.18.0` to `1.18.3`
