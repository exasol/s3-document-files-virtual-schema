# Virtual Schema for Document Data in Files on AWS S3 4.1.2, released 2026-??-??

Code name: Fixed vulnerabilities CVE-2026-93451, CVE-2026-93452, CVE-2026-93492, CVE-2026-93567, CVE-2026-93568, CVE-2026-93579, CVE-2026-100655, CVE-2026-100656, CVE-2026-100658, CVE-2026-93488, CVE-2026-93491, CVE-2026-93558, CVE-2026-93562, CVE-2026-93565, CVE-2026-93574, CVE-2026-93566, CVE-2026-93573

## Summary

This release fixes the following 17 vulnerabilities:

### CVE-2026-93451 (CWE-787) in dependency `org.xerial.snappy:snappy-java:jar:1.1.10.7:compile`
snappy-java through 1.1.10.8 contains a buffer overflow vulnerability in typed Snappy.uncompress*Array methods that allocate output arrays by dividing uncompressed length by element size but pass the undivided length to native code. Attackers controlling compressed input can cause misaligned length values to write past array bounds with attacker-controlled bytes, corrupting heap memory.
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-93451?component-type=maven&component-name=org.xerial.snappy%2Fsnappy-java&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-93451
* https://github.com/xerial/snappy-java/issues/729

### CVE-2026-93452 (CWE-787) in dependency `org.xerial.snappy:snappy-java:jar:1.1.10.7:compile`
snappy-java through 1.1.10.8 contains a buffer overflow vulnerability in Snappy.compress(ByteBuffer, ByteBuffer) that writes past the end of the destination buffer. Attackers can supply incompressible data that exceeds the destination buffer's remaining capacity, corrupting off-heap memory and causing JVM termination.
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-93452?component-type=maven&component-name=org.xerial.snappy%2Fsnappy-java&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-93452
* https://github.com/xerial/snappy-java/issues/732

### CVE-2026-93492 (CWE-1035) in dependency `io.netty:netty-codec-http2:jar:4.1.137.Final:runtime`
A flaw was found in Netty's HTTP/2 HpackEncoder. A remote attacker can exploit this by sending HTTP/2 SETTINGS frames with a very large MAX_HEADER_TABLE_SIZE. This causes the HpackEncoder to store an excessive number of unique headers, leading to increased CPU usage and memory consumption, ultimately resulting in a Denial of Service (DoS).
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-93492?component-type=maven&component-name=io.netty%2Fnetty-codec-http2&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-93492
* https://github.com/netty/netty/security/advisories/GHSA-8352-h356-c9qh
* https://access.redhat.com/security/cve/CVE-2026-93492
* https://bugzilla.redhat.com/show_bug.cgi?id=2536895

### CVE-2026-93567 (CWE-20) in dependency `io.netty:netty-codec-http2:jar:4.1.137.Final:runtime`
A flaw was found in Netty's HTTP/2 codec. When converting HTTP/1 CONNECT requests to HTTP/2, the component incorrectly uses the Host header instead of the CONNECT authority-form request-target for the tunnel authority. A remote attacker can exploit this by supplying a different Host header, leading to a malformed HTTP/2 CONNECT request. This can bypass security controls such as tunnel allow-lists or egress policies, resulting in integrity loss.
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-93567?component-type=maven&component-name=io.netty%2Fnetty-codec-http2&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-93567
* https://access.redhat.com/security/cve/CVE-2026-93567
* https://bugzilla.redhat.com/show_bug.cgi?id=2536955

### CVE-2026-93568 (CWE-20) in dependency `io.netty:netty-codec-http2:jar:4.1.137.Final:runtime`
A flaw was found in Netty. A remote attacker could exploit this vulnerability by sending specially crafted HTTP/2 or HTTP/3 Extended CONNECT requests. Netty's HTTP-object conversion path incorrectly processes these requests as regular HTTP/1.1 CONNECT requests, leading to a loss of critical protocol and path information. This misinterpretation can allow attackers to bypass security policies, such as routing or authorization logic, in applications that rely on Netty for HTTP/2 or HTTP/3 communication, resulting in integrity loss.
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-93568?component-type=maven&component-name=io.netty%2Fnetty-codec-http2&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-93568
* https://github.com/netty/netty/security/advisories/GHSA-w6j8-x45j-w75f
* https://bugzilla.redhat.com/show_bug.cgi?id=2536961

### CVE-2026-93579 (CWE-1035) in dependency `io.netty:netty-codec-http2:jar:4.1.137.Final:runtime`
A flaw was found in Netty's HTTP/2 stack. This vulnerability allows a remote attacker to inject prohibited characters, such as NUL, Line Feed, and Carriage Return, into HTTP/2 header field values due to insufficient validation. When these values cross an HTTP/2 to HTTP/1.1 translation boundary, they can be exploited for request smuggling, header injection, or response splitting. This could lead to unauthorized access, data manipulation, or other security bypasses.
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-93579?component-type=maven&component-name=io.netty%2Fnetty-codec-http2&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-93579
* https://bugzilla.redhat.com/show_bug.cgi?id=2536970
* https://github.com/netty/netty/security/advisories/GHSA-8whp-c7w8-2m72

### CVE-2026-100655 (CWE-770) in dependency `io.netty:netty-codec-http:jar:4.1.137.Final:runtime`
Netty (io.netty:netty-codec-http) versions up to and including 4.1.137.Final and from 4.2.0.Final through 4.2.17.Final accept an unlimited number of concurrent remote-initiated SPDY streams: SpdySessionHandler defaults localConcurrentStreams to Integer.MAX_VALUE and exposes no API to change it. A remote peer that opens a SPDY connection and sends millions of SYN_STREAM frames with FLAG_FIN=0 causes the server to allocate unbounded heap and direct memory, eventually triggering a JVM OutOfMemoryError and crashing the service. Fixed in 4.1.138.Final and 4.2.18.Final.
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-100655?component-type=maven&component-name=io.netty%2Fnetty-codec-http&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-100655
* https://github.com/CVEProject/cvelistV5/blob/main/cves/2026/100xxx/CVE-2026-100655.json

### CVE-2026-100656 (CWE-770) in dependency `io.netty:netty-codec-http:jar:4.1.137.Final:runtime`
Netty (io.netty:netty-codec-http) contains an unbounded per-connection queue growth flaw in HttpServerCodec. The codec tracks the HTTP method of each still-unanswered pipelined request; the first 32 entries are bit-packed into a single long, but every additional entry is appended to methodOverflowQueue, an ArrayDeque with no size limit and no rejection path. A remote, unauthenticated attacker who pipelines HTTP/1.1 requests on a single connection while withholding reads on their own end (preventing responses from being flushed) can grow this queue without bound, causing unbounded heap growth and denial of service. Affected versions are 4.2.0.Final through 4.2.17.Final and all releases up to and including 4.1.137.Final; the issue is fixed in 4.2.18.Final and 4.1.138.Final.
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-100656?component-type=maven&component-name=io.netty%2Fnetty-codec-http&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-100656
* https://github.com/CVEProject/cvelistV5/blob/main/cves/2026/100xxx/CVE-2026-100656.json

### CVE-2026-100658 (CWE-770) in dependency `io.netty:netty-codec-http:jar:4.1.137.Final:runtime`
Netty (io.netty:netty-codec-http) contains an unbounded per-connection queue in WebSocketServerExtensionHandler. The handler offers an entry to its per-channel validExtensions queue for every inbound HttpRequest, but polls an entry only when the application writes an HttpResponse, and the queue size is never bounded. A remote, unauthenticated peer can use HTTP/1.1 pipelining to send requests faster than the application produces responses â including plain non-upgrade HTTP requests to any path â causing the queue to grow without limit until the JVM exhausts heap memory and terminates with OutOfMemoryError. Because the affected handler is the base class of WebSocketServerCompressionHandler, any server that enables permessage-deflate is exposed on its plain HTTP port before any WebSocket upgrade completes and before any application-level authentication. Affected versions are 4.1.88.Final through 4.1.137.Final and 4.2.0.Final through 4.2.17.Final; the issue is fixed in 4.1.138.Final and 4.2.18.Final.
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-100658?component-type=maven&component-name=io.netty%2Fnetty-codec-http&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-100658
* https://github.com/CVEProject/cvelistV5/blob/main/cves/2026/100xxx/CVE-2026-100658.json

### CVE-2026-93488 (CWE-770) in dependency `io.netty:netty-codec-http:jar:4.1.137.Final:runtime`
A flaw was found in Netty. SpdySessionHandler accepts an unlimited number of concurrent remote-initiated streams because localConcurrentStreams defaults to Integer.MAX_VALUE and the handler provides no API to change it. A remote peer can open a SPDY connection and send a large number of SYN_STREAM frames with FLAG_FIN=0, causing unbounded heap and direct memory allocation that can lead to JVM OutOfMemoryError and a denial of service.
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-93488?component-type=maven&component-name=io.netty%2Fnetty-codec-http&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-93488
* https://github.com/netty/netty/security/advisories/GHSA-rmcw-9fcq-wjq7
* https://bugzilla.redhat.com/show_bug.cgi?id=2536887

### CVE-2026-93491 (CWE-770) in dependency `io.netty:netty-codec-http:jar:4.1.137.Final:runtime`
A flaw was found in Netty's HttpServerCodec. A remote, unauthenticated attacker can exploit this vulnerability by pipelining HTTP/1.1 requests on a single connection and withholding reads. This action causes the methodOverflowQueue to grow without limit, leading to unbounded heap memory consumption and a denial of service due to memory exhaustion.
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-93491?component-type=maven&component-name=io.netty%2Fnetty-codec-http&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-93491
* https://bugzilla.redhat.com/show_bug.cgi?id=2536891
* https://github.com/netty/netty/releases/tag/netty-4.1.138.Final
* https://github.com/netty/netty/releases/tag/netty-4.2.18.Final

### CVE-2026-93558 (CWE-1035) in dependency `io.netty:netty-codec-http:jar:4.1.137.Final:runtime`
A flaw was found in Netty's WebSocketServerExtensionHandler. A remote, unauthenticated attacker can exploit this vulnerability by using HTTP/1.1 pipelining to send requests faster than the application can respond. This leads to an unbounded growth of a per-connection queue, consuming excessive memory. Eventually, this can cause the Java Virtual Machine (JVM) to exhaust its heap, resulting in a Denial of Service (DoS) for the affected server.
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-93558?component-type=maven&component-name=io.netty%2Fnetty-codec-http&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-93558
* https://github.com/netty/netty/security/advisories/GHSA-2g37-3h88-55hc

### CVE-2026-93562 (CWE-1035) in dependency `io.netty:netty-codec-http:jar:4.1.137.Final:runtime`
A flaw was found in Netty's HTTP/1 decoder. Incomplete validation of malformed Transfer-Encoding headers allows a remote attacker to perform HTTP request smuggling. By sending specially crafted HTTP requests, an attacker can inject arbitrary HTTP requests, potentially bypassing security controls or accessing unauthorized resources.

Sonatype's research suggests that this CVE's details differ from those defined at NVD. See https://guide.sonatype.com/vulnerability/CVE-2026-93562 for details
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-93562?component-type=maven&component-name=io.netty%2Fnetty-codec-http&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-93562
* https://github.com/netty/netty/security/advisories/GHSA-hcvj-94mj-jp5c
* https://bugzilla.redhat.com/show_bug.cgi?id=2536951

### CVE-2026-93565 (CWE-1035) in dependency `io.netty:netty-codec-http:jar:4.1.137.Final:runtime`
A flaw was found in Netty RtspDecoder. The `RtspMethods.valueOf()` function incorrectly strips trailing control bytes from method tokens in Real-Time Streaming Protocol (RTSP) requests. A remote attacker can exploit this by sending a specially crafted RTSP request, leading to method-token smuggling. This vulnerability allows an attacker to bypass method-based access controls and can also be used to launder malicious requests through Netty-based RTSP proxies, making them appear legitimate to backend systems.
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-93565?component-type=maven&component-name=io.netty%2Fnetty-codec-http&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-93565
* https://github.com/netty/netty/security/advisories/GHSA-h75q-xqrh-59rf
* https://bugzilla.redhat.com/show_bug.cgi?id=2536952

### CVE-2026-93574 (CWE-444) in dependency `io.netty:netty-codec-http:jar:4.1.137.Final:runtime`
A flaw was found in Netty's `netty-codec-http` component. A remote attacker could exploit this vulnerability by sending a specially crafted HTTP/1.1 chunk-size token that includes post-digit whitespace. This incorrect parsing of the chunk size can lead to HTTP request smuggling. This allows an attacker to bypass security controls or access unauthorized resources in proxy/backend deployments.

Sonatype's research suggests that this CVE's details differ from those defined at NVD. See https://guide.sonatype.com/vulnerability/CVE-2026-93574 for details
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-93574?component-type=maven&component-name=io.netty%2Fnetty-codec-http&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-93574
* https://github.com/netty/netty/security/advisories/GHSA-j4mg-hqgv-34qc

### CVE-2026-93566 (CWE-444) in dependency `io.netty:netty-codec-http:jar:4.1.137.Final:runtime`
io.netty:netty-codec-http - HTTP Request/Response Smuggling
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-93566?component-type=maven&component-name=io.netty%2Fnetty-codec-http&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-93566
* https://github.com/netty/netty/security/advisories/GHSA-rq4j-fc47-9698

### CVE-2026-93573 (CWE-436) in dependency `io.netty:netty-codec-http:jar:4.1.137.Final:runtime`
io.netty:netty-codec-http - Interpretation Conflict [CVE-2026-93573]
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-93573?component-type=maven&component-name=io.netty%2Fnetty-codec-http&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-93573
* https://github.com/netty/netty/security/advisories/GHSA-3jrc-fchc-59pw

## Security

* #244: Fixed vulnerability CVE-2026-93451 in dependency `org.xerial.snappy:snappy-java:jar:1.1.10.7:compile`
* #245: Fixed vulnerability CVE-2026-93452 in dependency `org.xerial.snappy:snappy-java:jar:1.1.10.7:compile`
* #246: Fixed vulnerability CVE-2026-93492 in dependency `io.netty:netty-codec-http2:jar:4.1.137.Final:runtime`
* #247: Fixed vulnerability CVE-2026-93567 in dependency `io.netty:netty-codec-http2:jar:4.1.137.Final:runtime`
* #248: Fixed vulnerability CVE-2026-93568 in dependency `io.netty:netty-codec-http2:jar:4.1.137.Final:runtime`
* #249: Fixed vulnerability CVE-2026-93579 in dependency `io.netty:netty-codec-http2:jar:4.1.137.Final:runtime`
* #250: Fixed vulnerability CVE-2026-100655 in dependency `io.netty:netty-codec-http:jar:4.1.137.Final:runtime`
* #251: Fixed vulnerability CVE-2026-100656 in dependency `io.netty:netty-codec-http:jar:4.1.137.Final:runtime`
* #252: Fixed vulnerability CVE-2026-100658 in dependency `io.netty:netty-codec-http:jar:4.1.137.Final:runtime`
* #253: Fixed vulnerability CVE-2026-93488 in dependency `io.netty:netty-codec-http:jar:4.1.137.Final:runtime`
* #254: Fixed vulnerability CVE-2026-93491 in dependency `io.netty:netty-codec-http:jar:4.1.137.Final:runtime`
* #255: Fixed vulnerability CVE-2026-93558 in dependency `io.netty:netty-codec-http:jar:4.1.137.Final:runtime`
* #256: Fixed vulnerability CVE-2026-93562 in dependency `io.netty:netty-codec-http:jar:4.1.137.Final:runtime`
* #257: Fixed vulnerability CVE-2026-93565 in dependency `io.netty:netty-codec-http:jar:4.1.137.Final:runtime`
* #258: Fixed vulnerability CVE-2026-93574 in dependency `io.netty:netty-codec-http:jar:4.1.137.Final:runtime`
* #259: Fixed vulnerability CVE-2026-93566 in dependency `io.netty:netty-codec-http:jar:4.1.137.Final:runtime`
* #260: Fixed vulnerability CVE-2026-93573 in dependency `io.netty:netty-codec-http:jar:4.1.137.Final:runtime`

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:virtual-schema-common-document-files:9.1.0` to `9.1.1`
* Updated `software.amazon.awssdk:s3:2.51.3` to `2.55.6`

### Runtime Dependency Updates

* Updated `org.slf4j:slf4j-jdk14:2.0.18` to `2.0.20`

### Test Dependency Updates

* Updated `com.exasol:exasol-test-setup-abstraction-java:3.0.0` to `3.0.1`
* Updated `com.exasol:hamcrest-resultset-matcher:1.7.3` to `1.7.4`
* Updated `com.exasol:small-json-files-test-fixture:0.1.15` to `0.1.16`
* Updated `com.exasol:test-db-builder-java:4.0.2` to `4.0.3`
* Updated `com.exasol:virtual-schema-common-document-files:9.1.0` to `9.1.1`
* Updated `nl.jqno.equalsverifier:equalsverifier:3.19.4` to `4.5.2`
* Updated `org.junit.jupiter:junit-jupiter-params:5.14.4` to `6.1.3`
* Updated `org.mockito:mockito-core:5.23.0` to `5.24.0`

### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:5.7.4` to `5.7.6`
