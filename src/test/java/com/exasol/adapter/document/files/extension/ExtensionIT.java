package com.exasol.adapter.document.files.extension;

import static com.exasol.matcher.ResultSetStructureMatcher.table;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import com.exasol.adapter.RequestDispatcher;
import com.exasol.adapter.document.GenericUdfCallHandler;
import com.exasol.adapter.document.UdfEntryPoint;
import com.exasol.adapter.document.edml.*;
import com.exasol.adapter.document.edml.serializer.EdmlSerializer;
import com.exasol.adapter.document.files.IntegrationTestSetup;
import com.exasol.adapter.document.files.s3testsetup.AwsS3TestSetup;
import com.exasol.adapter.document.files.s3testsetup.S3TestSetup;
import com.exasol.dbbuilder.dialects.exasol.AdapterScript.Language;
import com.exasol.dbbuilder.dialects.exasol.ExasolSchema;
import com.exasol.dbbuilder.dialects.exasol.udf.UdfScript;
import com.exasol.dbbuilder.dialects.exasol.udf.UdfScript.InputType;
import com.exasol.exasoltestsetup.ServiceAddress;
import com.exasol.extensionmanager.client.model.*;
import com.exasol.mavenprojectversiongetter.MavenProjectVersionGetter;

import software.amazon.awssdk.core.sync.RequestBody;

class ExtensionIT {
    private static final int EXPECTED_PARAMETER_COUNT = 10;
    private static ExtensionManagerSetup setup;
    private static S3TestSetup s3TestSetup;
    private static String s3BucketName;
    private static String projectVersion;

    @TempDir
    static Path extensionFolder;

    @BeforeAll
    static void setup() {
        setup = ExtensionManagerSetup.create(extensionFolder);
        s3TestSetup = new AwsS3TestSetup();
        s3BucketName = "extension-test.s3.virtual-schema-test-bucket-" + System.currentTimeMillis();
        s3TestSetup.createBucket(s3BucketName);
        projectVersion = MavenProjectVersionGetter.getCurrentProjectVersion();
    }

    @AfterAll
    static void teardown() {
        if (s3TestSetup != null) {
            s3TestSetup.emptyS3Bucket(s3BucketName);
            s3TestSetup.deleteBucket(s3BucketName);
        }
        if (setup != null) {
            setup.close();
        }
    }

    @AfterEach
    void cleanup() {
        setup.dropExtensionSchema();
    }

    @Test
    void listExtensions() {
        final List<ExtensionsResponseExtension> extensions = setup.client().getExtensions();
        assertAll(() -> assertThat(extensions, hasSize(1)), //
                () -> assertThat(extensions.get(0).getName(), equalTo("S3 Virtual Schema")),
                () -> assertThat(extensions.get(0).getInstallableVersions(), contains(projectVersion)),
                () -> assertThat(extensions.get(0).getDescription(),
                        equalTo("Virtual Schema for document files on AWS S3")));
    }

    @Test
    void listInstallationsEmpty() {
        final List<InstallationsResponseInstallation> installations = setup.client().getInstallations();
        assertThat(installations, hasSize(0));
    }

    @Test
    void listInstallations_ignoresWrongScriptNames() {
        createAdapter("wrong_adapter_name", "wrong_import_script_name");
        final List<InstallationsResponseInstallation> installations = setup.client().getInstallations();
        assertThat(installations, hasSize(0));
    }

    @Test
    void listInstallations_findsMatchingScripts() {
        createAdapter("S3_FILES_ADAPTER", "IMPORT_FROM_S3_DOCUMENT_FILES");
        final List<InstallationsResponseInstallation> installations = setup.client().getInstallations();
        assertAll(() -> assertThat(installations, hasSize(1)), //
                () -> assertThat(installations.get(0).getName(),
                        equalTo(ExtensionManagerSetup.EXTENSION_SCHEMA_NAME + ".S3_FILES_ADAPTER")),
                () -> assertThat(installations.get(0).getVersion(), equalTo(setup.getCurrentProjectVersion())),
                () -> assertThat(installations.get(0).getInstanceParameters(), hasSize(EXPECTED_PARAMETER_COUNT)));
    }

    @Test
    void listInstallations_findsOwnInstallation() {
        setup.client().installExtension();
        final List<InstallationsResponseInstallation> installations = setup.client().getInstallations();
        assertAll(() -> assertThat(installations, hasSize(1)), //
                () -> assertThat(installations.get(0).getName(),
                        equalTo(ExtensionManagerSetup.EXTENSION_SCHEMA_NAME + ".S3_FILES_ADAPTER")),
                () -> assertThat(installations.get(0).getVersion(), equalTo(setup.getCurrentProjectVersion())),
                () -> assertThat(installations.get(0).getInstanceParameters(), hasSize(EXPECTED_PARAMETER_COUNT)));
    }

    @Test
    void install_createsScripts() {
        setup.client().installExtension();
        assertScriptsExist();
    }

    @Test
    void install_worksIfCalledTwice() {
        setup.client().installExtension();
        setup.client().installExtension();
        assertScriptsExist();
    }

    @Test
    void install_failsForUnsupportedVersion() {
        final ExtensionManagerClient client = setup.client();
        client.assertRequestFails(() -> client.installExtension("unsupported"), equalTo(
                "Installing version 'unsupported' not supported, try '" + setup.getCurrentProjectVersion() + "'."),
                equalTo(400));
        setup.exasolMetadata().assertNoScripts();
    }

    @Test
    void createInstanceFailsWithoutRequiredParameters() {
        final ExtensionManagerClient client = setup.client();
        client.installExtension();
        client.assertRequestFails(() -> client.createInstance(List.of()), startsWith(
                "invalid parameters: Failed to validate parameter 'Name of the new virtual schema': This is a required parameter."),
                equalTo(400));
    }

    @Test
    void virtualSchemaWorks() throws SQLException {
        setup.client().installExtension();
        final String prefix = "vs-works-test-" + System.currentTimeMillis() + "/";
        upload(prefix + "test-data-1.json", "{\"id\": 1, \"name\": \"abc\" }");
        upload(prefix + "test-data-2.json", "{\"id\": 2, \"name\": \"xyz\" }");
        createInstance("MY_VS", getMappingDefinition(prefix + "test-data-*.json"));
        try (final ResultSet result = setup.createStatement()
                .executeQuery("SELECT ID, NAME FROM MY_VS.TEST ORDER BY ID ASC")) {
            assertThat(result, table().row(1L, "abc").row(2L, "xyz").matches());
        }
    }

    @Test
    void listingInstancesNoVSExists() throws SQLException {
        assertThat(setup.client().listInstances(), hasSize(0));
    }

    private void upload(final String key, final String content) {
        s3TestSetup.upload(s3BucketName, key, RequestBody.fromString(content));
    }

    @Test
    void listingInstancesIgnoresVersion() {
        setup.client().installExtension();
        final String name = "my_virtual_SCHEMA";
        createInstance(name);
        assertThat(setup.client().listInstances("unknownVersion"),
                allOf(hasSize(1), equalTo(List.of(new Instance().id(name).name(name)))));
    }

    @Test
    void createInstanceCreatesDbObjects() {
        setup.client().installExtension();
        final String name = "my_virtual_SCHEMA";
        createInstance(name);

        setup.exasolMetadata().assertConnection(table().row("MY_VIRTUAL_SCHEMA_CONNECTION",
                "Created by extension manager for S3 virtual schema my_virtual_SCHEMA").matches());
        setup.exasolMetadata()
                .assertVirtualSchema(table()
                        .row("my_virtual_SCHEMA", "SYS", "EXA_EXTENSIONS.S3_FILES_ADAPTER", not(emptyOrNullString()))
                        .matches());
        assertThat(setup.client().listInstances(),
                allOf(hasSize(1), equalTo(List.of(new Instance().id(name).name(name)))));
    }

    @Test
    void createTwoInstances() {
        setup.client().installExtension();
        createInstance("vs1");
        createInstance("vs2");

        setup.exasolMetadata()
                .assertConnection(table()
                        .row("VS1_CONNECTION", "Created by extension manager for S3 virtual schema vs1")
                        .row("VS2_CONNECTION", "Created by extension manager for S3 virtual schema vs2").matches());
        setup.exasolMetadata()
                .assertVirtualSchema(table()
                        .row("vs1", "SYS", "EXA_EXTENSIONS.S3_FILES_ADAPTER", not(emptyOrNullString()))
                        .row("vs2", "SYS", "EXA_EXTENSIONS.S3_FILES_ADAPTER", not(emptyOrNullString())).matches());

        assertThat(setup.client().listInstances(), allOf(hasSize(2),
                equalTo(List.of(new Instance().id("vs1").name("vs1"), new Instance().id("vs2").name("vs2")))));
    }

    @Test
    void createInstanceWithSingleQuote() {
        setup.client().installExtension();
        createInstance("Quoted'schema");
        setup.exasolMetadata().assertConnection(table()
                .row("QUOTED'SCHEMA_CONNECTION", "Created by extension manager for S3 virtual schema Quoted'schema")
                .matches());
        setup.exasolMetadata().assertVirtualSchema(table()
                .row("Quoted'schema", "SYS", "EXA_EXTENSIONS.S3_FILES_ADAPTER", not(emptyOrNullString())).matches());
    }

    @Test
    void deleteNonExistingInstance() {
        assertDoesNotThrow(() -> setup.client().deleteInstance("no-such-instance"));
    }

    @Test
    void deleteExistingInstance() {
        setup.client().installExtension();
        createInstance("vs1");
        final List<Instance> instances = setup.client().listInstances();
        assertThat(instances, hasSize(1));
        setup.client().deleteInstance(instances.get(0).getId());
        assertThat(setup.client().listInstances(), is(empty()));
        assertAll(() -> setup.exasolMetadata().assertNoConnections(),
                () -> setup.exasolMetadata().assertNoVirtualSchema());
    }

    private void createInstance(final String virtualSchemaName) {
        createInstance(virtualSchemaName, getMappingDefinition("test-data-*.json"));
    }

    private void createInstance(final String virtualSchemaName, final EdmlDefinition mapping) {
        setup.addVirtualSchemaToDrop(virtualSchemaName);
        setup.addConnectionToDrop(virtualSchemaName.toUpperCase() + "_CONNECTION");
        final String instanceName = setup.client().createInstance(createValidParameters(virtualSchemaName, mapping));
        assertThat(instanceName, equalTo(virtualSchemaName));
    }

    private List<ParameterValue> createValidParameters(final String virtualSchemaName, final EdmlDefinition mapping) {
        final List<ParameterValue> parameters = new ArrayList<>(List.of(param("virtualSchemaName", virtualSchemaName),
                param("awsEndpointOverride", getInDatabaseS3Address()), //
                param("awsRegion", s3TestSetup.getRegion()), //
                param("s3Bucket", s3BucketName), //
                param("awsAccessKeyId", s3TestSetup.getUsername()), //
                param("awsSecretAccessKey", s3TestSetup.getPassword()), //
                param("mapping", new EdmlSerializer().serialize(mapping))));
        if (s3TestSetup.getMfaToken().isPresent()) {
            parameters.add(param("awsSessionToken", s3TestSetup.getMfaToken().get()));
        }
        return parameters;
    }

    private EdmlDefinition getMappingDefinition(final String source) {
        return EdmlDefinition.builder().source(source).destinationTable("TEST") //
                .mapping(Fields.builder() //
                        .mapField("id", ToDecimalMapping.builder().build()) //
                        .mapField("name", ToVarcharMapping.builder().varcharColumnSize(200).build()) //
                        .mapField("fieldWith'Quote", ToVarcharMapping.builder().varcharColumnSize(200).build()) //
                        .build())
                .build();
    }

    private String getInDatabaseS3Address() {
        final String s3Entrypoint = s3TestSetup.getEntrypoint();
        if (s3Entrypoint.contains(":")) {
            return setup.makeTcpServiceAccessibleFromDatabase(ServiceAddress.parse(s3Entrypoint)).toString();
        } else {
            return s3Entrypoint;
        }
    }

    private ParameterValue param(final String name, final String value) {
        return new ParameterValue().name(name).value(value);
    }

    private void assertScriptsExist() {
        final String jarDirective = "%jar /buckets/bfsdefault/default/" + IntegrationTestSetup.ADAPTER_JAR + ";";
        final String comment = "Created by extension manager for S3 virtual schema extension " + projectVersion;
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

    private void createAdapter(final String adapterScriptName, final String importScriptName) {
        final ExasolSchema schema = setup.createExtensionSchema();
        schema.createAdapterScriptBuilder(adapterScriptName)
                .bucketFsContent("com.exasol.adapter.RequestDispatcher", setup.getAdapterJarInBucketFs())
                .language(Language.JAVA).build();
        schema.createUdfBuilder(importScriptName).language(UdfScript.Language.JAVA).inputType(InputType.SET)
                .parameter(GenericUdfCallHandler.PARAMETER_DOCUMENT_FETCHER, "VARCHAR(2000000)")
                .parameter(GenericUdfCallHandler.PARAMETER_SCHEMA_MAPPING_REQUEST, "VARCHAR(2000000)")
                .parameter(GenericUdfCallHandler.PARAMETER_CONNECTION_NAME, "VARCHAR(500)").emits()
                .bucketFsContent(UdfEntryPoint.class.getName(), setup.getAdapterJarInBucketFs()).build();
    }
}
