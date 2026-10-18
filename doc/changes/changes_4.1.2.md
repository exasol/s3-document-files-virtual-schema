# Virtual Schema for Document Data in Files on AWS S3 4.1.2, released 2026-??-??

Code name: Fixed vulnerabilities CVE-2026-110262, CVE-2026-110270, CVE-2026-110257, CVE-2026-110258, CVE-2026-110259, CVE-2026-110260, CVE-2026-110261, CVE-2026-110263

## Summary

This release fixes the following 8 vulnerabilities:

### CVE-2026-110262 (CWE-770) in dependency `org.ow2.asm:asm-commons:jar:9.10.1:test`
OW2 ASM 3.1 before 9.11 contains an excessive memory allocation vulnerability in the asm-commons AdviceAdapter that allows attackers to exhaust memory by supplying crafted class files. Attackers can craft bytecode with many forward jump targets in constructors, causing unbounded stack copies in forwardJumpStackFrames and allocation of hundreds of megabytes, leading to denial of service.
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-110262?component-type=maven&component-name=org.ow2.asm%2Fasm-commons&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-110262
* https://github.com/CVEProject/cvelistV5/blob/main/cves/2026/110xxx/CVE-2026-110262.json

### CVE-2026-110270 (CWE-407) in dependency `org.ow2.asm:asm-commons:jar:9.10.1:test`
OW2 ASM 3.0 before 9.11 contains an uncontrolled memory allocation vulnerability in the asm-commons JSRInlinerAdapter that inlines JSR/RET subroutines without memory or work limits. Attackers can supply a crafted class file with nested subroutines to force O(2^n) memory allocation, causing an OutOfMemoryError that crashes the JVM.
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-110270?component-type=maven&component-name=org.ow2.asm%2Fasm-commons&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-110270
* https://github.com/CVEProject/cvelistV5/blob/main/cves/2026/110xxx/CVE-2026-110270.json

### CVE-2026-110257 (CWE-789) in dependency `org.ow2.asm:asm:jar:9.10.1:test`
OW2 ASM 5.0 before 9.11 contains an excessive memory allocation vulnerability in ClassReader.readBootstrapMethodsAttribute() that sizes an int array from num_bootstrap_methods without validating attribute_length. Attackers can supply a crafted class file of about 100 bytes that forces roughly 2 GB allocation, causing an OutOfMemoryError that crashes the JVM or consuming thread.
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-110257?component-type=maven&component-name=org.ow2.asm%2Fasm&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-110257
* https://github.com/CVEProject/cvelistV5/blob/main/cves/2026/110xxx/CVE-2026-110257.json

### CVE-2026-110258 (CWE-789) in dependency `org.ow2.asm:asm:jar:9.10.1:test`
OW2 ASM 3.0 before 9.11 contains an excessive memory allocation vulnerability in Label.addSubroutineRetSuccessors() that fails to enforce the single-RET-per-JSR rule. Attackers can supply a crafted invalid class file with multiple RET instructions for one JSR, triggering a roughly 2 GB allocation that crashes the JVM.
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-110258?component-type=maven&component-name=org.ow2.asm%2Fasm&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-110258
* https://github.com/CVEProject/cvelistV5/blob/main/cves/2026/110xxx/CVE-2026-110258.json

### CVE-2026-110259 (CWE-674) in dependency `org.ow2.asm:asm:jar:9.10.1:test`
OW2 ASM 3.0 before 9.11 contains an uncontrolled recursion vulnerability in ClassWriter.replaceAsmInstructions() that allows attackers to cause denial of service through crafted class files. Attackers can supply a class with a large method containing many long forward jumps, triggering unbounded toByteArray() recursion and a StackOverflowError during transformation.
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-110259?component-type=maven&component-name=org.ow2.asm%2Fasm&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-110259
* https://github.com/CVEProject/cvelistV5/blob/main/cves/2026/110xxx/CVE-2026-110259.json

### CVE-2026-110260 (CWE-407) in dependency `org.ow2.asm:asm:jar:9.10.1:test`
OW2 ASM 3.0 before 9.11 contains a quadratic memory allocation vulnerability in MethodWriter.computeMaxStackAndLocal() and computeAllFrames() when using COMPUTE_MAXS or COMPUTE_FRAMES. Attackers can supply a crafted class file with thousands of overlapping exception handlers, causing excessive Edge allocations and an OutOfMemoryError that crashes the JVM.
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-110260?component-type=maven&component-name=org.ow2.asm%2Fasm&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-110260
* https://github.com/CVEProject/cvelistV5/blob/main/cves/2026/110xxx/CVE-2026-110260.json

### CVE-2026-110261 (CWE-407) in dependency `org.ow2.asm:asm:jar:9.10.1:test`
OW2 ASM 3.0 before 9.11 contains a denial of service vulnerability that allows attackers to exhaust CPU by supplying crafted class files processed with ClassWriter.COMPUTE_FRAMES. Attackers can craft methods that make the unbounded MethodWriter.computeAllFrames() fix point algorithm converge slowly, tying up transforming threads for unbounded time.
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-110261?component-type=maven&component-name=org.ow2.asm%2Fasm&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-110261
* https://github.com/CVEProject/cvelistV5/blob/main/cves/2026/110xxx/CVE-2026-110261.json

### CVE-2026-110263 (CWE-674) in dependency `org.ow2.asm:asm:jar:9.10.1:test`
OW2 ASM 3.0 before 9.11 contains an uncontrolled recursion vulnerability in SignatureReader.parseType() that allows attackers to exhaust the stack via deeply nested generic type signatures. Attackers can supply a crafted class file with a long Signature attribute containing many nested type arguments to trigger a StackOverflowError during class transformation, causing denial of service.
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-110263?component-type=maven&component-name=org.ow2.asm%2Fasm&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-110263
* https://github.com/CVEProject/cvelistV5/blob/main/cves/2026/110xxx/CVE-2026-110263.json

## Security

* #268: Fixed vulnerability CVE-2026-110262 in dependency `org.ow2.asm:asm-commons:jar:9.10.1:test`
* #269: Fixed vulnerability CVE-2026-110270 in dependency `org.ow2.asm:asm-commons:jar:9.10.1:test`
* #270: Fixed vulnerability CVE-2026-110257 in dependency `org.ow2.asm:asm:jar:9.10.1:test`
* #271: Fixed vulnerability CVE-2026-110258 in dependency `org.ow2.asm:asm:jar:9.10.1:test`
* #272: Fixed vulnerability CVE-2026-110259 in dependency `org.ow2.asm:asm:jar:9.10.1:test`
* #273: Fixed vulnerability CVE-2026-110260 in dependency `org.ow2.asm:asm:jar:9.10.1:test`
* #274: Fixed vulnerability CVE-2026-110261 in dependency `org.ow2.asm:asm:jar:9.10.1:test`
* #275: Fixed vulnerability CVE-2026-110263 in dependency `org.ow2.asm:asm:jar:9.10.1:test`

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:virtual-schema-common-document-files:9.1.0` to `9.1.1`
* Updated `software.amazon.awssdk:s3:2.51.3` to `2.56.0`

### Runtime Dependency Updates

* Updated `org.slf4j:slf4j-jdk14:2.0.18` to `2.0.20`

### Test Dependency Updates

* Updated `com.exasol:exasol-test-setup-abstraction-java:3.0.0` to `3.0.1`
* Updated `com.exasol:hamcrest-resultset-matcher:1.7.3` to `1.7.4`
* Updated `com.exasol:small-json-files-test-fixture:0.1.15` to `0.1.16`
* Updated `com.exasol:test-db-builder-java:4.0.2` to `4.0.3`
* Updated `com.exasol:udf-debugging-java:0.6.20` to `0.6.21`
* Updated `com.exasol:virtual-schema-common-document-files:9.1.0` to `9.1.1`
* Updated `nl.jqno.equalsverifier:equalsverifier:3.19.4` to `4.5.2`
* Updated `org.junit.jupiter:junit-jupiter-params:5.14.4` to `6.1.3`
* Updated `org.mockito:mockito-core:5.23.0` to `5.24.0`

### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:5.7.4` to `5.7.7`
