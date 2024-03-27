# S3 Document Files Virtual Schema 3.0.4, released 2024-??-??

Code name: Fixed vulnerability CVE-2024-29025 in io.netty:netty-codec-http:jar:4.1.107.Final:runtime

## Summary

This release fixes the following vulnerability:

### CVE-2024-29025 (CWE-770) in dependency `io.netty:netty-codec-http:jar:4.1.107.Final:runtime`
Netty is an asynchronous event-driven network application framework for rapid development of maintainable high performance protocol servers & clients. The `HttpPostRequestDecoder` can be tricked to accumulate data. While the decoder can store items on the disk if configured so, there are no limits to the number of fields the form can have, an attacher can send a chunked post consisting of many small fields that will be accumulated in the `bodyListHttpData` list. The decoder cumulates bytes in the `undecodedChunk` buffer until it can decode a field, this field can cumulate data without limits. This vulnerability is fixed in 4.1.108.Final.
#### References
* https://ossindex.sonatype.org/vulnerability/CVE-2024-29025?component-type=maven&component-name=io.netty%2Fnetty-codec-http&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2024-29025
* https://github.com/advisories/GHSA-5jpm-x58v-624v

## Security

* #157: Fixed vulnerability CVE-2024-29025 in dependency `io.netty:netty-codec-http:jar:4.1.107.Final:runtime`

## Dependency Updates

### Virtual Schema for Document Data in Files on AWS S3

#### Compile Dependency Updates

* Updated `software.amazon.awssdk:s3:2.25.8` to `2.25.18`

#### Test Dependency Updates

* Updated `com.amazonaws:aws-java-sdk-s3:1.12.678` to `1.12.688`
* Updated `nl.jqno.equalsverifier:equalsverifier:3.15.8` to `3.16`
