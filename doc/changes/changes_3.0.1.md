# S3 Document Files Virtual Schema 3.0.1, released 2024-01-18

Code name: Fix CVE-2024-21634 in test dependency `software.amazon.ion:ion-java`

## Summary

This release fixes CVE-2024-21634 (CWE-770: Allocation of Resources Without Limits or Throttling (7.5)) in test dependency `software.amazon.ion:ion-java`.

## Security

* #144: Fixed CVE-2024-21634 in test dependency `software.amazon.ion:ion-java`

## Dependency Updates

### Virtual Schema for Document Data in Files on AWS S3

#### Compile Dependency Updates

* Updated `software.amazon.awssdk:s3:2.21.44` to `2.23.9`

#### Runtime Dependency Updates

* Updated `org.slf4j:slf4j-jdk14:2.0.9` to `2.0.11`

#### Test Dependency Updates

* Updated `com.amazonaws:aws-java-sdk-s3:1.12.613` to `1.12.643`
* Updated `com.exasol:hamcrest-resultset-matcher:1.6.3` to `1.6.4`
* Updated `com.exasol:small-json-files-test-fixture:0.1.8` to `0.1.9`
* Updated `com.fasterxml.jackson.core:jackson-databind:2.16.0` to `2.16.1`
* Updated `nl.jqno.equalsverifier:equalsverifier:3.15.4` to `3.15.6`
* Updated `org.mockito:mockito-core:5.8.0` to `5.9.0`

#### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:2.9.17` to `3.0.0`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.2.2` to `3.2.3`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.2.2` to `3.2.3`
* Added `org.apache.maven.plugins:maven-toolchains-plugin:3.1.0`
