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
| [Maven Project Version Getter][28]              | [MIT License][29]                             |
| [Extension integration tests library][30]       | [MIT License][31]                             |
| [jackson-databind][32]                          | [The Apache Software License, Version 2.0][9] |
| [JaCoCo :: Agent][33]                           | [EPL-2.0][34]                                 |

### Runtime Dependencies

| Dependency                 | License   |
| -------------------------- | --------- |
| [SLF4J JDK14 Provider][35] | [MIT][36] |

### Plugin Dependencies

| Dependency                                              | License                          |
| ------------------------------------------------------- | -------------------------------- |
| [Apache Maven Clean Plugin][37]                         | [Apache-2.0][9]                  |
| [Apache Maven Install Plugin][38]                       | [Apache-2.0][9]                  |
| [Apache Maven Resources Plugin][39]                     | [Apache-2.0][9]                  |
| [Apache Maven Site Plugin][40]                          | [Apache-2.0][9]                  |
| [SonarQube Scanner for Maven][41]                       | [GNU LGPL 3][42]                 |
| [Apache Maven Toolchains Plugin][43]                    | [Apache-2.0][9]                  |
| [Apache Maven Compiler Plugin][44]                      | [Apache-2.0][9]                  |
| [Apache Maven Enforcer Plugin][45]                      | [Apache-2.0][9]                  |
| [Maven Flatten Plugin][46]                              | [Apache Software Licenese][9]    |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][47] | [ASL2][48]                       |
| [Maven Surefire Plugin][49]                             | [Apache-2.0][9]                  |
| [Versions Maven Plugin][50]                             | [Apache License, Version 2.0][9] |
| [duplicate-finder-maven-plugin Maven Mojo][51]          | [Apache License 2.0][52]         |
| [Project Keeper Maven plugin][53]                       | [The MIT License][54]            |
| [Apache Maven Assembly Plugin][55]                      | [Apache-2.0][9]                  |
| [Apache Maven JAR Plugin][56]                           | [Apache-2.0][9]                  |
| [Artifact reference checker and unifier][57]            | [MIT License][58]                |
| [Apache Maven Dependency Plugin][59]                    | [Apache-2.0][9]                  |
| [Maven Failsafe Plugin][60]                             | [Apache-2.0][9]                  |
| [JaCoCo :: Maven Plugin][61]                            | [EPL-2.0][34]                    |
| [Quality Summarizer Maven Plugin][62]                   | [MIT License][63]                |
| [error-code-crawler-maven-plugin][64]                   | [MIT License][65]                |
| [Reproducible Build Maven Plugin][66]                   | [Apache 2.0][48]                 |
| [Exec Maven Plugin][67]                                 | [Apache License 2][9]            |

## Extension

### Compile Dependencies

| Dependency                                | License |
| ----------------------------------------- | ------- |
| [@exasol/extension-manager-interface][68] | MIT     |

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
[10]: https://junit.org/junit5/
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
[28]: https://github.com/exasol/maven-project-version-getter/
[29]: https://github.com/exasol/maven-project-version-getter/blob/main/LICENSE
[30]: https://github.com/exasol/extension-manager/
[31]: https://github.com/exasol/extension-manager/blob/main/LICENSE
[32]: https://github.com/FasterXML/jackson
[33]: https://www.eclemma.org/jacoco/index.html
[34]: https://www.eclipse.org/legal/epl-2.0/
[35]: http://www.slf4j.org
[36]: https://opensource.org/license/mit
[37]: https://maven.apache.org/plugins/maven-clean-plugin/
[38]: https://maven.apache.org/plugins/maven-install-plugin/
[39]: https://maven.apache.org/plugins/maven-resources-plugin/
[40]: https://maven.apache.org/plugins/maven-site-plugin/
[41]: http://docs.sonarqube.org/display/PLUG/Plugin+Library/sonar-maven-plugin
[42]: http://www.gnu.org/licenses/lgpl.txt
[43]: https://maven.apache.org/plugins/maven-toolchains-plugin/
[44]: https://maven.apache.org/plugins/maven-compiler-plugin/
[45]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[46]: https://www.mojohaus.org/flatten-maven-plugin/
[47]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[48]: http://www.apache.org/licenses/LICENSE-2.0.txt
[49]: https://maven.apache.org/surefire/maven-surefire-plugin/
[50]: https://www.mojohaus.org/versions/versions-maven-plugin/
[51]: https://basepom.github.io/duplicate-finder-maven-plugin
[52]: http://www.apache.org/licenses/LICENSE-2.0.html
[53]: https://github.com/exasol/project-keeper/
[54]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[55]: https://maven.apache.org/plugins/maven-assembly-plugin/
[56]: https://maven.apache.org/plugins/maven-jar-plugin/
[57]: https://github.com/exasol/artifact-reference-checker-maven-plugin/
[58]: https://github.com/exasol/artifact-reference-checker-maven-plugin/blob/main/LICENSE
[59]: https://maven.apache.org/plugins/maven-dependency-plugin/
[60]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[61]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[62]: https://github.com/exasol/quality-summarizer-maven-plugin/
[63]: https://github.com/exasol/quality-summarizer-maven-plugin/blob/main/LICENSE
[64]: https://github.com/exasol/error-code-crawler-maven-plugin/
[65]: https://github.com/exasol/error-code-crawler-maven-plugin/blob/main/LICENSE
[66]: http://zlika.github.io/reproducible-build-maven-plugin
[67]: https://www.mojohaus.org/exec-maven-plugin
[68]: https://registry.npmjs.org/@exasol/extension-manager-interface/-/extension-manager-interface-0.5.0.tgz
