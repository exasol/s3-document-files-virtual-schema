package com.exasol.adapter.document.files;

import static com.exasol.adapter.document.UdfEntryPoint.*;
import static com.exasol.adapter.document.files.S3DocumentFilesAdapter.ADAPTER_NAME;

import java.io.*;
import java.nio.charset.StandardCharsets;
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
import com.exasol.exasoltestsetup.*;
import com.exasol.udfdebugging.UdfTestSetup;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.paginators.ListObjectsV2Iterable;

public class IntegrationTestSetup implements AutoCloseable {
    private static final String ADAPTER_JAR = "document-files-virtual-schema-dist-3.0.0-s3-1.5.0.jar";
    public final String s3BucketName;
    private final ExasolTestSetup exasolTestSetup = new ExasolTestSetupFactory(
            Path.of("cloudSetup/generated/testConfig.json")).getTestSetup();
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
            throws SQLException, BucketAccessException, TimeoutException, FileNotFoundException {
        this.s3TestSetup = s3TestSetup;
        this.s3BucketName = s3BucketName;
        this.connection = this.exasolTestSetup.createConnection();
        this.statement = this.connection.createStatement();
        this.statement.executeUpdate("ALTER SESSION SET QUERY_CACHE = 'OFF';");
        final UdfTestSetup udfTestSetup = new UdfTestSetup(this.exasolTestSetup);
        this.exasolObjectFactory = new ExasolObjectFactory(this.connection,
                ExasolObjectConfiguration.builder().withJvmOptions(udfTestSetup.getJvmOptions()).build());
        final ExasolSchema adapterSchema = this.exasolObjectFactory.createSchema("ADAPTER");
        this.bucket = this.exasolTestSetup.getDefaultBucket();
        this.connectionDefinition = getConnectionDefinition();
        this.adapterScript = createAdapterScript(adapterSchema);
        createUdf(adapterSchema);
        this.s3 = this.s3TestSetup.getS3Client();
        if (System.getProperty("test.udf-log", "false").equals("true")) {
            getStatement().executeUpdate("ALTER SESSION SET SCRIPT_OUTPUT_ADDRESS = '127.0.0.1:3000';");
        }
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
        final String inDatabaseAddress = getInDatabaseS3Address();
        return "http://" + this.s3BucketName + ".s3." + this.s3TestSetup.getRegion() + "." + inDatabaseAddress + "/";
    }

    private String getInDatabaseS3Address() {
        final String s3Entrypoint = this.s3TestSetup.getEntrypoint();
        if (s3Entrypoint.contains(":")) {
            return this.exasolTestSetup.makeTcpServiceAccessibleFromDatabase(ServiceAddress.parse(s3Entrypoint))
                    .toString();
        } else {
            return s3Entrypoint;
        }
    }

    private AdapterScript createAdapterScript(final ExasolSchema adapterSchema)
            throws BucketAccessException, TimeoutException, FileNotFoundException {
        this.bucket.uploadFile(Path.of("target", ADAPTER_JAR), ADAPTER_JAR);
        return adapterSchema.createAdapterScriptBuilder("FILES_ADAPTER")
                .bucketFsContent("com.exasol.adapter.RequestDispatcher", "/buckets/bfsdefault/default/" + ADAPTER_JAR)
                .language(AdapterScript.Language.JAVA).build();
    }

    public void emptyS3Bucket() {
        final ListObjectsV2Iterable pages = this.s3
                .listObjectsV2Paginator(request -> request.bucket(this.s3BucketName));
        for (final ListObjectsV2Response page : pages) {
            page.contents().forEach(
                    s3Object -> this.s3.deleteObject(builder -> builder.bucket(this.s3BucketName).key(s3Object.key())));
        }
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

    protected VirtualSchema createVirtualSchema(final String schemaName, final Supplier<InputStream> mapping)
            throws IOException {
        try (final InputStream stream = mapping.get()) {
            return createVirtualSchema(schemaName, new String(stream.readAllBytes(), StandardCharsets.UTF_8));
        }
    }

    protected VirtualSchema createVirtualSchema(final String schemaName, final String mapping) {
        return createVirtualSchema(schemaName, mapping, this.connectionDefinition);
    }

    protected VirtualSchema createVirtualSchema(final String schemaName, final String mapping,
            final ConnectionDefinition connection) {
        try {
            this.bucket.uploadStringContent(mapping, "mapping.json");
            final VirtualSchema virtualSchema = getPreconfiguredVirtualSchemaBuilder(schemaName)
                    .connectionDefinition(connection)//
                    .properties(Map.of("MAPPING", "/bfsdefault/default/mapping.json")).build();
            this.createdObjects.add(virtualSchema);
            return virtualSchema;
        } catch (final BucketAccessException | TimeoutException | InterruptedException exception) {
            if (exception instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
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
