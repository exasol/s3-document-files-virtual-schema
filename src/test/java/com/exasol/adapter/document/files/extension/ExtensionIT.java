package com.exasol.adapter.document.files.extension;

import static com.exasol.matcher.ResultSetStructureMatcher.table;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;

import java.io.FileNotFoundException;
import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import com.exasol.adapter.RequestDispatcher;
import com.exasol.adapter.document.GenericUdfCallHandler;
import com.exasol.adapter.document.UdfEntryPoint;
import com.exasol.adapter.document.edml.*;
import com.exasol.adapter.document.edml.serializer.EdmlSerializer;
import com.exasol.adapter.document.files.IntegrationTestSetup;
import com.exasol.adapter.document.files.s3testsetup.AwsS3TestSetup;
import com.exasol.bucketfs.BucketAccessException;
import com.exasol.dbbuilder.dialects.exasol.AdapterScript.Language;
import com.exasol.dbbuilder.dialects.exasol.ExasolSchema;
import com.exasol.dbbuilder.dialects.exasol.udf.UdfScript;
import com.exasol.dbbuilder.dialects.exasol.udf.UdfScript.InputType;
import com.exasol.exasoltestsetup.ExasolTestSetup;
import com.exasol.exasoltestsetup.ExasolTestSetupFactory;
import com.exasol.extensionmanager.client.model.ParameterValue;
import com.exasol.extensionmanager.itest.ExasolVersionCheck;
import com.exasol.extensionmanager.itest.ExtensionManagerSetup;
import com.exasol.extensionmanager.itest.base.AbstractVirtualSchemaExtensionIT;
import com.exasol.extensionmanager.itest.base.ExtensionITConfig;
import com.exasol.extensionmanager.itest.builder.ExtensionBuilder;
import com.exasol.mavenprojectversiongetter.MavenProjectVersionGetter;

import software.amazon.awssdk.core.sync.RequestBody;

class ExtensionIT extends AbstractVirtualSchemaExtensionIT {
    private static final Path EXTENSION_SOURCE_DIR = Paths.get("extension").toAbsolutePath();
    private static final String PROJECT_VERSION = MavenProjectVersionGetter.getCurrentProjectVersion();
    private static final String EXTENSION_ID = "s3-vs-extension.js";
    private static final String MAPPING_DESTINATION_TABLE = "DESTINATION_TABLE";

    private static ExasolTestSetup exasolTestSetup;
    private static ExtensionManagerSetup setup;
    private static AwsS3TestSetup s3TestSetup;
    private static String s3BucketName;
    private String s3ImportPrefix;

    @BeforeEach
    void createS3ImportPrefix() {
        s3ImportPrefix = "vs-works-test-" + System.currentTimeMillis() + "/";
    }

    @Override
    protected ExtensionITConfig createConfig() {
        final String previousVersion = "3.0.5";
        return ExtensionITConfig.builder().projectName("s3-document-files-virtual-schema") //
                .extensionId(EXTENSION_ID) //
                .currentVersion(PROJECT_VERSION) //
                .expectedParameterCount(13) //
                .extensionName("S3 Virtual Schema") //
                .extensionDescription("Virtual Schema for document files on AWS S3") //
                .previousVersion(previousVersion) //
                .previousVersionJarFile("document-files-virtual-schema-dist-8.0.3-s3-" + previousVersion + ".jar")
                .build();
    }

    @BeforeAll
    static void setup() throws FileNotFoundException, BucketAccessException, TimeoutException {
        if (System.getProperty("com.exasol.dockerdb.image") == null) {
            System.setProperty("com.exasol.dockerdb.image", "8.26.0");
        }
        exasolTestSetup = new ExasolTestSetupFactory(IntegrationTestSetup.CLOUD_SETUP_CONFIG).getTestSetup();
        ExasolVersionCheck.assumeExasolVersion8(exasolTestSetup);
        setup = ExtensionManagerSetup.create(exasolTestSetup, ExtensionBuilder.createDefaultNpmBuilder(
                EXTENSION_SOURCE_DIR, EXTENSION_SOURCE_DIR.resolve("dist").resolve(EXTENSION_ID)));
        s3TestSetup = new AwsS3TestSetup();
        s3BucketName = "extension-test.s3.virtual-schema-test-bucket-" + System.currentTimeMillis();
        s3TestSetup.createBucket(s3BucketName);
        exasolTestSetup.getDefaultBucket().uploadFile(IntegrationTestSetup.ADAPTER_JAR_LOCAL_PATH,
                IntegrationTestSetup.ADAPTER_JAR);
    }

    @Override
    protected ExtensionManagerSetup getSetup() {
        return setup;
    }

    @Override
    protected void createScripts() {
        final String adapterJarBucketFsPath = "/buckets/bfsdefault/default/"
                + IntegrationTestSetup.ADAPTER_JAR_LOCAL_PATH.getFileName().toString();
        final ExasolSchema schema = setup.createExtensionSchema();
        schema.createAdapterScriptBuilder("S3_FILES_ADAPTER")
                .bucketFsContent("com.exasol.adapter.RequestDispatcher", adapterJarBucketFsPath).language(Language.JAVA)
                .build();
        schema.createUdfBuilder("IMPORT_FROM_S3_DOCUMENT_FILES").language(UdfScript.Language.JAVA)
                .inputType(InputType.SET)
                .parameter(GenericUdfCallHandler.PARAMETER_DOCUMENT_FETCHER, "VARCHAR(2000000)")
                .parameter(GenericUdfCallHandler.PARAMETER_SCHEMA_MAPPING_REQUEST, "VARCHAR(2000000)")
                .parameter(GenericUdfCallHandler.PARAMETER_CONNECTION_NAME, "VARCHAR(500)").emits()
                .bucketFsContent(UdfEntryPoint.class.getName(), adapterJarBucketFsPath).build();
    }

    @Override
    protected void assertScriptsExist() {
        final String jarDirective = "%jar /buckets/bfsdefault/default/" + IntegrationTestSetup.ADAPTER_JAR + ";";
        final String comment = "Created by Extension Manager for S3 Virtual Schema " + PROJECT_VERSION;
        setup.exasolMetadata()
                .assertScript(table()
                        .row("IMPORT_FROM_S3_DOCUMENT_FILES", "UDF", "SET", "EMITS",
                                allOf(containsString("%scriptclass " + UdfEntryPoint.class.getName() + ";"), //
                                        containsString(jarDirective)),
                                comment) //
                        .row("S3_FILES_ADAPTER", "ADAPTER", null, null,
                                allOf(containsString("%scriptclass " + RequestDispatcher.class.getName() + ";"), //
                                        containsString(jarDirective)),
                                comment) //
                        .matches());
    }

    @Override
    protected void prepareInstance() {
        upload(s3ImportPrefix + "test-data-1.json", "{\"id\": 1, \"name\": \"abc\" }");
        upload(s3ImportPrefix + "test-data-2.json", "{\"id\": 2, \"name\": \"xyz\" }");
    }

    private void upload(final String key, final String content) {
        s3TestSetup.upload(s3BucketName, key, RequestBody.fromString(content));
    }

    @Override
    protected void assertVirtualSchemaContent(final String virtualSchemaName) {
        final String virtualTable = "\"" + virtualSchemaName + "\".\"" + MAPPING_DESTINATION_TABLE + "\"";
        try (final ResultSet result = exasolTestSetup.createConnection().createStatement()
                .executeQuery("SELECT ID, NAME FROM " + virtualTable + " ORDER BY ID ASC")) {
            assertThat("content of " + virtualTable, result, table().row(1L, "abc").row(2L, "xyz").matches());
        } catch (final SQLException exception) {
            throw new AssertionError("Assertion query failed", exception);
        }
    }

    @Override
    protected Collection<ParameterValue> createValidParameterValues() {
        final List<ParameterValue> parameters = new ArrayList<>();
        parameters.addAll(List.of( //
                param("awsRegion", s3TestSetup.getRegion()), //
                param("s3Bucket", s3BucketName), //
                param("awsAccessKeyId", s3TestSetup.getUsername()), //
                param("awsSecretAccessKey", s3TestSetup.getPassword())));
        parameters.add(param("MAPPING", new EdmlSerializer().serialize(getMappingDefinition())));

        getInDatabaseS3Address().map(address -> param("awsEndpointOverride", address)) //
                .ifPresent(parameters::add);
        if (s3TestSetup.getMfaToken().isPresent()) {
            parameters.add(param("awsSessionToken", s3TestSetup.getMfaToken().get()));
        }
        return parameters;
    }

    private EdmlDefinition getMappingDefinition() {
        return getMappingDefinition(s3ImportPrefix + "test-data-*.json");
    }

    private EdmlDefinition getMappingDefinition(final String source) {
        return EdmlDefinition.builder().source(source).destinationTable(MAPPING_DESTINATION_TABLE) //
                .mapping(Fields.builder() //
                        .mapField("id", ToDecimalMapping.builder().build()) //
                        .mapField("name", ToVarcharMapping.builder().varcharColumnSize(200).build()) //
                        .mapField("fieldWith'Quote", ToVarcharMapping.builder().varcharColumnSize(200).build()) //
                        .build())
                .build();
    }

    private Optional<String> getInDatabaseS3Address() {
        return s3TestSetup.getEntrypoint()
                .map(endpoint -> exasolTestSetup.makeTcpServiceAccessibleFromDatabase(endpoint))
                .map(InetSocketAddress::toString);
    }
}
