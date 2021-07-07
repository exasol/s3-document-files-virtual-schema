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

## Test Dependencies

| Dependency                                      | License                           |
| ----------------------------------------------- | --------------------------------- |
| [Hamcrest][16]                                  | [BSD License 3][17]               |
| [Virtual Schema for document data in files][0]  | [MIT][1]                          |
| [JUnit Jupiter Engine][20]                      | [Eclipse Public License v2.0][21] |
| [JUnit Jupiter Params][20]                      | [Eclipse Public License v2.0][21] |
| [JUnit][24]                                     | [Eclipse Public License 1.0][25]  |
| [Testcontainers :: JUnit Jupiter Extension][26] | [MIT][27]                         |
| [Testcontainers :: Localstack][26]              | [MIT][27]                         |
| [Test Database Builder for Java][30]            | [MIT][1]                          |
| [AWS Java SDK for Amazon S3][2]                 | [Apache License, Version 2.0][3]  |
| [JaCoCo :: Agent][34]                           | [Eclipse Public License 2.0][35]  |
| [JaCoCo :: Core][34]                            | [Eclipse Public License 2.0][35]  |
| [udf-debugging-java][38]                        | [MIT][1]                          |
| [Matcher for SQL Result Sets][40]               | [MIT][1]                          |
| [exasol-test-setup-abstraction-java][42]        | [MIT][1]                          |

## Plugin Dependencies

| Dependency                                              | License                                        |
| ------------------------------------------------------- | ---------------------------------------------- |
| [Maven Surefire Plugin][44]                             | [Apache License, Version 2.0][45]              |
| [Maven Failsafe Plugin][46]                             | [Apache License, Version 2.0][45]              |
| [JaCoCo :: Maven Plugin][34]                            | [Eclipse Public License 2.0][35]               |
| [Apache Maven Compiler Plugin][50]                      | [Apache License, Version 2.0][45]              |
| [Maven Dependency Plugin][52]                           | [The Apache Software License, Version 2.0][11] |
| [Versions Maven Plugin][54]                             | [Apache License, Version 2.0][45]              |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][56] | [ASL2][11]                                     |
| [Apache Maven Enforcer Plugin][58]                      | [Apache License, Version 2.0][45]              |
| [Artifact reference checker and unifier][60]            | [MIT][1]                                       |
| [Project keeper maven plugin][62]                       | [MIT][1]                                       |
| [Apache Maven Shade Plugin][64]                         | [Apache License, Version 2.0][45]              |
| [error-code-crawler-maven-plugin][66]                   | [MIT][1]                                       |
| [Reproducible Build Maven Plugin][68]                   | [Apache 2.0][11]                               |
| [Maven Clean Plugin][70]                                | [The Apache Software License, Version 2.0][11] |
| [Maven Resources Plugin][72]                            | [The Apache Software License, Version 2.0][11] |
| [Maven JAR Plugin][74]                                  | [The Apache Software License, Version 2.0][11] |
| [Maven Install Plugin][76]                              | [The Apache Software License, Version 2.0][11] |
| [Maven Deploy Plugin][78]                               | [The Apache Software License, Version 2.0][11] |
| [Maven Site Plugin 3][80]                               | [The Apache Software License, Version 2.0][11] |

[34]: https://www.eclemma.org/jacoco/index.html
[62]: https://github.com/exasol/project-keeper-maven-plugin
[4]: https://github.com/exasol/error-reporting-java
[0]: https://github.com/exasol/virtual-schema-common-document-files
[11]: http://www.apache.org/licenses/LICENSE-2.0.txt
[44]: https://maven.apache.org/surefire/maven-surefire-plugin/
[70]: http://maven.apache.org/plugins/maven-clean-plugin/
[2]: https://aws.amazon.com/sdkforjava
[14]: http://github.com/FasterXML/jackson-jaxrs-providers/jackson-jaxrs-base
[1]: https://opensource.org/licenses/MIT
[6]: https://github.com/mockito/mockito
[46]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[30]: https://github.com/exasol/test-db-builder-java
[52]: http://maven.apache.org/plugins/maven-dependency-plugin/
[54]: http://www.mojohaus.org/versions-maven-plugin/
[64]: https://maven.apache.org/plugins/maven-shade-plugin/
[17]: http://opensource.org/licenses/BSD-3-Clause
[50]: https://maven.apache.org/plugins/maven-compiler-plugin/
[27]: http://opensource.org/licenses/MIT
[24]: http://junit.org
[35]: https://www.eclipse.org/legal/epl-2.0/
[25]: http://www.eclipse.org/legal/epl-v10.html
[3]: https://aws.amazon.com/apache2.0
[7]: https://github.com/mockito/mockito/blob/main/LICENSE
[40]: https://github.com/exasol/hamcrest-resultset-matcher
[68]: http://zlika.github.io/reproducible-build-maven-plugin
[74]: http://maven.apache.org/plugins/maven-jar-plugin/
[9]: http://www.opensource.org/licenses/mit-license.php
[10]: https://github.com/FasterXML/jackson-modules-base
[45]: https://www.apache.org/licenses/LICENSE-2.0.txt
[58]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[12]: http://github.com/FasterXML/jackson-jaxrs-providers/jackson-jaxrs-json-provider
[21]: https://www.eclipse.org/legal/epl-v20.html
[76]: http://maven.apache.org/plugins/maven-install-plugin/
[20]: https://junit.org/junit5/
[56]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[26]: https://testcontainers.org
[38]: https://github.com/exasol/udf-debugging-java
[16]: http://hamcrest.org/JavaHamcrest/
[8]: http://www.slf4j.org
[78]: http://maven.apache.org/plugins/maven-deploy-plugin/
[80]: http://maven.apache.org/plugins/maven-site-plugin/
[72]: http://maven.apache.org/plugins/maven-resources-plugin/
[60]: https://github.com/exasol/artifact-reference-checker-maven-plugin
[66]: https://github.com/exasol/error-code-crawler-maven-plugin
[42]: https://github.com/exasol/exasol-test-setup-abstraction-java
