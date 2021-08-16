# Developers Guide

This guide contains information for developers.

## Running Regression Test

This project contains some regression tests to monitor the performance cross releases. To run them locally use:

```shell
mvn verify -P regressionTests
```

However, a local run won't give you reliable numbers, since it's dependent on your local hardware configuration.