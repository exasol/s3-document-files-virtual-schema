# S3 Document Files Virtual Schema 3.0.4, released 2024-04-09

Code name: Fixed vulnerabilities CVE-2024-29131, CVE-2024-29133 and CVE-2024-29025

## Summary

This release fixes the following three vulnerability:

### CVE-2024-29025 (CWE-770) in dependency `io.netty:netty-codec-http:jar:4.1.107.Final:runtime`
Netty is an asynchronous event-driven network application framework for rapid development of maintainable high performance protocol servers & clients. The `HttpPostRequestDecoder` can be tricked to accumulate data. While the decoder can store items on the disk if configured so, there are no limits to the number of fields the form can have, an attacher can send a chunked post consisting of many small fields that will be accumulated in the `bodyListHttpData` list. The decoder cumulates bytes in the `undecodedChunk` buffer until it can decode a field, this field can cumulate data without limits. This vulnerability is fixed in 4.1.108.Final.
#### References
* https://ossindex.sonatype.org/vulnerability/CVE-2024-29025?component-type=maven&component-name=io.netty%2Fnetty-codec-http&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2024-29025
* https://github.com/advisories/GHSA-5jpm-x58v-624v

### CVE-2024-29131 (CWE-787) in dependency `org.apache.commons:commons-configuration2:jar:2.8.0:compile`
Out-of-bounds Write vulnerability in Apache Commons Configuration.This issue affects Apache Commons Configuration: from 2.0 before 2.10.1.

Users are recommended to upgrade to version 2.10.1, which fixes the issue.

#### References
* https://ossindex.sonatype.org/vulnerability/CVE-2024-29131?component-type=maven&component-name=org.apache.commons%2Fcommons-configuration2&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2024-29131
* https://lists.apache.org/thread/03nzzzjn4oknyw5y0871tw7ltj0t3r37
* https://github.com/advisories/GHSA-xjp4-hw94-mvp5

### CVE-2024-29133 (CWE-787) in dependency `org.apache.commons:commons-configuration2:jar:2.8.0:compile`
Out-of-bounds Write vulnerability in Apache Commons Configuration.This issue affects Apache Commons Configuration: from 2.0 before 2.10.1.

Users are recommended to upgrade to version 2.10.1, which fixes the issue.

#### References
* https://ossindex.sonatype.org/vulnerability/CVE-2024-29133?component-type=maven&component-name=org.apache.commons%2Fcommons-configuration2&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2024-29133
* https://lists.apache.org/thread/ccb9w15bscznh6tnp3wsvrrj9crbszh2

## Security

* #154: Fixed vulnerability CVE-2024-29131 in dependency `org.apache.commons:commons-configuration2:jar:2.8.0:compile`
* #155: Fixed vulnerability CVE-2024-29133 in dependency `org.apache.commons:commons-configuration2:jar:2.8.0:compile`
* #157: Fixed vulnerability CVE-2024-29025 in dependency `io.netty:netty-codec-http:jar:4.1.107.Final:runtime`

## Dependency Updates

### Virtual Schema for Document Data in Files on AWS S3

#### Compile Dependency Updates

* Updated `com.exasol:virtual-schema-common-document-files:8.0.2` to `8.0.3`
* Updated `software.amazon.awssdk:s3:2.25.8` to `2.25.27`

#### Test Dependency Updates

* Updated `com.amazonaws:aws-java-sdk-s3:1.12.678` to `1.12.697`
* Updated `com.exasol:exasol-test-setup-abstraction-java:2.1.1` to `2.1.2`
* Updated `com.exasol:virtual-schema-common-document-files:8.0.2` to `8.0.3`
* Updated `nl.jqno.equalsverifier:equalsverifier:3.15.8` to `3.16.1`

#### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.0.0` to `2.0.2`
* Updated `com.exasol:project-keeper-maven-plugin:4.1.0` to `4.3.0`
* Updated `org.apache.maven.plugins:maven-assembly-plugin:3.6.0` to `3.7.1`
* Updated `org.apache.maven.plugins:maven-compiler-plugin:3.12.1` to `3.13.0`
* Updated `org.codehaus.mojo:exec-maven-plugin:3.1.1` to `3.2.0`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.11` to `0.8.12`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:3.10.0.2594` to `3.11.0.3922`

### Extension

#### Development Dependency Updates

* Updated `eslint:^8.56.0` to `^8.57.0`
* Updated `@types/jest:^29.5.11` to `^29.5.12`
* Added `typescript-eslint:^7.5.0`
* Updated `typescript:^5.3.3` to `^5.4.4`
* Updated `esbuild:^0.19.12` to `^0.20.2`
* Removed `@typescript-eslint/parser:^6.19.1`
* Removed `@typescript-eslint/eslint-plugin:^6.19.1`
