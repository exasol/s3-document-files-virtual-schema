# Developers Guide

This guide contains information for developers.

## Configuring Local Test Setup

The integration tests of this project use an S3 bucket. Using a local mock was not possible since the tested local-stack s3 implementation did not work.

For that reason you need to configure the test. For that create a `test_config.yml` file:

```yml
awsProfile: <AWS profile>
owner: <your email used for exa:owner tag>
```

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