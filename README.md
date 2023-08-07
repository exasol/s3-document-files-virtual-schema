# Virtual Schema for Document Files in S3

[![Build Status](https://github.com/exasol/s3-document-files-virtual-schema/actions/workflows/ci-build.yml/badge.svg)](https://github.com/exasol/s3-document-files-virtual-schema/actions/workflows/ci-build.yml)

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3As3-document-files-virtual-schema&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.exasol%3As3-document-files-virtual-schema)

[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3As3-document-files-virtual-schema&metric=security_rating)](https://sonarcloud.io/dashboard?id=com.exasol%3As3-document-files-virtual-schema)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3As3-document-files-virtual-schema&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=com.exasol%3As3-document-files-virtual-schema)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3As3-document-files-virtual-schema&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=com.exasol%3As3-document-files-virtual-schema)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3As3-document-files-virtual-schema&metric=sqale_index)](https://sonarcloud.io/dashboard?id=com.exasol%3As3-document-files-virtual-schema)

[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3As3-document-files-virtual-schema&metric=code_smells)](https://sonarcloud.io/dashboard?id=com.exasol%3As3-document-files-virtual-schema)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3As3-document-files-virtual-schema&metric=coverage)](https://sonarcloud.io/dashboard?id=com.exasol%3As3-document-files-virtual-schema)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3As3-document-files-virtual-schema&metric=duplicated_lines_density)](https://sonarcloud.io/dashboard?id=com.exasol%3As3-document-files-virtual-schema)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3As3-document-files-virtual-schema&metric=ncloc)](https://sonarcloud.io/dashboard?id=com.exasol%3As3-document-files-virtual-schema)

This Virtual Schemas allows you to access document files stored in S3 like any regular Exasol table.

This Virtual Schema is built for and tested with the official AWS S3. Third-party S3 API compatible products are expected to work as well. It is highly recommended to thoroughly test 3rd party products used in combination with Exasol, especially regarding sufficient S3 API compatibility.

For [MinIO](https://min.io) each release of VSS3 is verified by automated integration tests. MinIO is a high-performance, S3 compatible object store. It is built for large scale AI/ML, data lake and database workloads. It runs on-prem and on any cloud (public or private) and from the data center to the edge. MinIO is software-defined and open source under GNU AGPL v3. See the [Hand-On Guide](doc/hands_on/hands_on_minio.md) for a quick tour with some sample JSON files.

This Virtual Schema is prepared for [Java UDF startup time improver](https://github.com/exasol/java-udf-startup-time-improver/).

For supported document file formats see [Virtual Schema Common Document Files](https://github.com/exasol/virtual-schema-common-document-files).

Additional Information:

* [User Guide](doc/user_guide/user_guide.md)
* [Hands-on Guide (JSON files)](doc/hands_on/hands_on.md)
* [Hands-on Guide (Parquet files)](doc/hands_on/hands_on_parquet.md)
* [Hands-on Guide (MinIO with JSON files)](doc/hands_on/hands_on_minio.md)
* [Changelog](doc/changes/changelog.md)
* [Dependencies](dependencies.md)
* [Developer Guide](doc/developer_guide/developer_guide.md)
