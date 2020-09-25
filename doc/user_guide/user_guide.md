# User Guide

This user guide helps you with getting started with the S3 Files Virtual Schemas.

### Installation
 
Upload the latest available [release of this adapter](https://github.com/exasol/s3-document-files-virtual-schema/releases) to BucketFS.
See [Create a bucket in BucketFS](https://docs.exasol.com/administration/on-premise/bucketfs/create_new_bucket_in_bucketfs_service.htm) and [Upload the driver to BucketFS](https://docs.exasol.com/administration/on-premise/bucketfs/accessfiles.htm) for details.

Then create a schema to hold the adapter script.

```
CREATE SCHEMA ADAPTER;
```

Next create the Adapter Script:
 ```
CREATE OR REPLACE JAVA ADAPTER SCRIPT ADAPTER.S3_FS_FILES_ADAPTER AS
    %scriptclass com.exasol.adapter.RequestDispatcher;
    %jar /buckets/bfsdefault/default/document-files-virtual-schema-dist-0.2.0-SNAPSHOT-s3-0.1.0.jar;
/
```

In addition to the adapter script you need to create a UDF function that will handle the loading of the data:
```
CREATE OR REPLACE JAVA SET SCRIPT ADAPTER.IMPORT_FROM_S3_DOCUMENT_FILES(
  DATA_LOADER VARCHAR(2000000),
  REMOTE_TABLE_QUERY VARCHAR(2000000),
  CONNECTION_NAME VARCHAR(500))
  EMITS(...) AS
    %scriptclass com.exasol.adapter.document.UdfEntryPoint;
    %jar /buckets/bfsdefault/default/document-files-virtual-schema-dist-0.2.0-SNAPSHOT-s3-0.1.0.jar;
/
```

## Creating a Connection
 
Now you need to define a connection that includes the location of stored files:

 ```
CREATE CONNECTION S3_CONNECTION
    TO 'AWS URI'
    USER 'AWS ACCESS KEY'
    IDENTIFIED BY 'AWS SECRET KEY';
``` 

The address (`TO`) must have one of these formats:

* `https://BUCKET.s3.REGION.amazonaws.com/KEY`
* `http(s)://BUCKET.s3.REGION.CUSTOM_ENDPOINT/KEY`

You can not use two factor authentication (session credentials) with this adapter.
Instead you need to create a machine account without two factor authentication.


The adapter uses the `KEY` defined here as a prefix for all keys that you define in the `source` field of the mapping definition.
Typically you leave it empty here.  

## Defining the Schema Mapping

Before creating a Virtual Schema you need to create a mapping definition that defines how the document data is mapped to Exasol tables.

For that we use the Exasol Document Mapping Language (EDML). It is universal over all document Virtual Schemas. 
To learn how to  define such EDML definitions check the [user guide in the common repository for all document Virtual Schemas](https://github.com/exasol/virtual-schema-common-document/doc/user_guide/edml_user_guide.md).

In the definitions you have to define the `source` property. 
For S3, you there define the key of the files to load.
Keep in mind the tha adapter prepends the key prefix you defined in the CONNECTION.

This Virtual Schema adapter automatically detects the type of the document file by the file extension.
You can find a list of supported file types and their extensions in the [user guide of the common repository for all file Virtual Schemas](https://github.com/exasol/virtual-schema-common-document-files/doc/user_guide/user_guide.md).

### Mapping multiple files

For some file type (for example JSON) each source file contains only a single document. 
That means, that you have one file for each row in the mapped table.
To define mappings for such types, you can use the GLOB syntax.
That means, you can use `*` and `?` as wildcards, where `*` matches multiple characters and `?` a single one.


## Creating the Virtual Schema

Finally create the Virtual Schema using:

```
CREATE VIRTUAL SCHEMA FILES_VS_TEST USING ADAPTER.S3_FILES_ADAPTER WITH
    CONNECTION_NAME = 'S3_CONNECTION'
    SQL_DIALECT     = 'S3_DOCUMENT_FILES'
    MAPPING         = '/bfsdefault/default/path/to/mappings/in/bucketfs';
```

The `CREATE VIRTUAL SCHEMA` command accepts the following properties:

| Property          | Mandatory   |  Default      |   Description                                                                   |
|-------------------|-------------|---------------|---------------------------------------------------------------------------------|
|`MAPPING`          | Yes         |               | Path to the mapping definition file(s)                                          |
|`MAX_PARALLEL_UDFS`| No          | -1            | Maximum number of UDFs that are executed in parallel. -1 represents unlimited. *| 
 
 \* The adapter will start at most one UDF per input file. 
 That means, if data from a single file (for example a JSON-Lines file) is loaded, it will not parallelize.
 
Now browse the data using your favorite SQL client.
