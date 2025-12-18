<!-- @formatter:off -->
# Dependencies

## Virtual Schema for Document Data in Files on aws s3

### Compile Dependencies

| Dependency                                     | License                          |
| ---------------------------------------------- | -------------------------------- |
| [Virtual Schema for document data in files][0] | [MIT License][1]                 |
| [AWS Java SDK :: Services :: Amazon S3][2]     | [Apache License, Version 2.0][3] |
| [error-reporting-java][4]                      | [MIT License][5]                 |

### Test Dependencies

| Dependency                                      | License                                       |
| ----------------------------------------------- | --------------------------------------------- |
| [Hamcrest][6]                                   | [BSD-3-Clause][7]                             |
| [EqualsVerifier \| release normal jar][8]       | [Apache License, Version 2.0][9]              |
| [Virtual Schema for document data in files][0]  | [MIT License][1]                              |
| [JUnit Jupiter Engine][10]                      | [Eclipse Public License v2.0][11]             |
| [JUnit Jupiter Params][10]                      | [Eclipse Public License v2.0][11]             |
| [mockito-core][12]                              | [MIT][13]                                     |
| [Testcontainers :: JUnit Jupiter Extension][14] | [MIT][15]                                     |
| [Testcontainers :: Localstack][14]              | [MIT][15]                                     |
| [Test Database Builder for Java][16]            | [MIT License][17]                             |
| [udf-debugging-java][18]                        | [MIT License][19]                             |
| [Matcher for SQL Result Sets][20]               | [MIT License][21]                             |
| [exasol-test-setup-abstraction-java][22]        | [MIT License][23]                             |
| [Small Json Files Test Fixture][24]             | [MIT License][25]                             |
| [Performance Test Recorder Java][26]            | [MIT License][27]                             |
| [Apache Commons Text][28]                       | [Apache-2.0][9]                               |
| [Maven Project Version Getter][29]              | [MIT License][30]                             |
| [Extension integration tests library][31]       | [MIT License][32]                             |
| [jackson-databind][33]                          | [The Apache Software License, Version 2.0][9] |
| [JaCoCo :: Agent][34]                           | [EPL-2.0][35]                                 |

### Runtime Dependencies

| Dependency                 | License   |
| -------------------------- | --------- |
| [SLF4J JDK14 Provider][36] | [MIT][37] |

### Plugin Dependencies

| Dependency                                              | License                                     |
| ------------------------------------------------------- | ------------------------------------------- |
| [Apache Maven Clean Plugin][38]                         | [Apache-2.0][9]                             |
| [Apache Maven Install Plugin][39]                       | [Apache-2.0][9]                             |
| [Apache Maven Resources Plugin][40]                     | [Apache-2.0][9]                             |
| [Apache Maven Site Plugin][41]                          | [Apache-2.0][9]                             |
| [SonarQube Scanner for Maven][42]                       | [GNU LGPL 3][43]                            |
| [Apache Maven Toolchains Plugin][44]                    | [Apache-2.0][9]                             |
| [Apache Maven Compiler Plugin][45]                      | [Apache-2.0][9]                             |
| [Apache Maven Enforcer Plugin][46]                      | [Apache-2.0][9]                             |
| [Maven Flatten Plugin][47]                              | [Apache Software License][9]                |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][48] | [ASL2][49]                                  |
| [Maven Surefire Plugin][50]                             | [Apache-2.0][9]                             |
| [Versions Maven Plugin][51]                             | [Apache License, Version 2.0][9]            |
| [duplicate-finder-maven-plugin Maven Mojo][52]          | [Apache License 2.0][53]                    |
| [Apache Maven Artifact Plugin][54]                      | [Apache-2.0][9]                             |
| [Project Keeper Maven plugin][55]                       | [The MIT License][56]                       |
| [Apache Maven Assembly Plugin][57]                      | [Apache-2.0][9]                             |
| [Apache Maven JAR Plugin][58]                           | [Apache-2.0][9]                             |
| [Artifact reference checker and unifier][59]            | [MIT License][60]                           |
| [Apache Maven Dependency Plugin][61]                    | [Apache-2.0][9]                             |
| [Maven Failsafe Plugin][62]                             | [Apache-2.0][9]                             |
| [JaCoCo :: Maven Plugin][63]                            | [EPL-2.0][35]                               |
| [Quality Summarizer Maven Plugin][64]                   | [MIT License][65]                           |
| [error-code-crawler-maven-plugin][66]                   | [MIT License][67]                           |
| [Git Commit Id Maven Plugin][68]                        | [GNU Lesser General Public License 3.0][69] |
| [Exec Maven Plugin][70]                                 | [Apache License 2][9]                       |

## Extension

### Compile Dependencies

| Dependency                                | License |
| ----------------------------------------- | ------- |
| [@exasol/extension-manager-interface][71] | MIT     |

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
[31]: https://github.com/exasol/extension-manager/
[32]: https://github.com/exasol/extension-manager/blob/main/LICENSE
[33]: https://github.com/FasterXML/jackson
[34]: https://www.eclemma.org/jacoco/index.html
[35]: https://www.eclipse.org/legal/epl-2.0/
[36]: http://www.slf4j.org
[37]: https://opensource.org/license/mit
[38]: https://maven.apache.org/plugins/maven-clean-plugin/
[39]: https://maven.apache.org/plugins/maven-install-plugin/
[40]: https://maven.apache.org/plugins/maven-resources-plugin/
[41]: https://maven.apache.org/plugins/maven-site-plugin/
[42]: http://docs.sonarqube.org/display/PLUG/Plugin+Library/sonar-scanner-maven/sonar-maven-plugin
[43]: http://www.gnu.org/licenses/lgpl.txt
[44]: https://maven.apache.org/plugins/maven-toolchains-plugin/
[45]: https://maven.apache.org/plugins/maven-compiler-plugin/
[46]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[47]: https://www.mojohaus.org/flatten-maven-plugin/
[48]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[49]: http://www.apache.org/licenses/LICENSE-2.0.txt
[50]: https://maven.apache.org/surefire/maven-surefire-plugin/
[51]: https://www.mojohaus.org/versions/versions-maven-plugin/
[52]: https://basepom.github.io/duplicate-finder-maven-plugin
[53]: http://www.apache.org/licenses/LICENSE-2.0.html
[54]: https://maven.apache.org/plugins/maven-artifact-plugin/
[55]: https://github.com/exasol/project-keeper/
[56]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[57]: https://maven.apache.org/plugins/maven-assembly-plugin/
[58]: https://maven.apache.org/plugins/maven-jar-plugin/
[59]: https://github.com/exasol/artifact-reference-checker-maven-plugin/
[60]: https://github.com/exasol/artifact-reference-checker-maven-plugin/blob/main/LICENSE
[61]: https://maven.apache.org/plugins/maven-dependency-plugin/
[62]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[63]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[64]: https://github.com/exasol/quality-summarizer-maven-plugin/
[65]: https://github.com/exasol/quality-summarizer-maven-plugin/blob/main/LICENSE
[66]: https://github.com/exasol/error-code-crawler-maven-plugin/
[67]: https://github.com/exasol/error-code-crawler-maven-plugin/blob/main/LICENSE
[68]: https://github.com/git-commit-id/git-commit-id-maven-plugin
[69]: http://www.gnu.org/licenses/lgpl-3.0.txt
[70]: https://www.mojohaus.org/exec-maven-plugin
[71]: https://registry.npmjs.org/@exasol/extension-manager-interface/-/extension-manager-interface-0.5.0.tgz
