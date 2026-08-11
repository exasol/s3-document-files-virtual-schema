<!-- @formatter:off -->
# Dependencies

## Compile Dependencies

| Dependency                                     | License                          |
| ---------------------------------------------- | -------------------------------- |
| [Virtual Schema for document data in files][0] | [MIT License][1]                 |
| [AWS Java SDK :: Services :: Amazon S3][2]     | [Apache License, Version 2.0][3] |
| [error-reporting-java][4]                      | [MIT License][5]                 |

## Test Dependencies

| Dependency                                      | License                           |
| ----------------------------------------------- | --------------------------------- |
| [Hamcrest][6]                                   | [BSD-3-Clause][7]                 |
| [EqualsVerifier \| release normal jar][8]       | [Apache License, Version 2.0][9]  |
| [Virtual Schema for document data in files][0]  | [MIT License][1]                  |
| [JUnit Jupiter Params][10]                      | [Eclipse Public License v2.0][11] |
| [mockito-core][12]                              | [MIT][13]                         |
| [Testcontainers :: JUnit Jupiter Extension][14] | [MIT][15]                         |
| [Testcontainers :: Localstack][14]              | [MIT][15]                         |
| [Test Database Builder for Java][16]            | [MIT License][17]                 |
| [udf-debugging-java][18]                        | [MIT License][19]                 |
| [Matcher for SQL Result Sets][20]               | [MIT License][21]                 |
| [exasol-test-setup-abstraction-java][22]        | [MIT License][23]                 |
| [Small Json Files Test Fixture][24]             | [MIT License][25]                 |
| [Performance Test Recorder Java][26]            | [MIT License][27]                 |
| [Apache Commons Text][28]                       | [Apache-2.0][9]                   |
| [Maven Project Version Getter][29]              | [MIT License][30]                 |
| [JaCoCo :: Agent][31]                           | [EPL-2.0][32]                     |

## Runtime Dependencies

| Dependency                 | License   |
| -------------------------- | --------- |
| [SLF4J JDK14 Provider][33] | [MIT][34] |

## Plugin Dependencies

| Dependency                                              | License                                        |
| ------------------------------------------------------- | ---------------------------------------------- |
| [SonarQube Scanner for Maven][35]                       | [GNU LGPL 3][36]                               |
| [Apache Maven Toolchains Plugin][37]                    | [Apache-2.0][9]                                |
| [Apache Maven Compiler Plugin][38]                      | [Apache-2.0][9]                                |
| [Apache Maven Enforcer Plugin][39]                      | [Apache-2.0][9]                                |
| [Maven Flatten Plugin][40]                              | [Apache Software License][9]                   |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][41] | [ASL2][42]                                     |
| [Maven Surefire Plugin][43]                             | [Apache-2.0][9]                                |
| [Versions Maven Plugin][44]                             | [Apache License, Version 2.0][9]               |
| [duplicate-finder-maven-plugin Maven Mojo][45]          | [Apache License 2.0][46]                       |
| [Apache Maven Artifact Plugin][47]                      | [Apache-2.0][9]                                |
| [Project Keeper Maven plugin][48]                       | [The MIT License][49]                          |
| [Apache Maven Assembly Plugin][50]                      | [Apache-2.0][9]                                |
| [Apache Maven JAR Plugin][51]                           | [Apache-2.0][9]                                |
| [Artifact reference checker and unifier][52]            | [MIT License][53]                              |
| [spdx-maven-plugin Maven Plugin][54]                    | [The Apache Software License, Version 2.0][42] |
| [Apache Maven Dependency Plugin][55]                    | [Apache-2.0][9]                                |
| [Maven Failsafe Plugin][56]                             | [Apache-2.0][9]                                |
| [JaCoCo :: Maven Plugin][57]                            | [EPL-2.0][32]                                  |
| [error-code-crawler-maven-plugin][58]                   | [MIT License][59]                              |
| [Git Commit Id Maven Plugin][60]                        | [GNU Lesser General Public License 3.0][61]    |
| [Apache Maven Clean Plugin][62]                         | [Apache-2.0][9]                                |
| [Apache Maven Resources Plugin][63]                     | [Apache-2.0][9]                                |
| [Apache Maven Install Plugin][64]                       | [Apache-2.0][9]                                |
| [Apache Maven Site Plugin][65]                          | [Apache-2.0][9]                                |

[0]: https://github.com/exasol/virtual-schema-common-document-files/
[1]: https://github.com/exasol/virtual-schema-common-document-files/blob/main/LICENSE
[2]: https://aws.amazon.com/sdkforjava
[3]: https://aws.amazon.com/apache2.0
[4]: https://github.com/exasol/error-reporting-java/
[5]: https://github.com/exasol/error-reporting-java/blob/main/LICENSE
[6]: http://hamcrest.org/JavaHamcrest/
[7]: https://raw.githubusercontent.com/hamcrest/JavaHamcrest/master/LICENSE
[8]: https://www.jqno.nl/equalsverifier
[9]: https://www.apache.org/licenses/LICENSE-2.0.txt
[10]: https://junit.org/
[11]: https://www.eclipse.org/legal/epl-v20.html
[12]: https://github.com/mockito/mockito
[13]: https://opensource.org/licenses/MIT
[14]: https://java.testcontainers.org
[15]: http://opensource.org/licenses/MIT
[16]: https://github.com/exasol/test-db-builder-java/
[17]: https://github.com/exasol/test-db-builder-java/blob/main/LICENSE
[18]: https://github.com/exasol/udf-debugging-java/
[19]: https://github.com/exasol/udf-debugging-java/blob/main/LICENSE
[20]: https://github.com/exasol/hamcrest-resultset-matcher/
[21]: https://github.com/exasol/hamcrest-resultset-matcher/blob/main/LICENSE
[22]: https://github.com/exasol/exasol-test-setup-abstraction-java/
[23]: https://github.com/exasol/exasol-test-setup-abstraction-java/blob/main/LICENSE
[24]: https://github.com/exasol/small-json-files-test-fixture/
[25]: https://github.com/exasol/small-json-files-test-fixture/blob/main/LICENSE
[26]: https://github.com/exasol/performance-test-recorder-java/
[27]: https://github.com/exasol/performance-test-recorder-java/blob/main/LICENSE
[28]: https://commons.apache.org/proper/commons-text
[29]: https://github.com/exasol/maven-project-version-getter/
[30]: https://github.com/exasol/maven-project-version-getter/blob/main/LICENSE
[31]: https://www.eclemma.org/jacoco/index.html
[32]: https://www.eclipse.org/legal/epl-2.0/
[33]: http://www.slf4j.org
[34]: https://opensource.org/license/mit
[35]: https://docs.sonarsource.com/sonarqube-server/latest/extension-guide/developing-a-plugin/plugin-basics/sonar-scanner-maven/sonar-maven-plugin/
[36]: http://www.gnu.org/licenses/lgpl.txt
[37]: https://maven.apache.org/plugins/maven-toolchains-plugin/
[38]: https://maven.apache.org/plugins/maven-compiler-plugin/
[39]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[40]: https://www.mojohaus.org/flatten-maven-plugin/
[41]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[42]: http://www.apache.org/licenses/LICENSE-2.0.txt
[43]: https://maven.apache.org/surefire/maven-surefire-plugin/
[44]: https://www.mojohaus.org/versions/versions-maven-plugin/
[45]: https://basepom.github.io/duplicate-finder-maven-plugin
[46]: http://www.apache.org/licenses/LICENSE-2.0.html
[47]: https://maven.apache.org/plugins/maven-artifact-plugin/
[48]: https://github.com/exasol/project-keeper/
[49]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[50]: https://maven.apache.org/plugins/maven-assembly-plugin/
[51]: https://maven.apache.org/plugins/maven-jar-plugin/
[52]: https://github.com/exasol/artifact-reference-checker-maven-plugin/
[53]: https://github.com/exasol/artifact-reference-checker-maven-plugin/blob/main/LICENSE
[54]: https://github.com/spdx/spdx-maven-plugin
[55]: https://maven.apache.org/plugins/maven-dependency-plugin/
[56]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[57]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[58]: https://github.com/exasol/error-code-crawler-maven-plugin/
[59]: https://github.com/exasol/error-code-crawler-maven-plugin/blob/main/LICENSE
[60]: https://github.com/git-commit-id/git-commit-id-maven-plugin
[61]: http://www.gnu.org/licenses/lgpl-3.0.txt
[62]: https://maven.apache.org/plugins/maven-clean-plugin/
[63]: https://maven.apache.org/plugins/maven-resources-plugin/
[64]: https://maven.apache.org/plugins/maven-install-plugin/
[65]: https://maven.apache.org/plugins/maven-site-plugin/
