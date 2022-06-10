<!-- @formatter:off -->
# Dependencies

## Compile Dependencies

| Dependency                                     | License                          |
| ---------------------------------------------- | -------------------------------- |
| [Virtual Schema for document data in files][0] | [MIT License][1]                 |
| [AWS Java SDK :: Services :: Amazon S3][2]     | [Apache License, Version 2.0][3] |
| [error-reporting-java][4]                      | [MIT][5]                         |
| [SLF4J JDK14 Binding][6]                       | [MIT License][7]                 |
| [Project Lombok][8]                            | [The MIT License][9]             |

## Test Dependencies

| Dependency                                      | License                           |
| ----------------------------------------------- | --------------------------------- |
| [Hamcrest][10]                                  | [BSD License 3][11]               |
| [Virtual Schema for document data in files][0]  | [MIT License][1]                  |
| [JUnit Jupiter Engine][14]                      | [Eclipse Public License v2.0][15] |
| [JUnit Jupiter Params][14]                      | [Eclipse Public License v2.0][15] |
| [mockito-core][18]                              | [The MIT License][19]             |
| [Testcontainers :: JUnit Jupiter Extension][20] | [MIT][21]                         |
| [Testcontainers :: Localstack][20]              | [MIT][21]                         |
| [Test Database Builder for Java][24]            | [MIT License][25]                 |
| [AWS Java SDK for Amazon S3][2]                 | [Apache License, Version 2.0][3]  |
| [udf-debugging-java][28]                        | [MIT][5]                          |
| [Matcher for SQL Result Sets][30]               | [MIT][5]                          |
| [exasol-test-setup-abstraction-java][32]        | [MIT License][33]                 |
| [SnakeYAML][34]                                 | [Apache License, Version 2.0][35] |
| [Small Json Files Test Fixture][36]             | [MIT][5]                          |
| [Class list verifier][38]                       | [MIT][5]                          |
| [Performance Test Recorder Java][40]            | [MIT][5]                          |
| [JaCoCo :: Agent][42]                           | [Eclipse Public License 2.0][43]  |

## Plugin Dependencies

| Dependency                                              | License                                        |
| ------------------------------------------------------- | ---------------------------------------------- |
| [SonarQube Scanner for Maven][44]                       | [GNU LGPL 3][45]                               |
| [Apache Maven Compiler Plugin][46]                      | [Apache License, Version 2.0][47]              |
| [Apache Maven Enforcer Plugin][48]                      | [Apache License, Version 2.0][47]              |
| [Maven Flatten Plugin][50]                              | [Apache Software Licenese][35]                 |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][52] | [ASL2][35]                                     |
| [Reproducible Build Maven Plugin][54]                   | [Apache 2.0][35]                               |
| [Maven Surefire Plugin][56]                             | [Apache License, Version 2.0][47]              |
| [Versions Maven Plugin][58]                             | [Apache License, Version 2.0][47]              |
| [Project keeper maven plugin][60]                       | [The MIT License][61]                          |
| [Apache Maven Assembly Plugin][62]                      | [Apache License, Version 2.0][47]              |
| [Apache Maven JAR Plugin][64]                           | [Apache License, Version 2.0][47]              |
| [Artifact reference checker and unifier][66]            | [MIT][5]                                       |
| [Apache Maven Dependency Plugin][68]                    | [Apache License, Version 2.0][47]              |
| [Lombok Maven Plugin][70]                               | [The MIT License][5]                           |
| [Maven Failsafe Plugin][72]                             | [Apache License, Version 2.0][47]              |
| [JaCoCo :: Maven Plugin][74]                            | [Eclipse Public License 2.0][43]               |
| [error-code-crawler-maven-plugin][76]                   | [MIT][5]                                       |
| [Maven Clean Plugin][78]                                | [The Apache Software License, Version 2.0][35] |
| [Maven Resources Plugin][80]                            | [The Apache Software License, Version 2.0][35] |
| [Maven Install Plugin][82]                              | [The Apache Software License, Version 2.0][35] |
| [Maven Deploy Plugin][84]                               | [The Apache Software License, Version 2.0][35] |
| [Maven Site Plugin 3][86]                               | [The Apache Software License, Version 2.0][35] |

[42]: https://www.eclemma.org/jacoco/index.html
[4]: https://github.com/exasol/error-reporting-java
[35]: http://www.apache.org/licenses/LICENSE-2.0.txt
[8]: https://projectlombok.org
[56]: https://maven.apache.org/surefire/maven-surefire-plugin/
[78]: http://maven.apache.org/plugins/maven-clean-plugin/
[2]: https://aws.amazon.com/sdkforjava
[5]: https://opensource.org/licenses/MIT
[18]: https://github.com/mockito/mockito
[50]: https://www.mojohaus.org/flatten-maven-plugin/
[58]: http://www.mojohaus.org/versions-maven-plugin/
[60]: https://github.com/exasol/project-keeper/
[11]: http://opensource.org/licenses/BSD-3-Clause
[46]: https://maven.apache.org/plugins/maven-compiler-plugin/
[25]: https://github.com/exasol/test-db-builder-java/blob/main/LICENSE
[32]: https://github.com/exasol/exasol-test-setup-abstraction-java/
[43]: https://www.eclipse.org/legal/epl-2.0/
[45]: http://www.gnu.org/licenses/lgpl.txt
[74]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[3]: https://aws.amazon.com/apache2.0
[19]: https://github.com/mockito/mockito/blob/main/LICENSE
[9]: https://projectlombok.org/LICENSE
[30]: https://github.com/exasol/hamcrest-resultset-matcher
[54]: http://zlika.github.io/reproducible-build-maven-plugin
[33]: https://github.com/exasol/exasol-test-setup-abstraction-java/blob/main/LICENSE
[7]: http://www.opensource.org/licenses/mit-license.php
[44]: http://sonarsource.github.io/sonar-scanner-maven/
[28]: https://github.com/exasol/udf-debugging-java/
[14]: https://junit.org/junit5/
[34]: https://bitbucket.org/snakeyaml/snakeyaml
[0]: https://github.com/exasol/virtual-schema-common-document-files/
[10]: http://hamcrest.org/JavaHamcrest/
[6]: http://www.slf4j.org
[80]: http://maven.apache.org/plugins/maven-resources-plugin/
[66]: https://github.com/exasol/artifact-reference-checker-maven-plugin
[38]: https://github.com/exasol/java-class-list-extractor
[64]: https://maven.apache.org/plugins/maven-jar-plugin/
[24]: https://github.com/exasol/test-db-builder-java/
[72]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[40]: https://github.com/exasol/performance-test-recorder-java
[70]: http://anthonywhitford.com/lombok.maven/lombok-maven-plugin/
[21]: http://opensource.org/licenses/MIT
[36]: https://github.com/exasol/small-json-files-test-fixture
[61]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[68]: https://maven.apache.org/plugins/maven-dependency-plugin/
[47]: https://www.apache.org/licenses/LICENSE-2.0.txt
[48]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[15]: https://www.eclipse.org/legal/epl-v20.html
[82]: http://maven.apache.org/plugins/maven-install-plugin/
[52]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[1]: https://github.com/exasol/virtual-schema-common-document-files/blob/main/LICENSE
[20]: https://testcontainers.org
[84]: http://maven.apache.org/plugins/maven-deploy-plugin/
[86]: http://maven.apache.org/plugins/maven-site-plugin/
[76]: https://github.com/exasol/error-code-crawler-maven-plugin
[62]: https://maven.apache.org/plugins/maven-assembly-plugin/
