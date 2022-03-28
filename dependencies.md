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
| [Small Json Files Test Fixture][42]             | [MIT][1]                          |
| [Class list verifier][44]                       | [MIT][1]                          |

## Plugin Dependencies

| Dependency                                              | License                                        |
| ------------------------------------------------------- | ---------------------------------------------- |
| [Apache Maven Compiler Plugin][46]                      | [Apache License, Version 2.0][47]              |
| [Apache Maven Enforcer Plugin][48]                      | [Apache License, Version 2.0][47]              |
| [Maven Flatten Plugin][50]                              | [Apache Software Licenese][41]                 |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][52] | [ASL2][41]                                     |
| [Reproducible Build Maven Plugin][54]                   | [Apache 2.0][41]                               |
| [Maven Surefire Plugin][56]                             | [Apache License, Version 2.0][47]              |
| [Versions Maven Plugin][58]                             | [Apache License, Version 2.0][47]              |
| [Apache Maven Assembly Plugin][60]                      | [Apache License, Version 2.0][47]              |
| [Apache Maven JAR Plugin][62]                           | [Apache License, Version 2.0][47]              |
| [Artifact reference checker and unifier][64]            | [MIT][1]                                       |
| [Apache Maven Dependency Plugin][66]                    | [Apache License, Version 2.0][47]              |
| [Lombok Maven Plugin][68]                               | [The MIT License][1]                           |
| [Maven Failsafe Plugin][70]                             | [Apache License, Version 2.0][47]              |
| [JaCoCo :: Maven Plugin][72]                            | [Eclipse Public License 2.0][31]               |
| [Project keeper maven plugin][74]                       | [MIT][1]                                       |
| [error-code-crawler-maven-plugin][76]                   | [MIT][1]                                       |
| [Maven Clean Plugin][78]                                | [The Apache Software License, Version 2.0][41] |
| [Maven Resources Plugin][80]                            | [The Apache Software License, Version 2.0][41] |
| [Maven Install Plugin][82]                              | [The Apache Software License, Version 2.0][41] |
| [Maven Deploy Plugin][84]                               | [The Apache Software License, Version 2.0][41] |
| [Maven Site Plugin 3][86]                               | [The Apache Software License, Version 2.0][41] |

[30]: https://www.eclemma.org/jacoco/index.html
[4]: https://github.com/exasol/error-reporting-java
[0]: https://github.com/exasol/virtual-schema-common-document-files
[41]: http://www.apache.org/licenses/LICENSE-2.0.txt
[10]: https://projectlombok.org
[56]: https://maven.apache.org/surefire/maven-surefire-plugin/
[78]: http://maven.apache.org/plugins/maven-clean-plugin/
[2]: https://aws.amazon.com/sdkforjava
[1]: https://opensource.org/licenses/MIT
[6]: https://github.com/mockito/mockito
[58]: http://www.mojohaus.org/versions-maven-plugin/
[13]: http://opensource.org/licenses/BSD-3-Clause
[46]: https://maven.apache.org/plugins/maven-compiler-plugin/
[20]: http://junit.org
[31]: https://www.eclipse.org/legal/epl-2.0/
[72]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[3]: https://aws.amazon.com/apache2.0
[7]: https://github.com/mockito/mockito/blob/main/LICENSE
[11]: https://projectlombok.org/LICENSE
[36]: https://github.com/exasol/hamcrest-resultset-matcher
[54]: http://zlika.github.io/reproducible-build-maven-plugin
[9]: http://www.opensource.org/licenses/mit-license.php
[16]: https://junit.org/junit5/
[40]: https://bitbucket.org/snakeyaml/snakeyaml
[50]: https://www.mojohaus.org/flatten-maven-plugin/flatten-maven-plugin
[12]: http://hamcrest.org/JavaHamcrest/
[8]: http://www.slf4j.org
[80]: http://maven.apache.org/plugins/maven-resources-plugin/
[64]: https://github.com/exasol/artifact-reference-checker-maven-plugin
[38]: https://github.com/exasol/exasol-test-setup-abstraction-java
[44]: https://github.com/exasol/java-class-list-extractor
[62]: https://maven.apache.org/plugins/maven-jar-plugin/
[74]: https://github.com/exasol/project-keeper
[70]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[26]: https://github.com/exasol/test-db-builder-java
[68]: http://anthonywhitford.com/lombok.maven/lombok-maven-plugin/
[23]: http://opensource.org/licenses/MIT
[21]: http://www.eclipse.org/legal/epl-v10.html
[42]: https://github.com/exasol/small-json-files-test-fixture
[66]: https://maven.apache.org/plugins/maven-dependency-plugin/
[47]: https://www.apache.org/licenses/LICENSE-2.0.txt
[48]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[17]: https://www.eclipse.org/legal/epl-v20.html
[82]: http://maven.apache.org/plugins/maven-install-plugin/
[52]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[22]: https://testcontainers.org
[34]: https://github.com/exasol/udf-debugging-java
[84]: http://maven.apache.org/plugins/maven-deploy-plugin/
[86]: http://maven.apache.org/plugins/maven-site-plugin/
[76]: https://github.com/exasol/error-code-crawler-maven-plugin
[60]: https://maven.apache.org/plugins/maven-assembly-plugin/
