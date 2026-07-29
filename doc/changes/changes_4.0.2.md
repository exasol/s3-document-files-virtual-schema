# Virtual Schema for Document Data in Files on AWS S3 4.0.2, released 2026-07-29

Code name: Fixed vulnerabilities CVE-2026-59900, CVE-2026-59899, CVE-2026-59898, CVE-2026-56746, CVE-2026-56745, CVE-2026-55833, CVE-2026-55831, CVE-2026-56819, CVE-2026-59921, CVE-2026-59901, CVE-2026-9563, CVE-2026-59889, CVE-2026-54515, CVE-2017-10355, CVE-2017-7503, CVE-2026-54428, CVE-2026-54399

## Summary

This release fixes 17 vulnerabilities in dependencies.

## Security

* #223: Fixed CVE-2026-59900 in `io.netty:netty-codec-http2:jar:4.1.135.Final:runtime`
* #222: Fixed CVE-2026-59899 in `io.netty:netty-codec-http:jar:4.1.135.Final:runtime`
* #221: Fixed CVE-2026-59898 in `io.netty:netty-codec-http:jar:4.1.135.Final:runtime`
* #220: Fixed CVE-2026-56746 in `io.netty:netty-codec-http:jar:4.1.135.Final:runtime`
* #219: Fixed CVE-2026-56745 in `io.netty:netty-codec-http:jar:4.1.135.Final:runtime`
* #218: Fixed CVE-2026-55833 in `io.netty:netty-codec-http:jar:4.1.135.Final:runtime`
* #217: Fixed CVE-2026-55831 in `io.netty:netty-codec-http:jar:4.1.135.Final:runtime`
* #216: Fixed CVE-2026-56819 in `io.netty:netty-codec-http2:jar:4.1.135.Final:runtime`
* #215: Fixed CVE-2026-59921 in `io.netty:netty-codec-http:jar:4.1.135.Final:runtime`
* #214: Fixed CVE-2026-59901 in `io.netty:netty-codec:jar:4.1.135.Final:runtime`
* #213: Fixed CVE-2026-9563 in `org.eclipse.parsson:parsson:jar:1.1.7:test`
* #212: Fixed CVE-2026-59889 in `com.fasterxml.jackson.core:jackson-databind:jar:2.22.0:test`
* #211: Fixed CVE-2026-54515 in `com.fasterxml.jackson.core:jackson-databind:jar:2.22.0:test`
* #210: Fixed CVE-2017-10355 in `xerces:xercesImpl:jar:2.12.2:test`
* #209: Fixed CVE-2017-7503 in `xerces:xercesImpl:jar:2.12.2:test`
* #208: Fixed CVE-2026-54428 in `org.apache.httpcomponents.core5:httpcore5-h2:jar:5.4:runtime`
* #207: Fixed CVE-2026-54399 in `org.apache.httpcomponents.core5:httpcore5:jar:5.4.2:runtime`


## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:virtual-schema-common-document-files:9.0.1` to `9.0.2`
* Updated `software.amazon.awssdk:s3:2.46.9` to `2.49.5`

### Test Dependency Updates

* Updated `com.exasol:exasol-test-setup-abstraction-java:2.1.11` to `3.0.0`
* Removed `com.exasol:extension-manager-integration-test-java:0.5.20`
* Updated `com.exasol:hamcrest-resultset-matcher:1.7.2` to `1.7.3`
* Updated `com.exasol:small-json-files-test-fixture:0.1.14` to `0.1.15`
* Updated `com.exasol:test-db-builder-java:4.0.1` to `4.0.2`
* Updated `com.exasol:udf-debugging-java:0.6.18` to `0.6.20`
* Updated `com.exasol:virtual-schema-common-document-files:9.0.1` to `9.0.2`
* Removed `com.fasterxml.jackson.core:jackson-databind:2.22.0`
* Updated `org.jacoco:org.jacoco.agent:0.8.14` to `0.8.15`

### Plugin Dependency Updates

* Updated `com.exasol:artifact-reference-checker-maven-plugin:0.4.4` to `1.0.1`
* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.7` to `2.1.0`
* Updated `com.exasol:project-keeper-maven-plugin:5.6.2` to `5.7.4`
* Removed `com.exasol:quality-summarizer-maven-plugin:0.2.1`
* Updated `org.apache.maven.plugins:maven-dependency-plugin:3.10.0` to `3.11.0`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.6.2` to `3.6.3`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.5.5` to `3.5.6`
* Updated `org.apache.maven.plugins:maven-site-plugin:3.21.0` to `3.22.0`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.5.5` to `3.5.6`
* Removed `org.codehaus.mojo:exec-maven-plugin:3.6.3`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.14` to `0.8.15`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:5.5.0.6356` to `5.7.0.6970`
* Added `org.spdx:spdx-maven-plugin:1.0.4`
