# S3 Document Files Virtual Schema 3.0.4, released 2024-??-??

Code name: Fixed vulnerabilities CVE-2024-29131, CVE-2024-29133

## Summary

This release fixes the following 2 vulnerabilities:

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

## Dependency Updates

### Virtual Schema for Document Data in Files on AWS S3

#### Compile Dependency Updates

* Updated `software.amazon.awssdk:s3:2.25.8` to `2.25.16`

#### Test Dependency Updates

* Updated `com.amazonaws:aws-java-sdk-s3:1.12.678` to `1.12.686`
* Updated `nl.jqno.equalsverifier:equalsverifier:3.15.8` to `3.16`
