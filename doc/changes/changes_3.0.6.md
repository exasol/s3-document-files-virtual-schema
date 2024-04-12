# S3 Document Files Virtual Schema 3.0.6, released 2024-??-??

Code name: Fixed vulnerability CVE-2024-23080 in joda-time:joda-time:jar:2.8.1:test

## Summary

This release fixes the following vulnerability:

### CVE-2024-23080 (CWE-476) in dependency `joda-time:joda-time:jar:2.8.1:test`
Joda Time v2.12.5 was discovered to contain a NullPointerException via the component org.joda.time.format.PeriodFormat::wordBased(Locale).
#### References
* https://ossindex.sonatype.org/vulnerability/CVE-2024-23080?component-type=maven&component-name=joda-time%2Fjoda-time&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2024-23080
* https://github.com/advisories/GHSA-gxgx-2mvf-9gh5

## Security

* #161: Fixed vulnerability CVE-2024-23080 in dependency `joda-time:joda-time:jar:2.8.1:test`

## Dependency Updates

### Virtual Schema for Document Data in Files on AWS S3

#### Compile Dependency Updates

* Updated `software.amazon.awssdk:s3:2.25.27` to `2.25.30`

#### Test Dependency Updates

* Updated `com.amazonaws:aws-java-sdk-s3:1.12.697` to `1.12.700`
* Updated `com.exasol:extension-manager-integration-test-java:0.5.8` to `0.5.9`
* Updated `com.exasol:small-json-files-test-fixture:0.1.9` to `0.1.10`
* Updated `com.exasol:udf-debugging-java:0.6.12` to `0.6.13`
