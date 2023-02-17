<!-- @formatter:off -->
# Dependencies

## Virtual Schema for Document Data in Files on aws s3

### Compile Dependencies

| Dependency                                     | License                          |
| ---------------------------------------------- | -------------------------------- |
| [Virtual Schema for document data in files][0] | [MIT License][1]                 |
| [Apache Commons Net][2]                        | [Apache License, Version 2.0][3] |
| [AWS Java SDK :: Services :: Amazon S3][4]     | [Apache License, Version 2.0][5] |
| [error-reporting-java][6]                      | [MIT License][7]                 |
| [SLF4J JDK14 Binding][8]                       | [MIT License][9]                 |
| [Apache Commons Text][10]                      | [Apache License, Version 2.0][3] |

### Test Dependencies

| Dependency                                      | License                           |
| ----------------------------------------------- | --------------------------------- |
| [Hamcrest][11]                                  | [BSD License 3][12]               |
| [EqualsVerifier | release normal jar][13]       | [Apache License, Version 2.0][3]  |
| [Virtual Schema for document data in files][0]  | [MIT License][1]                  |
| [JUnit Jupiter Engine][14]                      | [Eclipse Public License v2.0][15] |
| [JUnit Jupiter Params][14]                      | [Eclipse Public License v2.0][15] |
| [mockito-core][16]                              | [The MIT License][17]             |
| [Testcontainers :: JUnit Jupiter Extension][18] | [MIT][19]                         |
| [Testcontainers :: Localstack][18]              | [MIT][19]                         |
| [Test Database Builder for Java][20]            | [MIT License][21]                 |
| [AWS Java SDK for Amazon S3][4]                 | [Apache License, Version 2.0][5]  |
| [udf-debugging-java][22]                        | [MIT License][23]                 |
| [Matcher for SQL Result Sets][24]               | [MIT License][25]                 |
| [exasol-test-setup-abstraction-java][26]        | [MIT License][27]                 |
| [Test containers for Exasol on Docker][28]      | [MIT License][29]                 |
| [Small Json Files Test Fixture][30]             | [MIT License][31]                 |
| [Class list verifier][32]                       | [MIT License][33]                 |
| [Performance Test Recorder Java][34]            | [MIT License][35]                 |
| [Maven Project Version Getter][36]              | [MIT License][37]                 |
| [Extension integration tests library][38]       | [MIT License][39]                 |
| [JaCoCo :: Agent][40]                           | [Eclipse Public License 2.0][41]  |

### Plugin Dependencies

| Dependency                                              | License                          |
| ------------------------------------------------------- | -------------------------------- |
| [SonarQube Scanner for Maven][42]                       | [GNU LGPL 3][43]                 |
| [Apache Maven Compiler Plugin][44]                      | [Apache License, Version 2.0][3] |
| [Apache Maven Enforcer Plugin][45]                      | [Apache License, Version 2.0][3] |
| [Maven Flatten Plugin][46]                              | [Apache Software Licenese][3]    |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][47] | [ASL2][48]                       |
| [Maven Surefire Plugin][49]                             | [Apache License, Version 2.0][3] |
| [Versions Maven Plugin][50]                             | [Apache License, Version 2.0][3] |
| [Project keeper maven plugin][51]                       | [The MIT License][52]            |
| [Apache Maven Assembly Plugin][53]                      | [Apache License, Version 2.0][3] |
| [Apache Maven JAR Plugin][54]                           | [Apache License, Version 2.0][3] |
| [Artifact reference checker and unifier][55]            | [MIT License][56]                |
| [Apache Maven Dependency Plugin][57]                    | [Apache License, Version 2.0][3] |
| [Maven Failsafe Plugin][58]                             | [Apache License, Version 2.0][3] |
| [JaCoCo :: Maven Plugin][59]                            | [Eclipse Public License 2.0][41] |
| [error-code-crawler-maven-plugin][60]                   | [MIT License][61]                |
| [Reproducible Build Maven Plugin][62]                   | [Apache 2.0][48]                 |
| [Apache Maven Clean Plugin][63]                         | [Apache License, Version 2.0][3] |
| [Exec Maven Plugin][64]                                 | [Apache License 2][3]            |
| [Apache Maven Resources Plugin][65]                     | [Apache License, Version 2.0][3] |
| [Apache Maven Install Plugin][66]                       | [Apache License, Version 2.0][3] |
| [Apache Maven Deploy Plugin][67]                        | [Apache License, Version 2.0][3] |
| [Apache Maven Site Plugin][68]                          | [Apache License, Version 2.0][3] |

## Extension

### Compile Dependencies

| Dependency                                | License |
| ----------------------------------------- | ------- |
| [@exasol/extension-manager-interface][69] | MIT     |

[0]: https://github.com/exasol/virtual-schema-common-document-files/
[1]: https://github.com/exasol/virtual-schema-common-document-files/blob/main/LICENSE
[2]: https://commons.apache.org/proper/commons-net/
[3]: https://www.apache.org/licenses/LICENSE-2.0.txt
[4]: https://aws.amazon.com/sdkforjava
[5]: https://aws.amazon.com/apache2.0
[6]: https://github.com/exasol/error-reporting-java/
[7]: https://github.com/exasol/error-reporting-java/blob/main/LICENSE
[8]: http://www.slf4j.org
[9]: http://www.opensource.org/licenses/mit-license.php
[10]: https://commons.apache.org/proper/commons-text
[11]: http://hamcrest.org/JavaHamcrest/
[12]: http://opensource.org/licenses/BSD-3-Clause
[13]: https://www.jqno.nl/equalsverifier
[14]: https://junit.org/junit5/
[15]: https://www.eclipse.org/legal/epl-v20.html
[16]: https://github.com/mockito/mockito
[17]: https://github.com/mockito/mockito/blob/main/LICENSE
[18]: https://testcontainers.org
[19]: http://opensource.org/licenses/MIT
[20]: https://github.com/exasol/test-db-builder-java/
[21]: https://github.com/exasol/test-db-builder-java/blob/main/LICENSE
[22]: https://github.com/exasol/udf-debugging-java/
[23]: https://github.com/exasol/udf-debugging-java/blob/main/LICENSE
[24]: https://github.com/exasol/hamcrest-resultset-matcher/
[25]: https://github.com/exasol/hamcrest-resultset-matcher/blob/main/LICENSE
[26]: https://github.com/exasol/exasol-test-setup-abstraction-java/
[27]: https://github.com/exasol/exasol-test-setup-abstraction-java/blob/main/LICENSE
[28]: https://github.com/exasol/exasol-testcontainers/
[29]: https://github.com/exasol/exasol-testcontainers/blob/main/LICENSE
[30]: https://github.com/exasol/small-json-files-test-fixture/
[31]: https://github.com/exasol/small-json-files-test-fixture/blob/main/LICENSE
[32]: https://github.com/exasol/java-class-list-extractor/
[33]: https://github.com/exasol/java-class-list-extractor/blob/main/LICENSE
[34]: https://github.com/exasol/performance-test-recorder-java/
[35]: https://github.com/exasol/performance-test-recorder-java/blob/main/LICENSE
[36]: https://github.com/exasol/maven-project-version-getter/
[37]: https://github.com/exasol/maven-project-version-getter/blob/main/LICENSE
[38]: https://github.com/exasol/extension-manager/
[39]: https://github.com/exasol/extension-manager/blob/main/LICENSE
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
