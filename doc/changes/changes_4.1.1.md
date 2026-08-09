# Virtual Schema for Document Data in Files on AWS S3 4.1.1, released 2026-??-??

Code name: Fixed vulnerability CVE-2026-59903 in io.netty:netty-codec-http:jar:4.1.136.Final:runtime

## Summary

This release fixes the following vulnerability:

### CVE-2026-59903 (CWE-524) in dependency `io.netty:netty-codec-http:jar:4.1.136.Final:runtime`
io.netty:netty-codec-http - Use of Cache Containing Sensitive Information
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-59903?component-type=maven&component-name=io.netty%2Fnetty-codec-http&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-59903
* https://github.com/netty/netty/security/advisories/GHSA-8c42-7qj2-3j46

## Security

* #228: Fixed vulnerability CVE-2026-59903 in dependency `io.netty:netty-codec-http:jar:4.1.136.Final:runtime`

## Dependency Updates

### Compile Dependency Updates

* Updated `software.amazon.awssdk:s3:2.50.3` to `2.51.3`

### Test Dependency Updates

* Updated `nl.jqno.equalsverifier:equalsverifier:3.19.4` to `4.5`
* Updated `org.junit.jupiter:junit-jupiter-params:5.14.4` to `6.1.3`
