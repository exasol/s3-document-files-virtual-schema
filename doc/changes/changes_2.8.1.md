# S3 Document Files Virtual Schema 2.8.1, released 2023-09-28

Code name: Fix vulnerabilities in dependencies

## Summary

This release fixes the following vulnerabilities in dependencies:

* `org.apache.commons:commons-compress:compile`: CVE-2023-42503 CWE-20: Improper Input Validation (5.5)
* `org.xerial.snappy:snappy-java:compile`: CVE-2023-43642 CWE-770: Allocation of Resources Without Limits or Throttling (7.5)
* `org.eclipse.jgit:org.eclipse.jgit:test`: CVE-2023-4759: CWE-178: Improper Handling of Case Sensitivity (8.8)

**Known issue:** Transitive dependency `io.netty:netty-handler` of `software.amazon.awssdk:s3` contains vulnerability CVE-2023-4586 (CWE-300: Channel Accessible by Non-Endpoint ('Man-in-the-Middle') (6.5)). We assume that the AWS client's usage of `netty-handler` is not affected by the vulnerability.

## Security

* #135: Fix vulnerabilities in dependencies

## Documentation

* #132: Fixed wording in hands-on-guide for MinIO

## Dependency Updates

### Virtual Schema for Document Data in Files on AWS S3

#### Compile Dependency Updates

* Updated `com.exasol:virtual-schema-common-document-files:7.3.3` to `7.3.4`
* Updated `software.amazon.awssdk:s3:2.20.122` to `2.20.156`

#### Runtime Dependency Updates

* Updated `org.slf4j:slf4j-jdk14:2.0.7` to `2.0.9`

#### Test Dependency Updates

* Updated `com.amazonaws:aws-java-sdk-s3:1.12.525` to `1.12.559`
* Updated `com.exasol:exasol-test-setup-abstraction-java:2.0.2` to `2.0.4`
* Updated `com.exasol:extension-manager-integration-test-java:0.5.0` to `0.5.1`
* Updated `com.exasol:hamcrest-resultset-matcher:1.6.0` to `1.6.1`
* Updated `com.exasol:java-class-list-verifier:0.2.4` to `0.2.5`
* Updated `com.exasol:performance-test-recorder-java:0.1.2` to `0.1.3`
* Updated `com.exasol:test-db-builder-java:3.4.2` to `3.5.1`
* Updated `com.exasol:udf-debugging-java:0.6.10` to `0.6.11`
* Updated `com.exasol:virtual-schema-common-document-files:7.3.3` to `7.3.4`
* Updated `nl.jqno.equalsverifier:equalsverifier:3.15.1` to `3.15.2`
* Updated `org.mockito:mockito-core:5.4.0` to `5.5.0`
* Updated `org.testcontainers:junit-jupiter:1.18.3` to `1.19.0`
* Updated `org.testcontainers:localstack:1.18.3` to `1.19.0`

#### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:2.9.10` to `2.9.12`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.3.0` to `3.4.0`
