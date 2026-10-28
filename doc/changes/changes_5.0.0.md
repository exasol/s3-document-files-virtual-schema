# Virtual Schema for Document Data in Files on AWS S3 5.0.0, released 2026-10-??

Code name: Secure foundations

## Summary

This release requires Java 17 and no longer supports Exasol 8. It also updates vulnerable dependencies.

## Breaking Changes

* The project now requires Java 17 and no longer supports Exasol 8.

## Security

* #262: Fixed CVE-2026-104880 (CSV interpretation conflict) in `de.siegmar:fastcsv:3.7.0`.
* Fixed CVE-2026-116486 (symbolic name mapping error) in `org.objenesis:objenesis:3.4`.
* #268: Fixed CVE-2026-110262 (excessive resource allocation) in `org.ow2.asm:asm-commons:9.10.1`.
* #269: Fixed CVE-2026-110270 (algorithmic complexity) in `org.ow2.asm:asm-commons:9.10.1`.
* #270: Fixed CVE-2026-110257 (uncontrolled memory allocation) in `org.ow2.asm:asm:9.10.1`.
* #271: Fixed CVE-2026-110258 (uncontrolled memory allocation) in `org.ow2.asm:asm:9.10.1`.
* #272: Fixed CVE-2026-110259 (uncontrolled recursion) in `org.ow2.asm:asm:9.10.1`.
* #273: Fixed CVE-2026-110260 (algorithmic complexity) in `org.ow2.asm:asm:9.10.1`.
* #274: Fixed CVE-2026-110261 (algorithmic complexity) in `org.ow2.asm:asm:9.10.1`.
* #275: Fixed CVE-2026-110263 (uncontrolled recursion) in `org.ow2.asm:asm:9.10.1`.
* #236/#240: Fixed CVE-2026-89044 (HTTP request smuggling) in `io.netty:netty-codec-http2:4.1.137.Final` and `io.netty:netty-codec-http:4.1.137.Final`.
* #246: Fixed CVE-2026-93492 (unspecified vulnerability) in `io.netty:netty-codec-http2:4.1.137.Final`.
* #247: Fixed CVE-2026-93567 (improper input validation) in `io.netty:netty-codec-http2:4.1.137.Final`.
* #248: Fixed CVE-2026-93568 (improper input validation) in `io.netty:netty-codec-http2:4.1.137.Final`.
* #249: Fixed CVE-2026-93579 (unspecified vulnerability) in `io.netty:netty-codec-http2:4.1.137.Final`.
* #253: Fixed CVE-2026-93488 (resource allocation without limits) in `io.netty:netty-codec-http:4.1.137.Final`.
* #254: Fixed CVE-2026-93491 (resource allocation without limits) in `io.netty:netty-codec-http:4.1.137.Final`.
* #255: Fixed CVE-2026-93558 (unspecified vulnerability) in `io.netty:netty-codec-http:4.1.137.Final`.
* #256: Fixed CVE-2026-93562 (unspecified vulnerability) in `io.netty:netty-codec-http:4.1.137.Final`.
* #257: Fixed CVE-2026-93565 (unspecified vulnerability) in `io.netty:netty-codec-http:4.1.137.Final`.
* #258: Fixed CVE-2026-93574 (HTTP request smuggling) in `io.netty:netty-codec-http:4.1.137.Final`.
* #259: Fixed CVE-2026-93566 (HTTP request smuggling) in `io.netty:netty-codec-http:4.1.137.Final`.
* #260: Fixed CVE-2026-93573 (interpretation conflict) in `io.netty:netty-codec-http:4.1.137.Final`.
* #234: Fixed CVE-2026-73334 (improper input validation) in `org.apache.parquet:parquet-hadoop:1.17.1`.
* #237: Fixed CVE-2026-87795 (out-of-bounds read), CVE-2026-87823 (integer overflow), and CVE-2026-89045 (infinite loop) in `com.github.luben:zstd-jni:1.5.7-3`.
* #264: Fixed CVE-2026-108104 (double free), CVE-2026-108106 (uncontrolled memory allocation), #242: CVE-2026-90559, #244: CVE-2026-93451, and #245: CVE-2026-93452 (out-of-bounds write) in `org.xerial.snappy:snappy-java:1.1.10.7`.
* #232: Fixed CVE-2026-86231 (improper certificate validation) in `com.github.mwiede:jsch:2.28.4`.
* #230: Fixed CVE-2026-64607 (connection not released) in `org.apache.httpcomponents.client5:httpclient5:5.6.2`.
* Fixed CVE-2026-71290 (improper certificate validation) in `org.apache.httpcomponents.client5:httpclient5:5.6.2`.

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:virtual-schema-common-document-files:9.1.0` to `10.0.1`
* Updated `software.amazon.awssdk:s3:2.51.3` to `2.57.5`

### Runtime Dependency Updates

* Updated `org.slf4j:slf4j-jdk14:2.0.18` to `2.0.20`

### Test Dependency Updates

* Updated `com.exasol:exasol-test-setup-abstraction-java:3.0.0` to `3.0.1`
* Updated `com.exasol:hamcrest-resultset-matcher:1.7.3` to `1.7.4`
* Updated `com.exasol:small-json-files-test-fixture:0.1.15` to `0.1.16`
* Updated `com.exasol:test-db-builder-java:4.0.2` to `4.0.3`
* Updated `com.exasol:udf-debugging-java:0.6.20` to `0.6.22`
* Updated `com.exasol:virtual-schema-common-document-files:9.1.0` to `10.0.1`
* Updated `nl.jqno.equalsverifier:equalsverifier:3.19.4` to `4.5.2`
* Removed `org.junit.jupiter:junit-jupiter-params:5.14.4`
* Added `org.junit.jupiter:junit-jupiter:6.1.3`
* Updated `org.mockito:mockito-core:5.23.0` to `5.24.0`

### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.1.0` to `2.1.2`
* Updated `com.exasol:project-keeper-maven-plugin:5.7.4` to `5.8.1`
* Updated `io.github.git-commit-id:git-commit-id-maven-plugin:10.0.0` to `10.0.1`
* Updated `org.apache.maven.plugins:maven-artifact-plugin:3.6.1` to `3.7.0`
* Updated `org.apache.maven.plugins:maven-compiler-plugin:3.15.0` to `3.16.0`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.5.6` to `3.6.0`
* Updated `org.apache.maven.plugins:maven-install-plugin:3.1.4` to `3.2.0`
* Updated `org.apache.maven.plugins:maven-jar-plugin:3.5.0` to `3.5.1`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.5.6` to `3.6.0`
* Updated `org.apache.maven.plugins:maven-toolchains-plugin:3.2.0` to `3.3.0`
* Updated `org.codehaus.mojo:flatten-maven-plugin:1.7.3` to `1.8.0`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.21.0` to `2.23.0`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:5.7.0.6970` to `5.8.0.7211`
