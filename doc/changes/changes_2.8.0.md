# S3 Document Files Virtual Schema 2.8.0, released 2023-??-??

Code name: Upgrade Extension

## Summary

This release updates the extension so that it supports categories and upgrading an installed S3 virtual schema to the latest version.

## Features

* #121: Implemented upgrading installed extension

## Dependency Updates

### Virtual Schema for Document Data in Files on AWS S3

#### Compile Dependency Updates

* Updated `software.amazon.awssdk:s3:2.20.111` to `2.20.122`

#### Test Dependency Updates

* Updated `com.amazonaws:aws-java-sdk-s3:1.12.514` to `1.12.525`
* Updated `com.exasol:extension-manager-integration-test-java:0.4.0` to `0.5.0`
* Updated `com.exasol:java-class-list-verifier:0.2.2` to `0.2.3`
* Updated `nl.jqno.equalsverifier:equalsverifier:3.15` to `3.15.1`

#### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:2.9.9` to `2.9.10`
* Updated `org.apache.maven.plugins:maven-clean-plugin:3.2.0` to `3.3.1`

### Extension

#### Compile Dependency Updates

* Updated `@exasol/extension-manager-interface:0.1.15` to `0.3.0`

#### Development Dependency Updates

* Updated `eslint:^8.38.0` to `^8.46.0`
* Updated `@typescript-eslint/parser:^5.58.0` to `^6.3.0`
* Updated `ts-jest:^29.1.0` to `^29.1.1`
* Updated `@types/jest:^29.5.0` to `^29.5.3`
* Updated `typescript:^5.0.4` to `^5.1.6`
* Updated `@typescript-eslint/eslint-plugin:^5.58.0` to `^6.3.0`
* Updated `jest:29.5.0` to `29.6.2`
* Updated `esbuild:^0.17.16` to `^0.19.0`
