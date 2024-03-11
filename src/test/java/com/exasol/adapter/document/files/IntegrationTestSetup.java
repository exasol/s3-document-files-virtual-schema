package com.exasol.adapter.document.files;

import static com.exasol.adapter.document.GenericUdfCallHandler.*;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.sql.*;
import java.util.*;
import java.util.concurrent.TimeoutException;

import org.jetbrains.annotations.NotNull;

import com.exasol.adapter.document.UdfEntryPoint;
import com.exasol.adapter.document.files.s3testsetup.S3TestSetup;
import com.exasol.bucketfs.Bucket;
import com.exasol.bucketfs.BucketAccessException;
import com.exasol.dbbuilder.dialects.DatabaseObject;
import com.exasol.dbbuilder.dialects.exasol.*;
import com.exasol.dbbuilder.dialects.exasol.udf.UdfScript;
import com.exasol.exasoltestsetup.ExasolTestSetup;
import com.exasol.exasoltestsetup.ExasolTestSetupFactory;
import com.exasol.udfdebugging.UdfTestSetup;

import jakarta.json.*;
import software.amazon.awssdk.services.s3.S3Client;

public class IntegrationTestSetup implements AutoCloseable {
    public static final Path CLOUD_SETUP_CONFIG = Path.of("cloudSetup/generated/testConfig.json");
    public static final String ADAPTER_JAR = "document-files-virtual-schema-dist-8.0.1-s3-3.0.3.jar";
    public static final Path ADAPTER_JAR_LOCAL_PATH = Path.of("target", ADAPTER_JAR);
    public final String s3BucketName;
    private final ExasolTestSetup exasolTestSetup = new ExasolTestSetupFactory(CLOUD_SETUP_CONFIG).getTestSetup();
    private final Connection connection;
    private final Statement statement;
    private final ExasolObjectFactory exasolObjectFactory;
    private final AdapterScript adapterScript;
    private ConnectionDefinition connectionDefinition;
    private final Bucket bucket;
    private final List<DatabaseObject> createdObjects = new LinkedList<>();
    private final S3TestSetup s3TestSetup;
    private final UdfTestSetup udfTestSetup;

    public IntegrationTestSetup(final S3TestSetup s3TestSetup, final String s3BucketName)
            throws SQLException, BucketAccessException, TimeoutException, FileNotFoundException {
        this.s3TestSetup = s3TestSetup;
        this.s3BucketName = s3BucketName;
        this.connection = this.exasolTestSetup.createConnection();
        this.statement = this.connection.createStatement();
        this.statement.executeUpdate("ALTER SESSION SET QUERY_CACHE = 'OFF';");
        this.bucket = this.exasolTestSetup.getDefaultBucket();
        this.udfTestSetup = new UdfTestSetup(this.exasolTestSetup, this.connection);
        final List<String> jvmOptions = new ArrayList<>(Arrays.asList(this.udfTestSetup.getJvmOptions()));
        this.exasolObjectFactory = new ExasolObjectFactory(this.connection,
                ExasolObjectConfiguration.builder().withJvmOptions(jvmOptions.toArray(String[]::new)).build());
        final ExasolSchema adapterSchema = this.exasolObjectFactory.createSchema("ADAPTER");
        this.connectionDefinition = createConnectionDefinition();
        this.adapterScript = createAdapterScript(adapterSchema);
        createUdf(adapterSchema);
    }

    static UdfScript createUdf(final ExasolSchema adapterSchema) {
        return adapterSchema.createUdfBuilder("IMPORT_FROM_S3_DOCUMENT_FILES").language(UdfScript.Language.JAVA)
                .inputType(UdfScript.InputType.SET).parameter(PARAMETER_DOCUMENT_FETCHER, "VARCHAR(2000000)")
                .parameter(PARAMETER_SCHEMA_MAPPING_REQUEST, "VARCHAR(2000000)")
                .parameter(PARAMETER_CONNECTION_NAME, "VARCHAR(500)").emits()
                .bucketFsContent(UdfEntryPoint.class.getName(), getAdapterJarInBucketFs()).build();
    }

    @NotNull
    private static String getAdapterJarInBucketFs() {
        return "/buckets/bfsdefault/default/" + ADAPTER_JAR;
    }

    public InetSocketAddress makeLocalServiceAvailableInExasol(final int port) {
        return this.exasolTestSetup.makeLocalTcpServiceAccessibleFromDatabase(port);
    }

    private ConnectionDefinition createConnectionDefinition() {
        final JsonObjectBuilder configJson = getConnectionConfig();
        return createConnectionDefinition(configJson);
    }

    public JsonObjectBuilder getConnectionConfig() {
        final JsonObjectBuilder builder = Json.createObjectBuilder()//
                .add("awsRegion", this.s3TestSetup.getRegion())//
                .add("s3Bucket", this.s3BucketName)//
                .add("awsAccessKeyId", this.s3TestSetup.getUsername())//
                .add("awsSecretAccessKey", this.s3TestSetup.getPassword());
        this.s3TestSetup.getMfaToken().ifPresent(s -> builder.add("awsSessionToken", s));
        this.getInDatabaseS3Address().ifPresent(address -> builder.add("awsEndpointOverride", address.toString()));
        return builder;
    }

    public ConnectionDefinition createConnectionDefinition(final JsonObjectBuilder details) {
        return this.exasolObjectFactory.createConnectionDefinition("S3_CONNECTION_" + System.currentTimeMillis(), "",
                "", toJson(details.build()));
    }

    private String toJson(final JsonObject configJson) {
        try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                final JsonWriter writer = Json.createWriter(outputStream)) {
            writer.write(configJson);
            return outputStream.toString(StandardCharsets.UTF_8);
        } catch (final IOException exception) {
            throw new UncheckedIOException("Failed to serialize connection settings", exception);
        }
    }

    private Optional<String> getInDatabaseS3Address() {
        return this.s3TestSetup.getEntrypoint()
                .map(address -> this.exasolTestSetup.makeTcpServiceAccessibleFromDatabase(address))
                .map(InetSocketAddress::toString);
    }

    AdapterScript createAdapterScript(final ExasolSchema adapterSchema)
            throws BucketAccessException, TimeoutException, FileNotFoundException {
        this.bucket.uploadFile(ADAPTER_JAR_LOCAL_PATH, ADAPTER_JAR);
        return adapterSchema.createAdapterScriptBuilder("FILES_ADAPTER")
                .bucketFsContent("com.exasol.adapter.RequestDispatcher", getAdapterJarInBucketFs())
                .language(AdapterScript.Language.JAVA).build();
    }

    public void emptyS3Bucket() {
        emptyS3Bucket(this.s3BucketName);
    }

    public void emptyS3Bucket(final String bucketName) {
        this.s3TestSetup.emptyS3Bucket(bucketName);
    }

    @Override
    public void close() throws Exception {
        try {
            this.udfTestSetup.close();
            this.statement.close();
            this.connection.close();
            this.exasolTestSetup.close();
        } catch (final SQLException exception) {
            // at least we tried to close it
        }
    }

    public S3Client getS3Client() {
        return this.s3TestSetup.getS3Client();
    }

    protected VirtualSchema createVirtualSchema(final String schemaName, final String mapping) {
        return createVirtualSchema(schemaName, mapping, this.connectionDefinition);
    }

    protected VirtualSchema createVirtualSchema(final String schemaName, final String mapping,
            final ConnectionDefinition connection) {
        final VirtualSchema virtualSchema = getPreconfiguredVirtualSchemaBuilder(schemaName)
                .connectionDefinition(connection)//
                .properties(getVirtualSchemaProperties(mapping)).build();
        this.createdObjects.add(virtualSchema);
        return virtualSchema;
    }

    /**
     * Old version added property "DEBUG_ADDRESS" with value "127.0.0.1:3001". Recommendation is to use default
     * properties provided by test-db-builder-java:
     * <ul>
     * <li>com.exasol.virtualschema.debug.host</li>
     * <li>com.exasol.virtualschema.debug.port</li>
     * <li>com.exasol.virtualschema.debug.level</li>
     * </ul>
     *
     * @param mapping must be non null
     * @return map
     */
    @NotNull
    private Map<String, String> getVirtualSchemaProperties(final String mapping) {
        final Map<String, String> properties = new HashMap<>(Map.of("MAPPING", mapping));
        final String debugProperty = System.getProperty("test.debug", "");
        final String profileProperty = System.getProperty("test.jprofiler", "");
        if (!debugProperty.isBlank() || !profileProperty.isBlank()) {
            properties.put("MAX_PARALLEL_UDFS", "1");
        }
        return properties;
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
                .connectionDefinition(this.connectionDefinition).adapterScript(this.adapterScript);
    }

    public Connection getConnection() {
        return this.connection;
    }

    public ConnectionDefinition getConnectionDefinition() {
        return this.connectionDefinition;
    }

    public Statement getStatement() {
        return this.statement;
    }

    public void setConnectionDefinition(final ConnectionDefinition connectionDefinition) {
        this.connectionDefinition = connectionDefinition;
    }
}
