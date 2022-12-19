# Virtual Schema for document data in files on AWS S3 2.4.2, released 2022-22-12-19

Code name: Dependency Upgrade

## Summary

Fixed vulnerabilities
* CVE-2022-40152 by upgrading `com.fasterxml.woodstox:woodstox-core`, required by `exasol:parquet-io-java` / hadoop
* CVE-2022-41915 by upgrading `io.netty:netty-codec`, required by awssdk
* CVE-2021-37533 by upgrading `commons-net`, required by `exasol:parquet-io-java` / hadoop

## Bugfixes

* #103: Fixed vulnerabilities

## Dependency Updates

### Compile Dependency Updates

* Updated `ch.qos.reload4j:reload4j:1.2.22` to `1.2.24`
* Updated `com.exasol:virtual-schema-common-document-files:7.1.1` to `7.1.2`
* Added `com.fasterxml.woodstox:woodstox-core:5.4.0`
* Added `commons-net:commons-net:3.9.0`
* Added `io.netty:netty-codec:4.1.86.Final`
* Updated `org.eclipse.jetty:jetty-client:9.4.48.v20220622` to `11.0.13`
* Updated `org.slf4j:slf4j-jdk14:1.7.36` to `2.0.6`
* Updated `software.amazon.awssdk:s3:2.17.293` to `2.18.3`

### Test Dependency Updates

* Updated `com.amazonaws:aws-java-sdk-s3:1.12.322` to `1.12.367`
* Updated `com.exasol:bucketfs-java:2.4.0` to `2.5.0`
* Updated `com.exasol:exasol-test-setup-abstraction-java:0.3.2` to `1.1.0`
* Updated `com.exasol:exasol-testcontainers:6.2.0` to `6.4.0`
* Updated `com.exasol:extension-manager-integration-test-java:0.1.0` to `0.2.0`
* Updated `com.exasol:java-class-list-verifier:0.2.0` to `0.2.1`
* Updated `com.exasol:performance-test-recorder-java:0.1.1` to `0.1.2`
* Updated `com.exasol:test-db-builder-java:3.4.0` to `3.4.1`
* Updated `com.exasol:udf-debugging-java:0.6.4` to `0.6.5`
* Updated `com.exasol:virtual-schema-common-document-files:7.1.1` to `7.1.2`
* Updated `com.fasterxml.jackson.core:jackson-databind:2.14.0-rc1` to `2.14.1`
* Added `nl.jqno.equalsverifier:equalsverifier:3.12.3`
* Updated `org.mockito:mockito-core:4.8.0` to `4.10.0`
* Updated `org.testcontainers:junit-jupiter:1.17.5` to `1.17.6`
* Updated `org.testcontainers:localstack:1.17.5` to `1.17.6`

### Plugin Dependency Updates

* Updated `com.exasol:artifact-reference-checker-maven-plugin:0.4.0` to `0.4.2`
* Updated `com.exasol:error-code-crawler-maven-plugin:1.1.2` to `1.2.1`
* Updated `com.exasol:project-keeper-maven-plugin:2.8.0` to `2.9.1`
* Updated `io.github.zlika:reproducible-build-maven-plugin:0.15` to `0.16`
* Updated `org.apache.maven.plugins:maven-assembly-plugin:3.3.0` to `3.4.2`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.0.0-M5` to `3.0.0-M7`
* Updated `org.apache.maven.plugins:maven-jar-plugin:3.2.2` to `3.3.0`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.0.0-M5` to `3.0.0-M7`
* Updated `org.codehaus.mojo:flatten-maven-plugin:1.2.7` to `1.3.0`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.10.0` to `2.13.0`
* Removed `org.projectlombok:lombok-maven-plugin:1.18.20.0`
