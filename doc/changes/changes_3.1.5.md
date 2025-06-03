# S3 Document Files Virtual Schema 3.1.5, released 2025-06-03

Code name: Fixed vulnerabilities CVE-2025-48734, CVE-2025-4949 and CVE-2024-55551 in test dependencies

## Summary

This release is a security update. We updated the dependencies of the project to fix transitive security issues.

We also added an exception for the OSSIndex for CVE-2024-55551, which is a false positive in Exasol's JDBC driver.
This issue has been fixed quite a while back now, but the OSSIndex unfortunately does not contain the fix version of 24.2.1 (2024-12-10) set.

## Security

* #185: Fixed CVE-2025-48734 in `commons-beanutils:commons-beanutils:jar:1.9.4:test`
* #183: Fixed CVE-2025-4949 in `org.eclipse.jgit:org.eclipse.jgit:jar:6.7.0.202309050840-r:test`
* #180: Fixed CVE-2024-55551 in `com.exasol:exasol-jdbc:jar:24.2.1:test`

## Dependency Updates

### Virtual Schema for Document Data in Files on AWS S3

#### Compile Dependency Updates

* Updated `com.exasol:virtual-schema-common-document-files:8.1.6` to `8.1.7`
* Updated `software.amazon.awssdk:s3:2.30.20` to `2.31.55`

#### Runtime Dependency Updates

* Updated `org.slf4j:slf4j-jdk14:2.0.16` to `2.0.17`

#### Test Dependency Updates

* Updated `com.exasol:exasol-test-setup-abstraction-java:2.1.7` to `2.1.8`
* Updated `com.exasol:extension-manager-integration-test-java:0.5.15` to `0.5.16`
* Updated `com.exasol:hamcrest-resultset-matcher:1.7.0` to `1.7.1`
* Updated `com.exasol:performance-test-recorder-java:0.1.3` to `0.1.4`
* Updated `com.exasol:test-db-builder-java:3.6.0` to `3.6.1`
* Updated `com.exasol:udf-debugging-java:0.6.15` to `0.6.16`
* Updated `com.exasol:virtual-schema-common-document-files:8.1.6` to `8.1.7`
* Updated `com.fasterxml.jackson.core:jackson-databind:2.18.2` to `2.19.0`
* Updated `org.jacoco:org.jacoco.agent:0.8.12` to `0.8.13`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.11.4` to `5.13.0`
* Updated `org.junit.jupiter:junit-jupiter-params:5.11.4` to `5.13.0`
* Updated `org.mockito:mockito-core:5.15.2` to `5.18.0`
* Updated `org.testcontainers:junit-jupiter:1.20.4` to `1.21.1`
* Updated `org.testcontainers:localstack:1.20.4` to `1.21.1`

#### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:5.0.0` to `5.1.0`
* Added `io.github.git-commit-id:git-commit-id-maven-plugin:9.0.1`
* Removed `io.github.zlika:reproducible-build-maven-plugin:0.17`
* Added `org.apache.maven.plugins:maven-artifact-plugin:3.6.0`
* Updated `org.apache.maven.plugins:maven-clean-plugin:3.3.2` to `3.5.0`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.5.2` to `3.5.3`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.5.2` to `3.5.3`
* Updated `org.codehaus.mojo:exec-maven-plugin:3.2.0` to `3.5.0`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.12` to `0.8.13`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:5.0.0.4389` to `5.1.0.4751`

### Extension

#### Development Dependency Updates

* Updated `eslint:9.20.0` to `9.28.0`
* Updated `ts-jest:^29.2.5` to `^29.3.4`
* Updated `typescript-eslint:^8.23.0` to `^8.33.1`
* Updated `typescript:^5.7.3` to `^5.8.3`
* Updated `esbuild:^0.24.0` to `^0.25.5`
