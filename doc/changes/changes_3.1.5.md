# S3 Document Files Virtual Schema 3.1.5, released 2025-??-??

Code name: Fixed vulnerability CVE-2025-4949 in org.eclipse.jgit:org.eclipse.jgit:jar:6.7.0.202309050840-r:test

## Summary

This release fixes the following vulnerability:

### CVE-2025-4949 (CWE-611) in dependency `org.eclipse.jgit:org.eclipse.jgit:jar:6.7.0.202309050840-r:test`
Eclipse JGit - Improper Restriction of XML External Entity Reference
#### References
* https://ossindex.sonatype.org/vulnerability/CVE-2025-4949?component-type=maven&component-name=org.eclipse.jgit%2Forg.eclipse.jgit&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2025-4949
* https://gitlab.eclipse.org/security/cve-assignement/-/issues/64
* https://gitlab.eclipse.org/security/vulnerability-reports/-/issues/281

## Security

* #183: Fixed vulnerability CVE-2025-4949 in dependency `org.eclipse.jgit:org.eclipse.jgit:jar:6.7.0.202309050840-r:test`

## Dependency Updates

### Virtual Schema for Document Data in Files on AWS S3

#### Compile Dependency Updates

* Updated `software.amazon.awssdk:s3:2.30.20` to `2.31.48`

#### Runtime Dependency Updates

* Updated `org.slf4j:slf4j-jdk14:2.0.16` to `2.0.17`

#### Test Dependency Updates

* Updated `com.exasol:extension-manager-integration-test-java:0.5.15` to `0.5.16`
* Updated `com.exasol:test-db-builder-java:3.6.0` to `3.6.1`
* Updated `com.fasterxml.jackson.core:jackson-databind:2.18.2` to `2.19.0`
* Updated `nl.jqno.equalsverifier:equalsverifier:3.19` to `4.0`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.11.4` to `5.12.2`
* Updated `org.junit.jupiter:junit-jupiter-params:5.11.4` to `5.12.2`
* Updated `org.mockito:mockito-core:5.15.2` to `5.18.0`
* Updated `org.testcontainers:junit-jupiter:1.20.4` to `1.21.0`
* Updated `org.testcontainers:localstack:1.20.4` to `1.21.0`

#### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:5.0.0` to `5.1.0`
