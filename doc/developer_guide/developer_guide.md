# Developer Guide

This guide contains information for developers.

## Building

In case of time outs for building the extension for the [Extension Manager](https://github.com/exasol/extension-manager) please build it manually with
```shell
cd extension
npm ci
```

See also [PK ticket #461](https://github.com/exasol/project-keeper/issues/461).


## Executing the Integration Tests Locally

The integration tests of this project use an S3 bucket. There are multiple ways to use or emulate S3 buckets:
* Real S3 buckets in the AWS cloud
* [Localstack](https://github.com/localstack/localstack)
* [MinIO](https://min.io)

For using real AWS S3 buckets you need to configure the tests appropriately by creating file `s3-document-files-virtual-schema/test_config.properties` with the following content:

```properties
awsProfile = <AWS profile>
owner = <your email used for exa:owner tag>
s3CacheBucket = <name of an s3 bucket that will be used for caching the test files (optional)>
```

### Executing Integration Tests on Windows

AWS command line client (AWS CLI) and the AWS Software Development Kit for Java (AWS SDK) are using different defaults for finding your HOME directory:

|                  | Default HOME directory | Documentation                                                               |
|------------------|------------------------|-----------------------------------------------------------------------------|
| AWS CLI          | `%USERPROFILE%`        | https://docs.aws.amazon.com/cli/latest/userguide/cli-configure-envvars.html |
| AWS SDK for Java | `%HOME%`               | https://docs.aws.amazon.com/sdkref/latest/guide/file-location.html          |

In order to make the CLI write the same files as the SDK will read later on
* either do not set environment variable `HOME`
* or set environment variable `HOME` appropriately
* or set environment variable `AWS_SHARED_CREDENTIALS_FILE` to `%USER_PROFILE%\.aws\credentials`
* or copy the files manually from `%USER_PROFILE%\.aws\` to `%HOME%\.aws\`

#### Download HADOOPUTILS

Hadoop requires native libraries on Windows e.g. for accessing the local filesystem via `file://` as Hadoop uses some Windows APIs to implement posix-like file access permissions. This is implemented in `HADOOP.DLL` and `WINUTILS.EXE`. If these files are not locatable in `%HADOOP_HOME%\BIN\WINUTILS.EXE` then Hadoop and applications built on top it will fail, see https://cwiki.apache.org/confluence/display/HADOOP2/WindowsProblems.

* download from https://github.com/steveloughran/winutils
* unzip to Folder `<FOLDER>\bin`
* set environment variable `HADOOP_HOME=<FOLDER>`

## Running Regression Test

This project contains some regression tests to monitor the performance cross releases. To run them locally use:

```shell
mvn verify -P regressionTests
```

However, a local run won't give you reliable numbers, since it's dependent on your local hardware configuration.

## Getting Debug Output

To get debug output of the UDFs, add the following lines to your tests:

```java
SETUP.getStatement().executeUpdate("ALTER SESSION SET SCRIPT_OUTPUT_ADDRESS = '127.0.0.1:3000';");
 ```

## Debugging & Profiling

You can use a remote debugger and profiler for this project's integration tests. To do so, use the system properties from [UDF debugging Java](https://github.com/exasol/udf-debugging-java/).

When you enable debugging or profiling, this project's test will set the UDF concurrency to 1. Debugging concurrent UDFs is currently not possible due to reverse connection.

## Working With the Extension

### Running Unit Tests

```shell
cd extension
npm install
npm run build && npm test
```

To run tests continuously each time a file is changed on disk (useful during development), start the following command:

```shell
npm run test-watch
```

### Running Integration Tests With a Local Extension Manager

To use a local, non-published version of the extension manager during integration tests, first checkout the [extension-manager](https://github.com/exasol/extension-manager). Then create file `extension-test.properties` in this project directory with the following content:

```properties
localExtensionManager = /path/to/extension-manager
```

This will build the extension manager from source and use the built executable for the integration tests.

### Running Linter

To run static code analysis for the extension code, run

```shell
cd extension
npm run lint
```

### Using a Local Extension Manager Interface

To use a local, non-published version of the extension manager interface during development, edit [extension/package.json](../../extension/package.json) and replace the version of `"@exasol/extension-manager-interface"` with the path to your local clone of [extension-manager-interface](https://github.com/exasol/extension-manager-interface).

Then run `npm install` and restart your IDE.

### Upgrade Extension Dependencies

```shell
cd extension
npx npm-check-updates -u && rm -rf ./node_modules/ && npm install
```
