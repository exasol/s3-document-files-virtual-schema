<!-- @formatter:off -->
# Dependencies

## Compile Dependencies

| Dependency                                     | License                                        |
| ---------------------------------------------- | ---------------------------------------------- |
| [Virtual Schema for document data in files][0] | [MIT][1]                                       |
| [AWS Java SDK :: Services :: Amazon S3][2]     | [Apache License, Version 2.0][3]               |
| [error-reporting-java][4]                      | [MIT][1]                                       |
| [mockito-core][6]                              | [The MIT License][7]                           |
| [SLF4J JDK14 Binding][8]                       | [MIT License][9]                               |
| [Jackson module: JAXB Annotations][10]         | [The Apache Software License, Version 2.0][11] |
| [Jackson-JAXRS-JSON][12]                       | [The Apache Software License, Version 2.0][11] |
| [Jackson-JAXRS-base][14]                       | [The Apache Software License, Version 2.0][11] |
| [Jackson dataformat: CBOR][16]                 | [The Apache Software License, Version 2.0][11] |
| [Jackson-core][18]                             | [The Apache Software License, Version 2.0][11] |
| [jackson-databind][20]                         | [The Apache Software License, Version 2.0][11] |

## Test Dependencies

| Dependency                                      | License                           |
| ----------------------------------------------- | --------------------------------- |
| [Hamcrest][22]                                  | [BSD License 3][23]               |
| [Virtual Schema for document data in files][0]  | [MIT][1]                          |
| [JUnit Jupiter Engine][26]                      | [Eclipse Public License v2.0][27] |
| [JUnit Jupiter Params][26]                      | [Eclipse Public License v2.0][27] |
| [JUnit][30]                                     | [Eclipse Public License 1.0][31]  |
| [Testcontainers :: JUnit Jupiter Extension][32] | [MIT][33]                         |
| [Testcontainers :: Localstack][32]              | [MIT][33]                         |
| [Test Database Builder for Java][36]            | [MIT][1]                          |
| [AWS Java SDK for Amazon S3][2]                 | [Apache License, Version 2.0][3]  |
| [JaCoCo :: Agent][40]                           | [Eclipse Public License 2.0][41]  |
| [JaCoCo :: Core][40]                            | [Eclipse Public License 2.0][41]  |
| [udf-debugging-java][44]                        | [MIT][1]                          |
| [Matcher for SQL Result Sets][46]               | [MIT][1]                          |
| [exasol-test-setup-abstraction-java][48]        | [MIT][1]                          |

## Plugin Dependencies

| Dependency                                              | License                                        |
| ------------------------------------------------------- | ---------------------------------------------- |
| [Maven Surefire Plugin][50]                             | [Apache License, Version 2.0][51]              |
| [Maven Failsafe Plugin][52]                             | [Apache License, Version 2.0][51]              |
| [JaCoCo :: Maven Plugin][40]                            | [Eclipse Public License 2.0][41]               |
| [Apache Maven Compiler Plugin][56]                      | [Apache License, Version 2.0][51]              |
| [Maven Dependency Plugin][58]                           | [The Apache Software License, Version 2.0][11] |
| [Versions Maven Plugin][60]                             | [Apache License, Version 2.0][51]              |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][62] | [ASL2][11]                                     |
| [Apache Maven Enforcer Plugin][64]                      | [Apache License, Version 2.0][51]              |
| [Artifact reference checker and unifier][66]            | [MIT][1]                                       |
| [Project keeper maven plugin][68]                       | [MIT][1]                                       |
| [Apache Maven Shade Plugin][70]                         | [Apache License, Version 2.0][51]              |
| [error-code-crawler-maven-plugin][72]                   | [MIT][1]                                       |
| [Reproducible Build Maven Plugin][74]                   | [Apache 2.0][11]                               |
| [Maven Clean Plugin][76]                                | [The Apache Software License, Version 2.0][11] |
| [Maven Resources Plugin][78]                            | [The Apache Software License, Version 2.0][11] |
| [Maven JAR Plugin][80]                                  | [The Apache Software License, Version 2.0][11] |
| [Maven Install Plugin][82]                              | [The Apache Software License, Version 2.0][11] |
| [Maven Deploy Plugin][84]                               | [The Apache Software License, Version 2.0][11] |
| [Maven Site Plugin 3][86]                               | [The Apache Software License, Version 2.0][11] |

[40]: https://www.eclemma.org/jacoco/index.html
[68]: https://github.com/exasol/project-keeper-maven-plugin
[4]: https://github.com/exasol/error-reporting-java
[0]: https://github.com/exasol/virtual-schema-common-document-files
[11]: http://www.apache.org/licenses/LICENSE-2.0.txt
[50]: https://maven.apache.org/surefire/maven-surefire-plugin/
[76]: http://maven.apache.org/plugins/maven-clean-plugin/
[2]: https://aws.amazon.com/sdkforjava
[14]: http://github.com/FasterXML/jackson-jaxrs-providers/jackson-jaxrs-base
[1]: https://opensource.org/licenses/MIT
[6]: https://github.com/mockito/mockito
[60]: http://www.mojohaus.org/versions-maven-plugin/
[70]: https://maven.apache.org/plugins/maven-shade-plugin/
[16]: http://github.com/FasterXML/jackson-dataformats-binary
[23]: http://opensource.org/licenses/BSD-3-Clause
[56]: https://maven.apache.org/plugins/maven-compiler-plugin/
[30]: http://junit.org
[41]: https://www.eclipse.org/legal/epl-2.0/
[20]: http://github.com/FasterXML/jackson
[3]: https://aws.amazon.com/apache2.0
[7]: https://github.com/mockito/mockito/blob/main/LICENSE
[46]: https://github.com/exasol/hamcrest-resultset-matcher
[74]: http://zlika.github.io/reproducible-build-maven-plugin
[9]: http://www.opensource.org/licenses/mit-license.php
[12]: http://github.com/FasterXML/jackson-jaxrs-providers/jackson-jaxrs-json-provider
[26]: https://junit.org/junit5/
[22]: http://hamcrest.org/JavaHamcrest/
[8]: http://www.slf4j.org
[78]: http://maven.apache.org/plugins/maven-resources-plugin/
[66]: https://github.com/exasol/artifact-reference-checker-maven-plugin
[48]: https://github.com/exasol/exasol-test-setup-abstraction-java
[18]: https://github.com/FasterXML/jackson-core
[52]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[36]: https://github.com/exasol/test-db-builder-java
[58]: http://maven.apache.org/plugins/maven-dependency-plugin/
[33]: http://opensource.org/licenses/MIT
[31]: http://www.eclipse.org/legal/epl-v10.html
[80]: http://maven.apache.org/plugins/maven-jar-plugin/
[10]: https://github.com/FasterXML/jackson-modules-base
[51]: https://www.apache.org/licenses/LICENSE-2.0.txt
[64]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[27]: https://www.eclipse.org/legal/epl-v20.html
[82]: http://maven.apache.org/plugins/maven-install-plugin/
[62]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[32]: https://testcontainers.org
[44]: https://github.com/exasol/udf-debugging-java
[84]: http://maven.apache.org/plugins/maven-deploy-plugin/
[86]: http://maven.apache.org/plugins/maven-site-plugin/
[72]: https://github.com/exasol/error-code-crawler-maven-plugin
