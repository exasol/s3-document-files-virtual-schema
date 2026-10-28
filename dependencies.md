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
| [JUnit Jupiter (Aggregator)][10]                | [Eclipse Public License v2.0][11] |
| [mockito-core][12]                              | [MIT][13]                         |
| [Testcontainers :: JUnit Jupiter Extension][14] | [MIT][15]                         |
| [Testcontainers Floci (relocated)][16]          | [MIT License][13]                 |
| [Test Database Builder for Java][17]            | [MIT License][18]                 |
| [udf-debugging-java][19]                        | [MIT License][20]                 |
| [Matcher for SQL Result Sets][21]               | [MIT License][22]                 |
| [exasol-test-setup-abstraction-java][23]        | [MIT License][24]                 |
| [Small Json Files Test Fixture][25]             | [MIT License][26]                 |
| [Performance Test Recorder Java][27]            | [MIT License][28]                 |
| [Apache Commons Text][29]                       | [Apache-2.0][9]                   |
| [Maven Project Version Getter][30]              | [MIT License][31]                 |
| [JaCoCo :: Agent][32]                           | [EPL-2.0][33]                     |

## Runtime Dependencies

| Dependency                 | License   |
| -------------------------- | --------- |
| [SLF4J JDK14 Provider][34] | [MIT][35] |

## Plugin Dependencies

| Dependency                                              | License                                        |
| ------------------------------------------------------- | ---------------------------------------------- |
| [SonarQube Scanner for Maven][36]                       | [GNU LGPL 3][37]                               |
| [Apache Maven Toolchains Plugin][38]                    | [Apache-2.0][9]                                |
| [Apache Maven Compiler Plugin][39]                      | [Apache-2.0][9]                                |
| [Apache Maven Enforcer Plugin][40]                      | [Apache-2.0][9]                                |
| [Maven Flatten Plugin][41]                              | [Apache Software License][9]                   |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][42] | [ASL2][43]                                     |
| [Maven Surefire Plugin][44]                             | [Apache-2.0][9]                                |
| [Versions Maven Plugin][45]                             | [Apache License, Version 2.0][9]               |
| [duplicate-finder-maven-plugin Maven Mojo][46]          | [Apache License 2.0][47]                       |
| [Apache Maven Artifact Plugin][48]                      | [Apache-2.0][9]                                |
| [Project Keeper Maven plugin][49]                       | [The MIT License][50]                          |
| [Apache Maven Assembly Plugin][51]                      | [Apache-2.0][9]                                |
| [Apache Maven JAR Plugin][52]                           | [Apache-2.0][9]                                |
| [Artifact reference checker and unifier][53]            | [MIT License][54]                              |
| [spdx-maven-plugin Maven Plugin][55]                    | [The Apache Software License, Version 2.0][43] |
| [Apache Maven Dependency Plugin][56]                    | [Apache-2.0][9]                                |
| [Maven Failsafe Plugin][57]                             | [Apache-2.0][9]                                |
| [JaCoCo :: Maven Plugin][58]                            | [EPL-2.0][33]                                  |
| [error-code-crawler-maven-plugin][59]                   | [MIT License][60]                              |
| [Git Commit Id Maven Plugin][61]                        | [GNU Lesser General Public License 3.0][62]    |
| [Apache Maven Clean Plugin][63]                         | [Apache-2.0][9]                                |
| [Apache Maven Resources Plugin][64]                     | [Apache-2.0][9]                                |
| [Apache Maven Install Plugin][65]                       | [Apache-2.0][9]                                |
| [Apache Maven Site Plugin][66]                          | [Apache-2.0][9]                                |

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
[16]: https://github.com/floci-io/testcontainers-floci/testcontainers-floci
[17]: https://github.com/exasol/test-db-builder-java/
[18]: https://github.com/exasol/test-db-builder-java/blob/main/LICENSE
[19]: https://github.com/exasol/udf-debugging-java/
[20]: https://github.com/exasol/udf-debugging-java/blob/main/LICENSE
[21]: https://github.com/exasol/hamcrest-resultset-matcher/
[22]: https://github.com/exasol/hamcrest-resultset-matcher/blob/main/LICENSE
[23]: https://github.com/exasol/exasol-test-setup-abstraction-java/
[24]: https://github.com/exasol/exasol-test-setup-abstraction-java/blob/main/LICENSE
[25]: https://github.com/exasol/small-json-files-test-fixture/
[26]: https://github.com/exasol/small-json-files-test-fixture/blob/main/LICENSE
[27]: https://github.com/exasol/performance-test-recorder-java/
[28]: https://github.com/exasol/performance-test-recorder-java/blob/main/LICENSE
[29]: https://commons.apache.org/proper/commons-text
[30]: https://github.com/exasol/maven-project-version-getter/
[31]: https://github.com/exasol/maven-project-version-getter/blob/main/LICENSE
[32]: https://www.eclemma.org/jacoco/index.html
[33]: https://www.eclipse.org/legal/epl-2.0/
[34]: http://www.slf4j.org
[35]: https://opensource.org/license/mit
[36]: https://docs.sonarsource.com/sonarqube-server/latest/extension-guide/developing-a-plugin/plugin-basics/sonar-scanner-maven/sonar-maven-plugin/
[37]: http://www.gnu.org/licenses/lgpl.txt
[38]: https://maven.apache.org/plugins/maven-toolchains-plugin/
[39]: https://maven.apache.org/plugins/maven-compiler-plugin/
[40]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[41]: https://www.mojohaus.org/flatten-maven-plugin/
[42]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[43]: http://www.apache.org/licenses/LICENSE-2.0.txt
[44]: https://maven.apache.org/surefire/maven-surefire-plugin/
[45]: https://www.mojohaus.org/versions/versions-maven-plugin/
[46]: https://basepom.github.io/duplicate-finder-maven-plugin
[47]: http://www.apache.org/licenses/LICENSE-2.0.html
[48]: https://maven.apache.org/plugins/maven-artifact-plugin/
[49]: https://github.com/exasol/project-keeper/
[50]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[51]: https://maven.apache.org/plugins/maven-assembly-plugin/
[52]: https://maven.apache.org/plugins/maven-jar-plugin/
[53]: https://github.com/exasol/artifact-reference-checker-maven-plugin/
[54]: https://github.com/exasol/artifact-reference-checker-maven-plugin/blob/main/LICENSE
[55]: https://github.com/spdx/spdx-maven-plugin
[56]: https://maven.apache.org/plugins/maven-dependency-plugin/
[57]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[58]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[59]: https://github.com/exasol/error-code-crawler-maven-plugin/
[60]: https://github.com/exasol/error-code-crawler-maven-plugin/blob/main/LICENSE
[61]: https://github.com/git-commit-id/git-commit-id-maven-plugin
[62]: http://www.gnu.org/licenses/lgpl-3.0.txt
[63]: https://maven.apache.org/plugins/maven-clean-plugin/
[64]: https://maven.apache.org/plugins/maven-resources-plugin/
[65]: https://maven.apache.org/plugins/maven-install-plugin/
[66]: https://maven.apache.org/plugins/maven-site-plugin/
