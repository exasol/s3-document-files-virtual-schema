# S3 Document Files Virtual Schema 4.0.1, released 2026-06-12

Code name: Fix vulnerabilities in netty dependencies

## Summary

This release fixes vulnerabilities in the following dependencies:

* `io.netty:netty-codec-http2:jar:4.1.133.Final:runtime`:
  * CVE-2026-48043 (CWE-400): Uncontrolled Resource Consumption ('Resource Exhaustion') (5.3); https://guide.sonatype.com/vulnerability/CVE-2026-48043?component-type=maven&component-name=io.netty%2Fnetty-codec-http2&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
  * CVE-2026-50560 (CWE-770): Allocation of Resources Without Limits or Throttling (6.3); https://guide.sonatype.com/vulnerability/CVE-2026-50560?component-type=maven&component-name=io.netty%2Fnetty-codec-http2&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
  * CVE-2026-47244 (CWE-400): Uncontrolled Resource Consumption ('Resource Exhaustion') (5.3); https://guide.sonatype.com/vulnerability/CVE-2026-47244?component-type=maven&component-name=io.netty%2Fnetty-codec-http2&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* `io.netty:netty-handler:jar:4.1.133.Final:runtime`
  * CVE-2026-44249 (CWE-1025): Comparison Using Wrong Factors (8.1); https://guide.sonatype.com/vulnerability/CVE-2026-44249?component-type=maven&component-name=io.netty%2Fnetty-handler&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
  * CVE-2026-45416 (CWE-770): Allocation of Resources Without Limits or Throttling (7.5); https://guide.sonatype.com/vulnerability/CVE-2026-45416?component-type=maven&component-name=io.netty%2Fnetty-handler&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* `io.netty:netty-codec-http:jar:4.1.133.Final:runtime`
  * CVE-2026-42587 (CWE-400): Uncontrolled Resource Consumption ('Resource Exhaustion') (8.7); https://guide.sonatype.com/vulnerability/CVE-2026-42587?component-type=maven&component-name=io.netty%2Fnetty-codec-http&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1

## Security

* #205: Fixed vulnerabilities in netty dependencies

## Dependency Updates

### Virtual Schema for Document Data in Files on AWS S3

#### Compile Dependency Updates

* Updated `software.amazon.awssdk:s3:2.44.13` to `2.46.9`

#### Test Dependency Updates

* Updated `com.exasol:extension-manager-integration-test-java:0.5.19` to `0.5.20`
* Updated `com.exasol:performance-test-recorder-java:0.1.5` to `0.1.6`
* Updated `com.exasol:test-db-builder-java:4.0.0` to `4.0.1`
* Updated `com.fasterxml.jackson.core:jackson-databind:2.21.3` to `2.22.0`

### Extension

#### Development Dependency Updates

* Updated `eslint:10.4.0` to `10.4.1`
* Updated `typescript-eslint:^8.60.0` to `^8.61.0`
* Updated `esbuild:^0.28.0` to `^0.28.1`
