<!-- @formatter:off -->
# Dependencies

## Virtual Schema for Document Data in Files on aws s3

### Compile Dependencies

| Dependency                                 | License                          |
| ------------------------------------------ | -------------------------------- |
| virtual-schema-common-document-files       |                                  |
| [AWS Java SDK :: Services :: Amazon S3][0] | [Apache License, Version 2.0][1] |
| [error-reporting-java][2]                  | [MIT License][3]                 |

### Test Dependencies

| Dependency                                      | License                                       |
| ----------------------------------------------- | --------------------------------------------- |
| [Hamcrest][4]                                   | [BSD License 3][5]                            |
| [EqualsVerifier \| release normal jar][6]       | [Apache License, Version 2.0][7]              |
| virtual-schema-common-document-files            |                                               |
| [JUnit Jupiter Engine][8]                       | [Eclipse Public License v2.0][9]              |
| [JUnit Jupiter Params][8]                       | [Eclipse Public License v2.0][9]              |
| [mockito-core][10]                              | [MIT][11]                                     |
| [Testcontainers :: JUnit Jupiter Extension][12] | [MIT][13]                                     |
| [Testcontainers :: Localstack][12]              | [MIT][13]                                     |
| [Test Database Builder for Java][14]            | [MIT License][15]                             |
| [AWS Java SDK for Amazon S3][0]                 | [Apache License, Version 2.0][1]              |
| [udf-debugging-java][16]                        | [MIT License][17]                             |
| [Matcher for SQL Result Sets][18]               | [MIT License][19]                             |
| [exasol-test-setup-abstraction-java][20]        | [MIT License][21]                             |
| [Small Json Files Test Fixture][22]             | [MIT License][23]                             |
| [Class list verifier][24]                       | [MIT License][25]                             |
| [Performance Test Recorder Java][26]            | [MIT License][27]                             |
| [Maven Project Version Getter][28]              | [MIT License][29]                             |
| [Extension integration tests library][30]       | [MIT License][31]                             |
| [jackson-databind][32]                          | [The Apache Software License, Version 2.0][7] |
| [JaCoCo :: Agent][33]                           | [Eclipse Public License 2.0][34]              |

### Runtime Dependencies

| Dependency                 | License           |
| -------------------------- | ----------------- |
| [SLF4J JDK14 Provider][35] | [MIT License][36] |

### Plugin Dependencies

| Dependency                                              | License                          |
| ------------------------------------------------------- | -------------------------------- |
| [SonarQube Scanner for Maven][37]                       | [GNU LGPL 3][38]                 |
| [Apache Maven Compiler Plugin][39]                      | [Apache-2.0][7]                  |
| [Apache Maven Enforcer Plugin][40]                      | [Apache-2.0][7]                  |
| [Maven Flatten Plugin][41]                              | [Apache Software Licenese][7]    |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][42] | [ASL2][43]                       |
| [Maven Surefire Plugin][44]                             | [Apache-2.0][7]                  |
| [Versions Maven Plugin][45]                             | [Apache License, Version 2.0][7] |
| [duplicate-finder-maven-plugin Maven Mojo][46]          | [Apache License 2.0][47]         |
| [Project keeper maven plugin][48]                       | [The MIT License][49]            |
| [Apache Maven Assembly Plugin][50]                      | [Apache-2.0][7]                  |
| [Apache Maven JAR Plugin][51]                           | [Apache License, Version 2.0][7] |
| [Artifact reference checker and unifier][52]            | [MIT License][53]                |
| [Apache Maven Dependency Plugin][54]                    | [Apache-2.0][7]                  |
| [Maven Failsafe Plugin][55]                             | [Apache-2.0][7]                  |
| [JaCoCo :: Maven Plugin][56]                            | [Eclipse Public License 2.0][34] |
| [error-code-crawler-maven-plugin][57]                   | [MIT License][58]                |
| [Reproducible Build Maven Plugin][59]                   | [Apache 2.0][43]                 |
| [Apache Maven Clean Plugin][60]                         | [Apache-2.0][7]                  |
| [Exec Maven Plugin][61]                                 | [Apache License 2][7]            |

## Extension

### Compile Dependencies

| Dependency                                | License |
| ----------------------------------------- | ------- |
| [@exasol/extension-manager-interface][62] | MIT     |

[0]: https://aws.amazon.com/sdkforjava
[1]: https://aws.amazon.com/apache2.0
[2]: https://github.com/exasol/error-reporting-java/
[3]: https://github.com/exasol/error-reporting-java/blob/main/LICENSE
[4]: http://hamcrest.org/JavaHamcrest/
[5]: http://opensource.org/licenses/BSD-3-Clause
[6]: https://www.jqno.nl/equalsverifier
[7]: https://www.apache.org/licenses/LICENSE-2.0.txt
[8]: https://junit.org/junit5/
[9]: https://www.eclipse.org/legal/epl-v20.html
[10]: https://github.com/mockito/mockito
[11]: https://github.com/mockito/mockito/blob/main/LICENSE
[12]: https://java.testcontainers.org
[13]: http://opensource.org/licenses/MIT
[14]: https://github.com/exasol/test-db-builder-java/
[15]: https://github.com/exasol/test-db-builder-java/blob/main/LICENSE
[16]: https://github.com/exasol/udf-debugging-java/
[17]: https://github.com/exasol/udf-debugging-java/blob/main/LICENSE
[18]: https://github.com/exasol/hamcrest-resultset-matcher/
[19]: https://github.com/exasol/hamcrest-resultset-matcher/blob/main/LICENSE
[20]: https://github.com/exasol/exasol-test-setup-abstraction-java/
[21]: https://github.com/exasol/exasol-test-setup-abstraction-java/blob/main/LICENSE
[22]: https://github.com/exasol/small-json-files-test-fixture/
[23]: https://github.com/exasol/small-json-files-test-fixture/blob/main/LICENSE
[24]: https://github.com/exasol/java-class-list-extractor/
[25]: https://github.com/exasol/java-class-list-extractor/blob/main/LICENSE
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
[36]: http://www.opensource.org/licenses/mit-license.php
[37]: http://sonarsource.github.io/sonar-scanner-maven/
[38]: http://www.gnu.org/licenses/lgpl.txt
[39]: https://maven.apache.org/plugins/maven-compiler-plugin/
[40]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[41]: https://www.mojohaus.org/flatten-maven-plugin/
[42]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[43]: http://www.apache.org/licenses/LICENSE-2.0.txt
[44]: https://maven.apache.org/surefire/maven-surefire-plugin/
[45]: https://www.mojohaus.org/versions/versions-maven-plugin/
[46]: https://basepom.github.io/duplicate-finder-maven-plugin
[47]: http://www.apache.org/licenses/LICENSE-2.0.html
[48]: https://github.com/exasol/project-keeper/
[49]: https://github.com/exasol/project-keeper/blob/main/LICENSE
[50]: https://maven.apache.org/plugins/maven-assembly-plugin/
[51]: https://maven.apache.org/plugins/maven-jar-plugin/
[52]: https://github.com/exasol/artifact-reference-checker-maven-plugin/
[53]: https://github.com/exasol/artifact-reference-checker-maven-plugin/blob/main/LICENSE
[54]: https://maven.apache.org/plugins/maven-dependency-plugin/
[55]: https://maven.apache.org/surefire/maven-failsafe-plugin/
[56]: https://www.jacoco.org/jacoco/trunk/doc/maven.html
[57]: https://github.com/exasol/error-code-crawler-maven-plugin/
[58]: https://github.com/exasol/error-code-crawler-maven-plugin/blob/main/LICENSE
[59]: http://zlika.github.io/reproducible-build-maven-plugin
[60]: https://maven.apache.org/plugins/maven-clean-plugin/
[61]: https://www.mojohaus.org/exec-maven-plugin
[62]: https://registry.npmjs.org/@exasol/extension-manager-interface/-/extension-manager-interface-0.3.0.tgz
