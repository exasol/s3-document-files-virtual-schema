# Virtual Schema for Document Data in Files on AWS S3 4.1.2, released 2026-??-??

Code name: Fixed vulnerability CVE-2026-71290 in org.apache.httpcomponents.client5:httpclient5:jar:5.6.2:runtime

## Summary

This release fixes the following vulnerability:

### CVE-2026-71290 (CWE-295) in dependency `org.apache.httpcomponents.client5:httpclient5:jar:5.6.2:runtime`
Improper TLS hostname verification vulnerability in Apache HttpComponents Client 5.4 or newer.Â HostnameVerificationPolicy#BUILTIN setting has no effect when used with the async version of HttpClient. An attacker that can intercept and modify traffic between the client and the server can impersonate the server by presenting a valid certificate for a different domain.Â 

Please note the classic version of HttpClient is not affected by this vulnerability.Â 

Affected users are recommended to upgrade to at least version 5.6.4, which fixes the issue.
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-71290?component-type=maven&component-name=org.apache.httpcomponents.client5%2Fhttpclient5&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-71290
* https://lists.apache.org/thread/bhf7g2zwpom2ohvwjjjlonc93br2s8vq
* https://github.com/advisories/GHSA-72q8-9rgw-5g6j

## Security

* #232: Fixed vulnerability CVE-2026-71290 in dependency `org.apache.httpcomponents.client5:httpclient5:jar:5.6.2:runtime`

## Dependency Updates

### Compile Dependency Updates

* Updated `software.amazon.awssdk:s3:2.51.3` to `2.54.7`

### Test Dependency Updates

* Updated `nl.jqno.equalsverifier:equalsverifier:3.19.4` to `4.5.2`
* Updated `org.junit.jupiter:junit-jupiter-params:5.14.4` to `6.1.3`
