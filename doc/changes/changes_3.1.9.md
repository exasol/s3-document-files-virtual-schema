# S3 Document Files Virtual Schema 3.1.9, released 2025-??-??

Code name: Fixed vulnerability CVE-2025-58057 in io.netty:netty-codec:jar:4.1.124.Final:runtime

## Summary

This release fixes the following vulnerability:

### CVE-2025-58057 (CWE-409) in dependency `io.netty:netty-codec:jar:4.1.124.Final:runtime`
netty-codec - Improper Handling of Highly Compressed Data (Data Amplification)
#### References
* https://ossindex.sonatype.org/vulnerability/CVE-2025-58057?component-type=maven&component-name=io.netty%2Fnetty-codec&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2025-58057
* https://github.com/netty/netty/security/advisories/GHSA-3p8m-j85q-pgmj

## Security

* #195: Fixed vulnerability CVE-2025-58057 in dependency `io.netty:netty-codec:jar:4.1.124.Final:runtime`

## Dependency Updates

### Virtual Schema for Document Data in Files on AWS S3

#### Compile Dependency Updates

* Updated `com.exasol:virtual-schema-common-document-files:8.1.11` to `8.1.12`
* Updated `software.amazon.awssdk:s3:2.31.55` to `2.34.0`

#### Test Dependency Updates

* Updated `com.exasol:exasol-test-setup-abstraction-java:2.1.8` to `2.1.9`
* Updated `com.exasol:hamcrest-resultset-matcher:1.7.1` to `1.7.2`
* Updated `com.exasol:performance-test-recorder-java:0.1.4` to `0.1.5`
* Updated `com.exasol:small-json-files-test-fixture:0.1.12` to `0.1.13`
* Updated `com.exasol:test-db-builder-java:3.6.1` to `3.6.3`
* Updated `com.exasol:virtual-schema-common-document-files:8.1.11` to `8.1.12`
* Updated `com.fasterxml.jackson.core:jackson-databind:2.19.0` to `2.20.0`
* Updated `nl.jqno.equalsverifier:equalsverifier:3.19` to `3.19.4`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.13.0` to `5.13.4`
* Updated `org.junit.jupiter:junit-jupiter-params:5.13.0` to `5.13.4`
* Updated `org.mockito:mockito-core:5.18.0` to `5.20.0`
* Updated `org.testcontainers:junit-jupiter:1.21.1` to `1.21.3`
* Updated `org.testcontainers:localstack:1.21.1` to `1.21.3`
