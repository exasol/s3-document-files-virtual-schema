<!-- @formatter:off -->
# Dependencies

## Compile Dependencies

| Dependency                                     | License                                                                               |
| ---------------------------------------------- | ------------------------------------------------------------------------------------- |
| [Virtual Schema for document data in files][0] | [MIT License][1]                                                                      |
| [Parquet for Java][2]                          | [MIT License][3]                                                                      |
| [Jetty :: Asynchronous HTTP Client][4]         | [Apache Software License - Version 2.0][5]; [Eclipse Public License - Version 1.0][6] |
| [reload4j][7]                                  | [The Apache Software License, Version 2.0][8]                                         |
| [AWS Java SDK :: Services :: Amazon S3][9]     | [Apache License, Version 2.0][10]                                                     |
| [error-reporting-java][11]                     | [MIT License][12]                                                                     |
| [SLF4J JDK14 Binding][13]                      | [MIT License][14]                                                                     |
| [Project Lombok][15]                           | [The MIT License][16]                                                                 |

## Test Dependencies

| Dependency                                      | License                                        |
| ----------------------------------------------- | ---------------------------------------------- |
| [Hamcrest][17]                                  | [BSD License 3][18]                            |
| [Virtual Schema for document data in files][0]  | [MIT License][1]                               |
| [JUnit Jupiter Engine][19]                      | [Eclipse Public License v2.0][20]              |
| [JUnit Jupiter Params][19]                      | [Eclipse Public License v2.0][20]              |
| [mockito-core][21]                              | [The MIT License][22]                          |
| [Testcontainers :: JUnit Jupiter Extension][23] | [MIT][24]                                      |
| [Testcontainers :: Localstack][23]              | [MIT][24]                                      |
| [Test Database Builder for Java][25]            | [MIT License][26]                              |
| [AWS Java SDK for Amazon S3][9]                 | [Apache License, Version 2.0][10]              |
| [jackson-databind][27]                          | [The Apache Software License, Version 2.0][28] |
| [udf-debugging-java][29]                        | [MIT][30]                                      |
| [Matcher for SQL Result Sets][31]               | [MIT License][32]                              |
| [exasol-test-setup-abstraction-java][33]        | [MIT License][34]                              |
| [Test containers for Exasol on Docker][35]      | [MIT License][36]                              |
| [BucketFS Java][37]                             | [MIT License][38]                              |
| [SnakeYAML][39]                                 | [Apache License, Version 2.0][8]               |
| [Small Json Files Test Fixture][40]             | [MIT License][41]                              |
| [Class list verifier][42]                       | [MIT][30]                                      |
| [Performance Test Recorder Java][43]            | [MIT License][44]                              |
| [Maven Project Version Getter][45]              | [MIT License][46]                              |
| [Extension integration tests library][47]       | [MIT License][48]                              |
| [JaCoCo :: Agent][49]                           | [Eclipse Public License 2.0][50]               |

## Plugin Dependencies

| Dependency                                              | License                                       |
| ------------------------------------------------------- | --------------------------------------------- |
| [SonarQube Scanner for Maven][51]                       | [GNU LGPL 3][52]                              |
| [Apache Maven Compiler Plugin][53]                      | [Apache License, Version 2.0][28]             |
| [Apache Maven Enforcer Plugin][54]                      | [Apache License, Version 2.0][28]             |
| [Maven Flatten Plugin][55]                              | [Apache Software Licenese][8]                 |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][56] | [ASL2][8]                                     |
| [Maven Surefire Plugin][57]                             | [Apache License, Version 2.0][28]             |
| [Versions Maven Plugin][58]                             | [Apache License, Version 2.0][28]             |
| [Project keeper maven plugin][59]                       | [The MIT License][60]                         |
| [Apache Maven Assembly Plugin][61]                      | [Apache License, Version 2.0][28]             |
| [Apache Maven JAR Plugin][62]                           | [Apache License, Version 2.0][28]             |
| [Artifact reference checker and unifier][63]            | [MIT][30]                                     |
| [Apache Maven Dependency Plugin][64]                    | [Apache License, Version 2.0][28]             |
| [Lombok Maven Plugin][65]                               | [The MIT License][30]                         |
| [Maven Failsafe Plugin][66]                             | [Apache License, Version 2.0][28]             |
| [JaCoCo :: Maven Plugin][67]                            | [Eclipse Public License 2.0][50]              |
| [Apache Maven Clean Plugin][68]                         | [Apache License, Version 2.0][28]             |
| [Exec Maven Plugin][69]                                 | [Apache License 2][8]                         |
| [error-code-crawler-maven-plugin][70]                   | [MIT License][71]                             |
| [Reproducible Build Maven Plugin][72]                   | [Apache 2.0][8]                               |
| [Maven Resources Plugin][73]                            | [The Apache Software License, Version 2.0][8] |
| [Maven Install Plugin][74]                              | [The Apache Software License, Version 2.0][8] |
| [Maven Deploy Plugin][75]                               | [The Apache Software License, Version 2.0][8] |
| [Maven Site Plugin 3][76]                               | [The Apache Software License, Version 2.0][8] |

[0]: https://github.com/exasol/virtual-schema-common-document-files/
[1]: https://github.com/exasol/virtual-schema-common-document-files/blob/main/LICENSE
[2]: https://github.com/exasol/parquet-io-java/
[3]: https://github.com/exasol/parquet-io-java/blob/main/LICENSE
[4]: https://eclipse.org/jetty/jetty-client
[5]: http://www.apache.org/licenses/LICENSE-2.0
[6]: https://www.eclipse.org/org/documents/epl-v10.php
[7]: https://reload4j.qos.ch
[8]: http://www.apache.org/licenses/LICENSE-2.0.txt
[9]: https://aws.amazon.com/sdkforjava
[10]: https://aws.amazon.com/apache2.0
[11]: https://github.com/exasol/error-reporting-java/
[12]: https://github.com/exasol/error-reporting-java/blob/main/LICENSE
[13]: http://www.slf4j.org
[14]: http://www.opensource.org/licenses/mit-license.php
[15]: https://projectlombok.org
[16]: https://projectlombok.org/LICENSE
[17]: http://hamcrest.org/JavaHamcrest/
[18]: http://opensource.org/licenses/BSD-3-Clause
[19]: https://junit.org/junit5/
[20]: https://www.eclipse.org/legal/epl-v20.html
[21]: https://github.com/mockito/mockito
[22]: https://github.com/mockito/mockito/blob/main/LICENSE
[23]: https://testcontainers.org
[24]: http://opensource.org/licenses/MIT
[25]: https://github.com/exasol/test-db-builder-java/
[26]: https://github.com/exasol/test-db-builder-java/blob/main/LICENSE
[27]: https://github.com/FasterXML/jackson
[28]: https://www.apache.org/licenses/LICENSE-2.0.txt
[29]: https://github.com/exasol/udf-debugging-java/
[30]: https://opensource.org/licenses/MIT
[31]: https://github.com/exasol/hamcrest-resultset-matcher/
[32]: https://github.com/exasol/hamcrest-resultset-matcher/blob/main/LICENSE
[33]: https://github.com/exasol/exasol-test-setup-abstraction-java/
[34]: https://github.com/exasol/exasol-test-setup-abstraction-java/blob/main/LICENSE
[35]: https://github.com/exasol/exasol-testcontainers/
[36]: https://github.com/exasol/exasol-testcontainers/blob/main/LICENSE
[37]: https://github.com/exasol/bucketfs-java/
[38]: https://github.com/exasol/bucketfs-java/blob/main/LICENSE
[39]: https://bitbucket.org/snakeyaml/snakeyaml
[40]: https://github.com/exasol/small-json-files-test-fixture/
[41]: https://github.com/exasol/small-json-files-test-fixture/blob/main/LICENSE
[42]: https://github.com/exasol/java-class-list-extractor
[43]: https://github.com/exasol/performance-test-recorder-java/
[44]: https://github.com/exasol/performance-test-recorder-java/blob/main/LICENSE
[45]: https://github.com/exasol/maven-project-version-getter/
[46]: https://github.com/exasol/maven-project-version-getter/blob/main/LICENSE
[47]: https://github.com/exasol/extension-manager/
[48]: https://github.com/exasol/extension-manager/blob/main/LICENSE
[49]: https://www.eclemma.org/jacoco/index.html
[50]: https://www.eclipse.org/legal/epl-2.0/
[51]: http://sonarsource.github.io/sonar-scanner-maven/
[52]: http://www.gnu.org/licenses/lgpl.txt
[53]: https://maven.apache.org/plugins/maven-compiler-plugin/
[54]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[55]: https://www.mojohaus.org/flatten-maven-plugin/
[56]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[57]: https://maven.apache.org/surefire/maven-surefire-plugin/
[58]: http://www.mojohaus.org/versions-maven-plugin/
[59]: https://github.com/exasol/project-keeper/
[60]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[61]: https://maven.apache.org/plugins/maven-assembly-plugin/
[62]: https://maven.apache.org/plugins/maven-jar-plugin/
[63]: https://github.com/exasol/artifact-reference-checker-maven-plugin
[64]: https://maven.apache.org/plugins/maven-dependency-plugin/
[65]: http://anthonywhitford.com/lombok.maven/lombok-maven-plugin/
[66]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[67]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[68]: https://maven.apache.org/plugins/maven-clean-plugin/
[69]: http://www.mojohaus.org/exec-maven-plugin
[70]: https://github.com/exasol/error-code-crawler-maven-plugin/
[71]: https://github.com/exasol/error-code-crawler-maven-plugin/blob/main/LICENSE
[72]: http://zlika.github.io/reproducible-build-maven-plugin
[73]: http://maven.apache.org/plugins/maven-resources-plugin/
[74]: http://maven.apache.org/plugins/maven-install-plugin/
[75]: http://maven.apache.org/plugins/maven-deploy-plugin/
[76]: http://maven.apache.org/plugins/maven-site-plugin/
