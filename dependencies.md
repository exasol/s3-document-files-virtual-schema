<!-- @formatter:off -->
# Dependencies

## Compile Dependencies

| Dependency                                     | License                          |
| ---------------------------------------------- | -------------------------------- |
| [Virtual Schema for document data in files][0] | [MIT][1]                         |
| [AWS Java SDK :: Services :: Amazon S3][2]     | [Apache License, Version 2.0][3] |
| [error-reporting-java][4]                      | [MIT][1]                         |
| [mockito-core][6]                              | [The MIT License][7]             |
| [SLF4J JDK14 Binding][8]                       | [MIT License][9]                 |
| [Project Lombok][10]                           | [The MIT License][11]            |

## Test Dependencies

| Dependency                                      | License                           |
| ----------------------------------------------- | --------------------------------- |
| [Hamcrest][12]                                  | [BSD License 3][13]               |
| [Virtual Schema for document data in files][0]  | [MIT][1]                          |
| [JUnit Jupiter Engine][16]                      | [Eclipse Public License v2.0][17] |
| [JUnit Jupiter Params][16]                      | [Eclipse Public License v2.0][17] |
| [JUnit][20]                                     | [Eclipse Public License 1.0][21]  |
| [Testcontainers :: JUnit Jupiter Extension][22] | [MIT][23]                         |
| [Testcontainers :: Localstack][22]              | [MIT][23]                         |
| [Test Database Builder for Java][26]            | [MIT][1]                          |
| [AWS Java SDK for Amazon S3][2]                 | [Apache License, Version 2.0][3]  |
| [JaCoCo :: Agent][30]                           | [Eclipse Public License 2.0][31]  |
| [JaCoCo :: Core][30]                            | [Eclipse Public License 2.0][31]  |
| [udf-debugging-java][34]                        | [MIT][1]                          |
| [Matcher for SQL Result Sets][36]               | [MIT][1]                          |
| [exasol-test-setup-abstraction-java][38]        | [MIT][1]                          |
| [SnakeYAML][40]                                 | [Apache License, Version 2.0][41] |
| [AWS Java SDK :: Services :: AWS Lambda][2]     | [Apache License, Version 2.0][3]  |
| [AWS Java SDK :: Services :: AWS IAM][2]        | [Apache License, Version 2.0][3]  |
| [AWS Java SDK :: Services :: AWS STS][2]        | [Apache License, Version 2.0][3]  |

## Plugin Dependencies

| Dependency                                              | License                                        |
| ------------------------------------------------------- | ---------------------------------------------- |
| [Maven Surefire Plugin][48]                             | [Apache License, Version 2.0][49]              |
| [Maven Failsafe Plugin][50]                             | [Apache License, Version 2.0][49]              |
| [JaCoCo :: Maven Plugin][52]                            | [Eclipse Public License 2.0][31]               |
| [Apache Maven Compiler Plugin][54]                      | [Apache License, Version 2.0][49]              |
| [Maven Dependency Plugin][56]                           | [The Apache Software License, Version 2.0][41] |
| [Versions Maven Plugin][58]                             | [Apache License, Version 2.0][49]              |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][60] | [ASL2][41]                                     |
| [Apache Maven Enforcer Plugin][62]                      | [Apache License, Version 2.0][49]              |
| [Artifact reference checker and unifier][64]            | [MIT][1]                                       |
| [Project keeper maven plugin][66]                       | [MIT][1]                                       |
| [error-code-crawler-maven-plugin][68]                   | [MIT][1]                                       |
| [Reproducible Build Maven Plugin][70]                   | [Apache 2.0][41]                               |
| [Apache Maven Assembly Plugin][72]                      | [Apache License, Version 2.0][49]              |
| [Apache Maven JAR Plugin][74]                           | [Apache License, Version 2.0][49]              |
| [Lombok Maven Plugin][76]                               | [The MIT License][1]                           |
| [Maven Clean Plugin][78]                                | [The Apache Software License, Version 2.0][41] |
| [Maven Resources Plugin][80]                            | [The Apache Software License, Version 2.0][41] |
| [Maven Install Plugin][82]                              | [The Apache Software License, Version 2.0][41] |
| [Maven Deploy Plugin][84]                               | [The Apache Software License, Version 2.0][41] |
| [Maven Site Plugin 3][86]                               | [The Apache Software License, Version 2.0][41] |

[30]: https://www.eclemma.org/jacoco/index.html
[66]: https://github.com/exasol/project-keeper-maven-plugin
[40]: http://www.snakeyaml.org
[4]: https://github.com/exasol/error-reporting-java
[0]: https://github.com/exasol/virtual-schema-common-document-files
[41]: http://www.apache.org/licenses/LICENSE-2.0.txt
[10]: https://projectlombok.org
[48]: https://maven.apache.org/surefire/maven-surefire-plugin/
[78]: http://maven.apache.org/plugins/maven-clean-plugin/
[2]: https://aws.amazon.com/sdkforjava
[1]: https://opensource.org/licenses/MIT
[6]: https://github.com/mockito/mockito
[50]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[26]: https://github.com/exasol/test-db-builder-java
[56]: http://maven.apache.org/plugins/maven-dependency-plugin/
[58]: http://www.mojohaus.org/versions-maven-plugin/
[13]: http://opensource.org/licenses/BSD-3-Clause
[54]: https://maven.apache.org/plugins/maven-compiler-plugin/
[23]: http://opensource.org/licenses/MIT
[20]: http://junit.org
[31]: https://www.eclipse.org/legal/epl-2.0/
[21]: http://www.eclipse.org/legal/epl-v10.html
[52]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[3]: https://aws.amazon.com/apache2.0
[7]: https://github.com/mockito/mockito/blob/main/LICENSE
[11]: https://projectlombok.org/LICENSE
[36]: https://github.com/exasol/hamcrest-resultset-matcher
[70]: http://zlika.github.io/reproducible-build-maven-plugin
[9]: http://www.opensource.org/licenses/mit-license.php
[49]: https://www.apache.org/licenses/LICENSE-2.0.txt
[62]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[76]: https://awhitford.github.com/lombok.maven/lombok-maven-plugin/
[17]: https://www.eclipse.org/legal/epl-v20.html
[82]: http://maven.apache.org/plugins/maven-install-plugin/
[16]: https://junit.org/junit5/
[60]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[22]: https://testcontainers.org
[34]: https://github.com/exasol/udf-debugging-java
[12]: http://hamcrest.org/JavaHamcrest/
[8]: http://www.slf4j.org
[84]: http://maven.apache.org/plugins/maven-deploy-plugin/
[86]: http://maven.apache.org/plugins/maven-site-plugin/
[80]: http://maven.apache.org/plugins/maven-resources-plugin/
[64]: https://github.com/exasol/artifact-reference-checker-maven-plugin
[68]: https://github.com/exasol/error-code-crawler-maven-plugin
[38]: https://github.com/exasol/exasol-test-setup-abstraction-java
[74]: https://maven.apache.org/plugins/maven-jar-plugin/
[72]: https://maven.apache.org/plugins/maven-assembly-plugin/
