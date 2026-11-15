# Virtual Schema for Document Data in Files on AWS S3 5.0.1, released 2026-??-??

Code name: Fixed vulnerability CVE-2026-119078 in commons-codec:commons-codec:jar:1.19.0:test

## Summary

This release fixes the following vulnerability:

### CVE-2026-119078 (CWE-1068) in dependency `commons-codec:commons-codec:jar:1.19.0:test`
Apache Commons Codec's Blake3 class documented the output of its doFinalize methods as depending on any previously finalized bytes, so that consecutive calls would return different output. In fact, doFinalize does not change the hash state: calling it again without supplying more input returns the same bytes. The key-derivation example in the class Javadoc, shipped since 1.16.0, derives two keys with two consecutive doFinalize calls. Code that follows it gets identical keys, so the separation between the derived keys is lost. The same applies to any application that calls doFinalize more than once on one instance and expects new output each time.

This issue affects Apache Commons Codec from 1.16.0 before 1.23.0.

Version 1.23.0 corrects the documentation, but the behavior of doFinalize is unchanged. Upgrading alone does not fix affected code. Users should check their applications for repeated doFinalize calls on the same Blake3 instance and derive multiple keys by requesting the combined length in one call and splitting the output, as the updated Javadoc shows.
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-119078?component-type=maven&component-name=commons-codec%2Fcommons-codec&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-119078
* https://lists.apache.org/thread.html/ol71kdgopfxwlxgqmkd0jrfcw05wdq5z
* http://www.openwall.com/lists/oss-security/2026/11/09/29

## Security

* #279: Fixed vulnerability CVE-2026-119078 in dependency `commons-codec:commons-codec:jar:1.19.0:test`

## Dependency Updates

### Compile Dependency Updates

* Updated `software.amazon.awssdk:s3:2.57.5` to `2.59.1`

### Test Dependency Updates

* Updated `com.exasol:test-db-builder-java:4.0.3` to `5.0.0`

### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:5.8.1` to `5.8.2`
