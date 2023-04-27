<!-- @formatter:off -->
# Dependencies

## Virtual Schema for Document Data in Files on aws s3

### Compile Dependencies

| Dependency                                     | License                          |
| ---------------------------------------------- | -------------------------------- |
| [Virtual Schema for document data in files][0] | [MIT License][1]                 |
| [AWS Java SDK :: Services :: Amazon S3][2]     | [Apache License, Version 2.0][3] |
| [error-reporting-java][4]                      | [MIT License][5]                 |
| [Apache Commons Text][6]                       | [Apache License, Version 2.0][7] |

### Test Dependencies

| Dependency                                      | License                                       |
| ----------------------------------------------- | --------------------------------------------- |
| [Hamcrest][8]                                   | [BSD License 3][9]                            |
| [EqualsVerifier | release normal jar][10]       | [Apache License, Version 2.0][7]              |
| [Virtual Schema for document data in files][0]  | [MIT License][1]                              |
| [JUnit Jupiter Engine][11]                      | [Eclipse Public License v2.0][12]             |
| [JUnit Jupiter Params][11]                      | [Eclipse Public License v2.0][12]             |
| [mockito-core][13]                              | [The MIT License][14]                         |
| [Testcontainers :: JUnit Jupiter Extension][15] | [MIT][16]                                     |
| [Testcontainers :: Localstack][15]              | [MIT][16]                                     |
| [Test Database Builder for Java][17]            | [MIT License][18]                             |
| [AWS Java SDK for Amazon S3][2]                 | [Apache License, Version 2.0][3]              |
| [udf-debugging-java][19]                        | [MIT License][20]                             |
| [Matcher for SQL Result Sets][21]               | [MIT License][22]                             |
| [exasol-test-setup-abstraction-java][23]        | [MIT License][24]                             |
| [Small Json Files Test Fixture][25]             | [MIT License][26]                             |
| [Class list verifier][27]                       | [MIT License][28]                             |
| [Performance Test Recorder Java][29]            | [MIT License][30]                             |
| [Maven Project Version Getter][31]              | [MIT License][32]                             |
| [Extension integration tests library][33]       | [MIT License][34]                             |
| [jackson-databind][35]                          | [The Apache Software License, Version 2.0][7] |
| [JaCoCo :: Agent][36]                           | [Eclipse Public License 2.0][37]              |

### Runtime Dependencies

| Dependency                | License           |
| ------------------------- | ----------------- |
| [SLF4J JDK14 Binding][38] | [MIT License][39] |

### Plugin Dependencies

| Dependency                                              | License                                        |
| ------------------------------------------------------- | ---------------------------------------------- |
| [SonarQube Scanner for Maven][40]                       | [GNU LGPL 3][41]                               |
| [Apache Maven Compiler Plugin][42]                      | [Apache-2.0][7]                                |
| [Apache Maven Enforcer Plugin][43]                      | [Apache-2.0][7]                                |
| [Maven Flatten Plugin][44]                              | [Apache Software Licenese][7]                  |
| [org.sonatype.ossindex.maven:ossindex-maven-plugin][45] | [ASL2][46]                                     |
| [Maven Surefire Plugin][47]                             | [Apache-2.0][7]                                |
| [Versions Maven Plugin][48]                             | [Apache License, Version 2.0][7]               |
| [duplicate-finder-maven-plugin Maven Mojo][49]          | [Apache License 2.0][50]                       |
| [Project keeper maven plugin][51]                       | [The MIT License][52]                          |
| [Apache Maven Assembly Plugin][53]                      | [Apache License, Version 2.0][7]               |
| [Apache Maven JAR Plugin][54]                           | [Apache License, Version 2.0][7]               |
| [Artifact reference checker and unifier][55]            | [MIT License][56]                              |
| [Apache Maven Dependency Plugin][57]                    | [Apache License, Version 2.0][7]               |
| [Maven Failsafe Plugin][58]                             | [Apache-2.0][7]                                |
| [JaCoCo :: Maven Plugin][59]                            | [Eclipse Public License 2.0][37]               |
| [error-code-crawler-maven-plugin][60]                   | [MIT License][61]                              |
| [Reproducible Build Maven Plugin][62]                   | [Apache 2.0][46]                               |
| [Apache Maven Clean Plugin][63]                         | [Apache License, Version 2.0][7]               |
| [Exec Maven Plugin][64]                                 | [Apache License 2][7]                          |
| [Maven Resources Plugin][65]                            | [The Apache Software License, Version 2.0][46] |
| [Maven Install Plugin][66]                              | [The Apache Software License, Version 2.0][46] |
| [Maven Deploy Plugin][67]                               | [The Apache Software License, Version 2.0][46] |
| [Maven Site Plugin 3][68]                               | [The Apache Software License, Version 2.0][46] |

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
[6]: https://commons.apache.org/proper/commons-text
[7]: https://www.apache.org/licenses/LICENSE-2.0.txt
[8]: http://hamcrest.org/JavaHamcrest/
[9]: http://opensource.org/licenses/BSD-3-Clause
[10]: https://www.jqno.nl/equalsverifier
[11]: https://junit.org/junit5/
[12]: https://www.eclipse.org/legal/epl-v20.html
[13]: https://github.com/mockito/mockito
[14]: https://github.com/mockito/mockito/blob/main/LICENSE
[15]: https://testcontainers.org
[16]: http://opensource.org/licenses/MIT
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
[27]: https://github.com/exasol/java-class-list-extractor/
[28]: https://github.com/exasol/java-class-list-extractor/blob/main/LICENSE
[29]: https://github.com/exasol/performance-test-recorder-java/
[30]: https://github.com/exasol/performance-test-recorder-java/blob/main/LICENSE
[31]: https://github.com/exasol/maven-project-version-getter/
[32]: https://github.com/exasol/maven-project-version-getter/blob/main/LICENSE
[33]: https://github.com/exasol/extension-manager/
[34]: https://github.com/exasol/extension-manager/blob/main/LICENSE
[35]: https://github.com/FasterXML/jackson
[36]: https://www.eclemma.org/jacoco/index.html
[37]: https://www.eclipse.org/legal/epl-2.0/
[38]: http://www.slf4j.org
[39]: http://www.opensource.org/licenses/mit-license.php
[40]: http://sonarsource.github.io/sonar-scanner-maven/
[41]: http://www.gnu.org/licenses/lgpl.txt
[42]: https://maven.apache.org/plugins/maven-compiler-plugin/
[43]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[44]: https://www.mojohaus.org/flatten-maven-plugin/
[45]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[46]: http://www.apache.org/licenses/LICENSE-2.0.txt
[47]: https://maven.apache.org/surefire/maven-surefire-plugin/
[48]: https://www.mojohaus.org/versions/versions-maven-plugin/
[49]: https://github.com/basepom/duplicate-finder-maven-plugin
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
[65]: http://maven.apache.org/plugins/maven-resources-plugin/
[66]: http://maven.apache.org/plugins/maven-install-plugin/
[67]: http://maven.apache.org/plugins/maven-deploy-plugin/
[68]: http://maven.apache.org/plugins/maven-site-plugin/
[69]: https://registry.npmjs.org/@exasol/extension-manager-interface/-/extension-manager-interface-0.1.15.tgz
