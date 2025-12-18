# S3 Document Files Virtual Schema 3.1.10, released 2025-12-18

Code name: Update AWS SDK

## Summary

This release updates the AWS SDK to 2.40.11

## Dependencies

* #199: Update AWS SDK

## Dependency Updates

### Virtual Schema for Document Data in Files on AWS S3

#### Compile Dependency Updates

* Updated `com.exasol:error-reporting-java:1.0.1` to `1.0.2`
* Updated `software.amazon.awssdk:s3:2.34.0` to `2.40.11`

#### Test Dependency Updates

* Updated `com.exasol:exasol-test-setup-abstraction-java:2.1.9` to `2.1.10`
* Updated `com.exasol:maven-project-version-getter:1.2.1` to `1.2.2`
* Updated `com.exasol:small-json-files-test-fixture:0.1.13` to `0.1.14`
* Updated `com.exasol:test-db-builder-java:3.6.3` to `3.6.4`
* Updated `com.exasol:udf-debugging-java:0.6.17` to `0.6.18`
* Updated `com.fasterxml.jackson.core:jackson-databind:2.20.0` to `2.20.1`
* Added `org.apache.commons:commons-text:1.14.0`
* Updated `org.jacoco:org.jacoco.agent:0.8.13` to `0.8.14`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.13.4` to `5.14.1`
* Updated `org.junit.jupiter:junit-jupiter-params:5.13.4` to `5.14.1`
* Updated `org.mockito:mockito-core:5.20.0` to `5.21.0`
* Updated `org.testcontainers:junit-jupiter:1.21.3` to `1.21.4`
* Updated `org.testcontainers:localstack:1.21.3` to `1.21.4`

#### Plugin Dependency Updates

* Updated `com.exasol:artifact-reference-checker-maven-plugin:0.4.3` to `0.4.4`
* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.4` to `2.0.5`
* Updated `com.exasol:project-keeper-maven-plugin:5.2.3` to `5.4.4`
* Updated `com.exasol:quality-summarizer-maven-plugin:0.2.0` to `0.2.1`
* Updated `io.github.git-commit-id:git-commit-id-maven-plugin:9.0.1` to `9.0.2`
* Updated `org.apache.maven.plugins:maven-artifact-plugin:3.6.0` to `3.6.1`
* Updated `org.apache.maven.plugins:maven-assembly-plugin:3.7.1` to `3.8.0`
* Updated `org.apache.maven.plugins:maven-compiler-plugin:3.14.0` to `3.14.1`
* Updated `org.apache.maven.plugins:maven-dependency-plugin:3.8.1` to `3.9.0`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.5.0` to `3.6.2`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.5.3` to `3.5.4`
* Updated `org.apache.maven.plugins:maven-jar-plugin:3.4.2` to `3.5.0`
* Updated `org.apache.maven.plugins:maven-resources-plugin:3.3.1` to `3.4.0`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.5.3` to `3.5.4`
* Updated `org.codehaus.mojo:flatten-maven-plugin:1.7.0` to `1.7.3`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.18.0` to `2.20.1`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.13` to `0.8.14`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:5.1.0.4751` to `5.5.0.6356`
