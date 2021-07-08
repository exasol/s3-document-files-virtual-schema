package com.exasol.adapter.document.files;

import static com.exasol.adapter.document.UdfEntryPoint.*;
import static com.exasol.adapter.document.files.S3DocumentFilesAdapter.ADAPTER_NAME;

import java.io.InputStream;
import java.nio.file.Path;
import java.sql.*;
import java.util.*;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

import com.exasol.adapter.document.UdfEntryPoint;
import com.exasol.adapter.document.files.s3testsetup.S3TestSetup;
import com.exasol.bucketfs.Bucket;
import com.exasol.bucketfs.BucketAccessException;
import com.exasol.dbbuilder.dialects.DatabaseObject;
import com.exasol.dbbuilder.dialects.exasol.*;
import com.exasol.dbbuilder.dialects.exasol.udf.UdfScript;
import com.exasol.exasoltestsetup.ExasolTestSetup;
import com.exasol.exasoltestsetup.ServiceAddress;
import com.exasol.exasoltestsetup.testcontainers.ExasolTestcontainerTestSetup;
import com.exasol.udfdebugging.UdfTestSetup;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;

public class IntegrationTestSetup implements AutoCloseable {
    private static final String ADAPTER_JAR = "document-files-virtual-schema-dist-2.0.0-SNAPSHOT-s3-1.1.0.jar";
    public final String s3BucketName;
    private final ExasolTestSetup exasolTestSetup = new ExasolTestcontainerTestSetup();
    private final Connection connection;
    private final Statement statement;
    private final ExasolObjectFactory exasolObjectFactory;
    private final AdapterScript adapterScript;
    private final ConnectionDefinition connectionDefinition;
    private final Bucket bucket;
    private final List<DatabaseObject> createdObjects = new LinkedList<>();
    private final S3TestSetup s3TestSetup;
    private final S3Client s3;

    public IntegrationTestSetup(final S3TestSetup s3TestSetup, final String s3BucketName)
            throws SQLException, InterruptedException, BucketAccessException, TimeoutException {
        this.s3TestSetup = s3TestSetup;
        this.s3BucketName = s3BucketName;
        this.connection = this.exasolTestSetup.createConnection();
        this.statement = this.connection.createStatement();
        final UdfTestSetup udfTestSetup = new UdfTestSetup(this.exasolTestSetup);
        final List<String> jvmOptions = new ArrayList<>(Arrays.asList(udfTestSetup.getJvmOptions()));
        jvmOptions.add("-Xmx1g");
        this.exasolObjectFactory = new ExasolObjectFactory(this.connection,
                ExasolObjectConfiguration.builder().withJvmOptions(jvmOptions.toArray(String[]::new)).build());
        final ExasolSchema adapterSchema = this.exasolObjectFactory.createSchema("ADAPTER");
        this.bucket = this.exasolTestSetup.getDefaultBucket();
        this.connectionDefinition = getConnectionDefinition();
        this.adapterScript = createAdapterScript(adapterSchema);
        createUdf(adapterSchema);
        this.s3 = this.s3TestSetup.getS3Client();
    }

    private static void createUdf(final ExasolSchema adapterSchema) {
        adapterSchema.createUdfBuilder("IMPORT_FROM_S3_DOCUMENT_FILES").language(UdfScript.Language.JAVA)
                .inputType(UdfScript.InputType.SET).parameter(PARAMETER_DOCUMENT_FETCHER, "VARCHAR(2000000)")
                .parameter(PARAMETER_SCHEMA_MAPPING_REQUEST, "VARCHAR(2000000)")
                .parameter(PARAMETER_CONNECTION_NAME, "VARCHAR(500)").emits()
                .bucketFsContent(UdfEntryPoint.class.getName(), "/buckets/bfsdefault/default/" + ADAPTER_JAR).build();
    }

    private ConnectionDefinition getConnectionDefinition() {
        return this.exasolObjectFactory.createConnectionDefinition("S3_CONNECTION", getS3Address(),
                this.s3TestSetup.getUsername(), this.s3TestSetup.getPassword());
    }

    private String getS3Address() {
        final String inDatabaseAddress = getInDatabaseAddress();
        return "http://" + this.s3BucketName + ".s3." + this.s3TestSetup.getRegion() + "." + inDatabaseAddress + "/";
    }

    private String getInDatabaseAddress() {
        final String s3Entrypoint = this.s3TestSetup.getEntrypoint();
        if (s3Entrypoint.contains(":")) {
            return this.exasolTestSetup.makeTcpServiceAccessibleFromDatabase(ServiceAddress.parse(s3Entrypoint))
                    .toString();
        } else {
            return s3Entrypoint;
        }
    }

    private AdapterScript createAdapterScript(final ExasolSchema adapterSchema)
            throws InterruptedException, BucketAccessException, TimeoutException {
        this.bucket.uploadFile(Path.of("target", ADAPTER_JAR), ADAPTER_JAR);
        return adapterSchema.createAdapterScriptBuilder("FILES_ADAPTER")
                .bucketFsContent("com.exasol.adapter.RequestDispatcher", "/buckets/bfsdefault/default/" + ADAPTER_JAR)
                .language(AdapterScript.Language.JAVA).build();
    }

    public void emptyS3Bucket() {
        final ListObjectsResponse listObjectsResponse = this.s3
                .listObjects(builder -> builder.bucket(this.s3BucketName));
        listObjectsResponse.contents().forEach(
                s3Object -> this.s3.deleteObject(builder -> builder.bucket(this.s3BucketName).key(s3Object.key())));
    }

    @Override
    public void close() throws Exception {
        try {
            this.statement.close();
            this.connection.close();
            this.exasolTestSetup.close();
        } catch (final SQLException exception) {
            // at least we tried to close it
        }
    }

    public Statement getStatement() {
        return this.statement;
    }

    public S3Client getS3Client() {
        return this.s3;
    }

    protected VirtualSchema createVirtualSchema(final String schemaName, final Supplier<InputStream> mapping) {
        try {
            this.bucket.uploadInputStream(mapping, "mapping.json");
            final VirtualSchema virtualSchema = getPreconfiguredVirtualSchemaBuilder(schemaName)
                    .properties(Map.of("MAPPING", "/bfsdefault/default/mapping.json")).build();
            this.createdObjects.add(virtualSchema);
            return virtualSchema;
        } catch (final InterruptedException | BucketAccessException | TimeoutException exception) {
            throw new IllegalStateException("Failed to create Virtual Schema.", exception);
        }
    }

    public void dropCreatedObjects() {
        for (final DatabaseObject createdObject : this.createdObjects) {
            createdObject.drop();
        }
        this.createdObjects.clear();
    }

    public Bucket getBucket() {
        return this.bucket;
    }

    public ExasolObjectFactory getExasolObjectFactory() {
        return this.exasolObjectFactory;
    }

    public VirtualSchema.Builder getPreconfiguredVirtualSchemaBuilder(final String schemaName) {
        return this.exasolObjectFactory.createVirtualSchemaBuilder(schemaName)
                .connectionDefinition(this.connectionDefinition).adapterScript(this.adapterScript)
                .dialectName(ADAPTER_NAME);
    }

    public S3TestSetup getS3TestSetup() {
        return this.s3TestSetup;
    }
}
