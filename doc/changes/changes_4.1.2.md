# Virtual Schema for Document Data in Files on AWS S3 4.1.2, released 2026-??-??

Code name: Fixed vulnerability CVE-2026-90559 in org.xerial.snappy:snappy-java:jar:1.1.10.7:compile

## Summary

This release fixes the following vulnerability:

### CVE-2026-90559 (CWE-787) in dependency `org.xerial.snappy:snappy-java:jar:1.1.10.7:compile`
snappy-java through 1.1.10.8 contains an out-of-bounds write vulnerability in Snappy.uncompress(ByteBuffer, ByteBuffer) because destination buffer capacity is never validated against decompressed size. Attackers can supply valid compressed data that decompresses larger than the destination buffer, causing writes past buffer boundaries and JVM termination.
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-90559?component-type=maven&component-name=org.xerial.snappy%2Fsnappy-java&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-90559
* https://github.com/xerial/snappy-java/issues/728

## Security

* #242: Fixed vulnerability CVE-2026-90559 in dependency `org.xerial.snappy:snappy-java:jar:1.1.10.7:compile`

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:virtual-schema-common-document-files:9.1.0` to `9.1.1`
* Updated `software.amazon.awssdk:s3:2.51.3` to `2.55.1`

### Runtime Dependency Updates

* Updated `org.slf4j:slf4j-jdk14:2.0.18` to `2.0.19`

### Test Dependency Updates

* Updated `com.exasol:exasol-test-setup-abstraction-java:3.0.0` to `3.0.1`
* Updated `com.exasol:hamcrest-resultset-matcher:1.7.3` to `1.7.4`
* Updated `com.exasol:small-json-files-test-fixture:0.1.15` to `0.1.16`
* Updated `com.exasol:test-db-builder-java:4.0.2` to `4.0.3`
* Updated `com.exasol:virtual-schema-common-document-files:9.1.0` to `9.1.1`
* Updated `nl.jqno.equalsverifier:equalsverifier:3.19.4` to `4.5.2`
* Updated `org.junit.jupiter:junit-jupiter-params:5.14.4` to `6.1.3`

### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:5.7.4` to `5.7.6`
