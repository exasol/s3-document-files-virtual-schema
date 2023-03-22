<!-- @formatter:off -->
# Dependencies

## Virtual Schema for Document Data in Files on aws s3

### Compile Dependencies

| Dependency                                     | License                          |
| ---------------------------------------------- | -------------------------------- |
| [Virtual Schema for document data in files][0] | [MIT License][1]                 |
| [AWS Java SDK :: Services :: Amazon S3][2]     | [Apache License, Version 2.0][3] |
| [error-reporting-java][4]                      | [MIT License][5]                 |
| [SLF4J JDK14 Binding][6]                       | [MIT License][7]                 |
| [Apache Commons Text][8]                       | [Apache License, Version 2.0][9] |

### Test Dependencies

| Dependency                                      | License                           |
| ----------------------------------------------- | --------------------------------- |
| [Hamcrest][10]                                  | [BSD License 3][11]               |
| [EqualsVerifier | release normal jar][12]       | [Apache License, Version 2.0][9]  |
| [Virtual Schema for document data in files][0]  | [MIT License][1]                  |
| [JUnit Jupiter Engine][13]                      | [Eclipse Public License v2.0][14] |
| [JUnit Jupiter Params][13]                      | [Eclipse Public License v2.0][14] |
| [mockito-core][15]                              | [The MIT License][16]             |
| [Testcontainers :: JUnit Jupiter Extension][17] | [MIT][18]                         |
| [Testcontainers :: Localstack][17]              | [MIT][18]                         |
| [Test Database Builder for Java][19]            | [MIT License][20]                 |
| [AWS Java SDK for Amazon S3][2]                 | [Apache License, Version 2.0][3]  |
| [udf-debugging-java][21]                        | [MIT License][22]                 |
| [Matcher for SQL Result Sets][23]               | [MIT License][24]                 |
| [exasol-test-setup-abstraction-java][25]        | [MIT License][26]                 |
| [Test containers for Exasol on Docker][27]      | [MIT License][28]                 |
| [Small Json Files Test Fixture][29]             | [MIT License][30]                 |
| [Class list verifier][31]                       | [MIT License][32]                 |
| [Performance Test Recorder Java][33]            | [MIT License][34]                 |
| [Maven Project Version Getter][35]              | [MIT License][36]                 |
| [Extension integration tests library][37]       | [MIT License][38]                 |
| [jackson-databind][39]                          | [Apache License, Version 2.0][9]  |
| [JaCoCo :: Agent][40]                           | [Eclipse Public License 2.0][41]  |

### Plugin Dependencies

| Dependency                                              | License                           |
| ------------------------------------------------------- | --------------------------------- |
| [SonarQube Scanner for Maven][42]                       | [GNU LGPL 3][43]                  |
| [Apache Maven Compiler Plugin][44]                      | [Apache License, Version 2.0][9]  |
| [Apache Maven Enforcer Plugin][45]                      | [Apache-2.0][9]                   |
| [Maven Flatten Plugin][46]                              | [Apache Software Licenese][9]     |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][47] | [ASL2][48]                        |
| [Maven Surefire Plugin][49]                             | [Apache License, Version 2.0][9]  |
| [Versions Maven Plugin][50]                             | [Apache License, Version 2.0][9]  |
| [Project keeper maven plugin][51]                       | [The MIT License][52]             |
| [Apache Maven Assembly Plugin][53]                      | [Apache License, Version 2.0][9]  |
| [Apache Maven JAR Plugin][54]                           | [Apache License, Version 2.0][9]  |
| [Artifact reference checker and unifier][55]            | [MIT License][56]                 |
| [Apache Maven Dependency Plugin][57]                    | [Apache License, Version 2.0][9]  |
| [Maven Failsafe Plugin][58]                             | [Apache License, Version 2.0][9]  |
| [JaCoCo :: Maven Plugin][59]                            | [Eclipse Public License 2.0][41]  |
| [error-code-crawler-maven-plugin][60]                   | [MIT License][61]                 |
| [Reproducible Build Maven Plugin][62]                   | [Apache 2.0][48]                  |
| [Apache Maven Clean Plugin][63]                         | [Apache License, Version 2.0][9]  |
| [Exec Maven Plugin][64]                                 | [Apache License 2][9]             |
| [Maven Resources Plugin][65]                            | [Apache License, Version 2.0][48] |
| [Maven Install Plugin][66]                              | [Apache License, Version 2.0][48] |
| [Maven Deploy Plugin][67]                               | [Apache License, Version 2.0][48] |
| [Maven Site Plugin 3][68]                               | [Apache License, Version 2.0][48] |

## Extension

### Compile Dependencies

| Dependency                                | License |
| ----------------------------------------- | ------- |
| [@exasol/extension-manager-interface][69] | MIT     |

[0]: https://github.com/exasol/virtual-schema-common-document-files/
[1]: https://github.com/exasol/virtual-schema-common-document-files/blob/main/LICENSE
[2]: https://aws.amazon.com/sdkforjava
[3]: https://aws.amazon.com/apache2.0
[4]: https://github.com/exasol/error-reporting-java/
[5]: https://github.com/exasol/error-reporting-java/blob/main/LICENSE
[6]: http://www.slf4j.org
[7]: http://www.opensource.org/licenses/mit-license.php
[8]: https://commons.apache.org/proper/commons-text
[9]: https://www.apache.org/licenses/LICENSE-2.0.txt
[10]: http://hamcrest.org/JavaHamcrest/
[11]: http://opensource.org/licenses/BSD-3-Clause
[12]: https://www.jqno.nl/equalsverifier
[13]: https://junit.org/junit5/
[14]: https://www.eclipse.org/legal/epl-v20.html
[15]: https://github.com/mockito/mockito
[16]: https://github.com/mockito/mockito/blob/main/LICENSE
[17]: https://testcontainers.org
[18]: http://opensource.org/licenses/MIT
[19]: https://github.com/exasol/test-db-builder-java/
[20]: https://github.com/exasol/test-db-builder-java/blob/main/LICENSE
[21]: https://github.com/exasol/udf-debugging-java/
[22]: https://github.com/exasol/udf-debugging-java/blob/main/LICENSE
[23]: https://github.com/exasol/hamcrest-resultset-matcher/
[24]: https://github.com/exasol/hamcrest-resultset-matcher/blob/main/LICENSE
[25]: https://github.com/exasol/exasol-test-setup-abstraction-java/
[26]: https://github.com/exasol/exasol-test-setup-abstraction-java/blob/main/LICENSE
[27]: https://github.com/exasol/exasol-testcontainers/
[28]: https://github.com/exasol/exasol-testcontainers/blob/main/LICENSE
[29]: https://github.com/exasol/small-json-files-test-fixture/
[30]: https://github.com/exasol/small-json-files-test-fixture/blob/main/LICENSE
[31]: https://github.com/exasol/java-class-list-extractor/
[32]: https://github.com/exasol/java-class-list-extractor/blob/main/LICENSE
[33]: https://github.com/exasol/performance-test-recorder-java/
[34]: https://github.com/exasol/performance-test-recorder-java/blob/main/LICENSE
[35]: https://github.com/exasol/maven-project-version-getter/
[36]: https://github.com/exasol/maven-project-version-getter/blob/main/LICENSE
[37]: https://github.com/exasol/extension-manager/
[38]: https://github.com/exasol/extension-manager/blob/main/LICENSE
[39]: https://github.com/FasterXML/jackson
[40]: https://www.eclemma.org/jacoco/index.html
[41]: https://www.eclipse.org/legal/epl-2.0/
[42]: http://sonarsource.github.io/sonar-scanner-maven/
[43]: http://www.gnu.org/licenses/lgpl.txt
[44]: https://maven.apache.org/plugins/maven-compiler-plugin/
[45]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[46]: https://www.mojohaus.org/flatten-maven-plugin/
[47]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[48]: http://www.apache.org/licenses/LICENSE-2.0.txt
[49]: https://maven.apache.org/surefire/maven-surefire-plugin/
[50]: https://www.mojohaus.org/versions/versions-maven-plugin/
[51]: https://github.com/exasol/project-keeper/
[52]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[53]: https://maven.apache.org/plugins/maven-assembly-plugin/
[54]: https://maven.apache.org/plugins/maven-jar-plugin/
[55]: https://github.com/exasol/artifact-reference-checker-maven-plugin/
[56]: https://github.com/exasol/artifact-reference-checker-maven-plugin/blob/main/LICENSE
[57]: https://maven.apache.org/plugins/maven-dependency-plugin/
[58]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[59]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[60]: https://github.com/exasol/error-code-crawler-maven-plugin/
[61]: https://github.com/exasol/error-code-crawler-maven-plugin/blob/main/LICENSE
[62]: http://zlika.github.io/reproducible-build-maven-plugin
[63]: https://maven.apache.org/plugins/maven-clean-plugin/
[64]: https://www.mojohaus.org/exec-maven-plugin
[65]: https://maven.apache.org/plugins/maven-resources-plugin/
[66]: https://maven.apache.org/plugins/maven-install-plugin/
[67]: https://maven.apache.org/plugins/maven-deploy-plugin/
[68]: https://maven.apache.org/plugins/maven-site-plugin/
[69]: https://registry.npmjs.org/@exasol/extension-manager-interface/-/extension-manager-interface-0.1.15.tgz
