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
| [Hamcrest][6]                                   | [BSD License 3][7]                            |
| [EqualsVerifier \| release normal jar][8]       | [Apache License, Version 2.0][9]              |
| [Virtual Schema for document data in files][0]  | [MIT License][1]                              |
| [JUnit Jupiter Engine][10]                      | [Eclipse Public License v2.0][11]             |
| [JUnit Jupiter Params][10]                      | [Eclipse Public License v2.0][11]             |
| [mockito-core][12]                              | [MIT][13]                                     |
| [Testcontainers :: JUnit Jupiter Extension][14] | [MIT][15]                                     |
| [Testcontainers :: Localstack][14]              | [MIT][15]                                     |
| [Test Database Builder for Java][16]            | [MIT License][17]                             |
| [AWS Java SDK for Amazon S3][2]                 | [Apache License, Version 2.0][3]              |
| [udf-debugging-java][18]                        | [MIT License][19]                             |
| [Matcher for SQL Result Sets][20]               | [MIT License][21]                             |
| [exasol-test-setup-abstraction-java][22]        | [MIT License][23]                             |
| [Small Json Files Test Fixture][24]             | [MIT License][25]                             |
| [Class list verifier][26]                       | [MIT License][27]                             |
| [Performance Test Recorder Java][28]            | [MIT License][29]                             |
| [Maven Project Version Getter][30]              | [MIT License][31]                             |
| [Extension integration tests library][32]       | [MIT License][33]                             |
| [jackson-databind][34]                          | [The Apache Software License, Version 2.0][9] |
| [JaCoCo :: Agent][35]                           | [Eclipse Public License 2.0][36]              |

### Runtime Dependencies

| Dependency                 | License           |
| -------------------------- | ----------------- |
| [SLF4J JDK14 Provider][37] | [MIT License][38] |

### Plugin Dependencies

| Dependency                                              | License                          |
| ------------------------------------------------------- | -------------------------------- |
| [SonarQube Scanner for Maven][39]                       | [GNU LGPL 3][40]                 |
| [Apache Maven Toolchains Plugin][41]                    | [Apache License, Version 2.0][9] |
| [Apache Maven Compiler Plugin][42]                      | [Apache-2.0][9]                  |
| [Apache Maven Enforcer Plugin][43]                      | [Apache-2.0][9]                  |
| [Maven Flatten Plugin][44]                              | [Apache Software Licenese][9]    |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][45] | [ASL2][46]                       |
| [Maven Surefire Plugin][47]                             | [Apache-2.0][9]                  |
| [Versions Maven Plugin][48]                             | [Apache License, Version 2.0][9] |
| [duplicate-finder-maven-plugin Maven Mojo][49]          | [Apache License 2.0][50]         |
| [Project Keeper Maven plugin][51]                       | [The MIT License][52]            |
| [Apache Maven Assembly Plugin][53]                      | [Apache-2.0][9]                  |
| [Apache Maven JAR Plugin][54]                           | [Apache License, Version 2.0][9] |
| [Artifact reference checker and unifier][55]            | [MIT License][56]                |
| [Apache Maven Dependency Plugin][57]                    | [Apache-2.0][9]                  |
| [Maven Failsafe Plugin][58]                             | [Apache-2.0][9]                  |
| [JaCoCo :: Maven Plugin][59]                            | [EPL-2.0][36]                    |
| [error-code-crawler-maven-plugin][60]                   | [MIT License][61]                |
| [Reproducible Build Maven Plugin][62]                   | [Apache 2.0][46]                 |
| [Apache Maven Clean Plugin][63]                         | [Apache-2.0][9]                  |
| [Exec Maven Plugin][64]                                 | [Apache License 2][9]            |

## Extension

### Compile Dependencies

| Dependency                                | License |
| ----------------------------------------- | ------- |
| [@exasol/extension-manager-interface][65] | MIT     |

[0]: https://github.com/exasol/virtual-schema-common-document-files/
[1]: https://github.com/exasol/virtual-schema-common-document-files/blob/main/LICENSE
[2]: https://aws.amazon.com/sdkforjava
[3]: https://aws.amazon.com/apache2.0
[4]: https://github.com/exasol/error-reporting-java/
[5]: https://github.com/exasol/error-reporting-java/blob/main/LICENSE
[6]: http://hamcrest.org/JavaHamcrest/
[7]: http://opensource.org/licenses/BSD-3-Clause
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
[26]: https://github.com/exasol/java-class-list-extractor/
[27]: https://github.com/exasol/java-class-list-extractor/blob/main/LICENSE
[28]: https://github.com/exasol/performance-test-recorder-java/
[29]: https://github.com/exasol/performance-test-recorder-java/blob/main/LICENSE
[30]: https://github.com/exasol/maven-project-version-getter/
[31]: https://github.com/exasol/maven-project-version-getter/blob/main/LICENSE
[32]: https://github.com/exasol/extension-manager/
[33]: https://github.com/exasol/extension-manager/blob/main/LICENSE
[34]: https://github.com/FasterXML/jackson
[35]: https://www.eclemma.org/jacoco/index.html
[36]: https://www.eclipse.org/legal/epl-2.0/
[37]: http://www.slf4j.org
[38]: http://www.opensource.org/licenses/mit-license.php
[39]: http://sonarsource.github.io/sonar-scanner-maven/
[40]: http://www.gnu.org/licenses/lgpl.txt
[41]: https://maven.apache.org/plugins/maven-toolchains-plugin/
[42]: https://maven.apache.org/plugins/maven-compiler-plugin/
[43]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[44]: https://www.mojohaus.org/flatten-maven-plugin/
[45]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[46]: http://www.apache.org/licenses/LICENSE-2.0.txt
[47]: https://maven.apache.org/surefire/maven-surefire-plugin/
[48]: https://www.mojohaus.org/versions/versions-maven-plugin/
[49]: https://basepom.github.io/duplicate-finder-maven-plugin
[50]: http://www.apache.org/licenses/LICENSE-2.0.html
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
[65]: https://registry.npmjs.org/@exasol/extension-manager-interface/-/extension-manager-interface-0.4.1.tgz
