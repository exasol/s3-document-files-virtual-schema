# User Guide

This user guide helps you with getting started with the S3 Files Virtual Schemas.

### Installation

Upload the latest available [release of this adapter](https://github.com/exasol/s3-document-files-virtual-schema/releases) to BucketFS. See the Exasol documentation for details: [Create a bucket in BucketFS](https://docs.exasol.com/administration/on-premise/bucketfs/create_new_bucket_in_bucketfs_service.htm), [Access Files in BucketFS](https://docs.exasol.com/administration/on-premise/bucketfs/accessfiles.htm).

Then create a schema to hold the adapter script.

```sql
CREATE SCHEMA ADAPTER;
```

Next create the Adapter Script:

```sql
CREATE OR REPLACE JAVA ADAPTER SCRIPT ADAPTER.S3_FILES_ADAPTER AS
    %scriptclass com.exasol.adapter.RequestDispatcher;
    %jar /buckets/bfsdefault/default/document-files-virtual-schema-dist-7.3.6-s3-2.8.2.jar;
/
```

In addition to the adapter script you need to create a UDF function that will handle the loading of the data:

```sql
CREATE OR REPLACE JAVA SET SCRIPT ADAPTER.IMPORT_FROM_S3_DOCUMENT_FILES(
  DATA_LOADER VARCHAR(2000000),
  SCHEMA_MAPPING_REQUEST VARCHAR(2000000),
  CONNECTION_NAME VARCHAR(500))
  EMITS(...) AS
    %scriptclass com.exasol.adapter.document.UdfEntryPoint;
    %jar /buckets/bfsdefault/default/document-files-virtual-schema-dist-7.3.6-s3-2.8.2.jar;
/
```

## Creating a Connection

Now you need to define a connection that includes the location of stored files:

```sql
CREATE CONNECTION S3_CONNECTION
    TO ''
    USER ''
    IDENTIFIED BY '{
        "awsAccessKeyId": "<AWS ACCESS KEY ID>", 
        "awsSecretAccessKey": "<AWS SECRET KEY ID>", 
        "awsRegion": "<AWS REGION>", 
        "s3Bucket": "<S3 BUCKET NAME>" 
    }';
``` 

The connection stores all connection details as JSON in the `IDENTIFIED BY` part. There you can use the following keys:

| Key                   | Default        |  Required  | Example                  |
|-----------------------|----------------|:----------:|--------------------------|
| `awsAccessKeyId`      |                |     ✓      | `"ABCDABCDABCDABCD1234"` |
| `awsSecretAccessKey`  |                |     ✓      |                          |
| `awsRegion`           |                |     ✓      | `"eu-central-1"`         |
| `s3Bucket`            |                |     ✓      | `"my-s3-bucket"`         |
| `awsSessionToken`     |                |     ✘      |                          |
| `awsEndpointOverride` | _AWS endpoint_ |     ✘      | `"s3.my-company.de"`     |
| `s3PathStyleAccess`   | `false`        |     ✘      | `true`                   |
| `useSsl`              | `true`         |     ✘      | `false`                  |

By setting `awsSessionToken` you can use two-factor authentication with this Virtual Schema adapter. However, please keep in mind that the token will expire within few hours. So usually it's better to create a machine user without two-factor authentication enabled.

This adapter currently support S3 path-style-access. However, the feature is deprecated by AWS. In case AWS will stop supporting it in their SDK we will also stop supporting it.

## Defining the Schema Mapping

Before creating a Virtual Schema you need to create a mapping definition that defines how the document data is mapped to Exasol tables.

For that we use the Exasol Document Mapping Language (EDML). It is universal over all document Virtual Schemas. To learn how to define such EDML definitions check the [user guide in the common repository for all document Virtual Schemas](https://github.com/exasol/virtual-schema-common-document/blob/main/doc/user_guide/edml_user_guide.md).

In the definitions you have to define the `source` property. For S3, you define the key (S3 term for file identifier) of the files to load.

This Virtual Schema adapter automatically detects the type of the document file by the file extension. You can find a list of supported file types and their extensions in the [user guide of the common repository for all file Virtual Schemas](https://github.com/exasol/virtual-schema-common-document-files/blob/main/doc/user_guide/user_guide.md).

### Mapping multiple files

For some file type (for example JSON) each source file contains only a single document. That means, that you have one file for each row in the mapped table. To define mappings for such types, you can use the GLOB syntax. That means, you can use `*` and `?` as wildcards, where `*` matches multiple characters and `?` a single one.

## Creating the Virtual Schema

Finally, create the Virtual Schema using:

```sql
CREATE VIRTUAL SCHEMA FILES_VS_TEST USING ADAPTER.S3_FILES_ADAPTER WITH
    CONNECTION_NAME = 'S3_CONNECTION'
    MAPPING         = '/bfsdefault/default/path/to/mappings/in/bucketfs';
```

The `CREATE VIRTUAL SCHEMA` command accepts the following properties:

| Property          | Mandatory   |  Default      |   Description                                                                   |
|-------------------|-------------|---------------|---------------------------------------------------------------------------------|
|`MAPPING`          | Yes         |               | Path to the mapping definition file(s)                                          |
|`MAX_PARALLEL_UDFS`| No          | -1            | Maximum number of UDFs that are executed in parallel. -1 represents unlimited. *| 


Now browse the data using your favorite SQL client.

## Known Issues

* Certain virtual-schema queries can cause a database crash. For details see [#41](https://github.com/exasol/virtual-schema-common-document-files/issues/41).

## Troubleshooting

### Error When Creating the Virtual Schema

When you get the following error when executing the `CREATE VIRTUAL SCHEMA` statement

```
VM error: F-UDF-CL-LIB-1125: F-UDF-CL-SL-JAVA-1000: F-UDF-CL-SL-JAVA-1038: End of %scriptclass statement not found
```

then probably your SQL client interpreted the semicolon `;` to end the current SQL statement.
To avoid this, simply change your SQL statement to ensure the SQL client runs the complete statement for creating the adapter script by 
* prepending a line `--/`
* and appending `/`

In DbVisualizer use exactly this command:

```sql
--/
CREATE OR REPLACE JAVA ADAPTER SCRIPT ADAPTER.S3_FILES_ADAPTER AS
   %scriptclass com.exasol.adapter.RequestDispatcher;
   %jar /buckets/bfsdefault/default/vs/document-files-virtual-schema-dist-7.3.6-s3-2.8.2.jar;
/
```

You can check the actual content of the script using this query:

```sql
SELECT SCRIPT_NAME, SCRIPT_TEXT FROM EXA_ALL_SCRIPTS;
```

Column `SCRIPT_TEXT` must contain the complete statement including `%jar /buckets/*.jar`.

### Error When Executing a Query in an S3 Virtual Schema

When you get the following error

```
com.exasol.ExaUDFException: F-UDF-CL-SL-JAVA-1068: Exception during singleCall adapterCall 
software.amazon.awssdk.services.s3.model.S3Exception: The bucket you are attempting to access must be addressed using the specified endpoint. Please send all future requests to this endpoint.
```

then please make sure to use the correct AWS region in field `awsRegion` when creating the connection for S3.
