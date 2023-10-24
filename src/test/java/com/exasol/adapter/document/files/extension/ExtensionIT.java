package com.exasol.adapter.document.files.extension;

import static com.exasol.matcher.ResultSetStructureMatcher.table;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.FileNotFoundException;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.*;

import com.exasol.adapter.RequestDispatcher;
import com.exasol.adapter.document.GenericUdfCallHandler;
import com.exasol.adapter.document.UdfEntryPoint;
import com.exasol.adapter.document.edml.*;
import com.exasol.adapter.document.edml.serializer.EdmlSerializer;
import com.exasol.adapter.document.files.IntegrationTestSetup;
import com.exasol.adapter.document.files.s3testsetup.AwsS3TestSetup;
import com.exasol.adapter.document.files.s3testsetup.S3TestSetup;
import com.exasol.bucketfs.BucketAccessException;
import com.exasol.dbbuilder.dialects.exasol.AdapterScript.Language;
import com.exasol.dbbuilder.dialects.exasol.ExasolSchema;
import com.exasol.dbbuilder.dialects.exasol.udf.UdfScript;
import com.exasol.dbbuilder.dialects.exasol.udf.UdfScript.InputType;
import com.exasol.exasoltestsetup.ExasolTestSetup;
import com.exasol.exasoltestsetup.ExasolTestSetupFactory;
import com.exasol.extensionmanager.client.model.*;
import com.exasol.extensionmanager.itest.*;
import com.exasol.extensionmanager.itest.builder.ExtensionBuilder;
import com.exasol.mavenprojectversiongetter.MavenProjectVersionGetter;

import software.amazon.awssdk.core.sync.RequestBody;

class ExtensionIT {
    private static final String PREVIOUS_VERSION = "2.8.1";
    private static final String PREVIOUS_VERSION_JAR_FILE = "document-files-virtual-schema-dist-7.3.4-s3-"
            + PREVIOUS_VERSION + ".jar";
    private static final Path EXTENSION_SOURCE_DIR = Paths.get("extension").toAbsolutePath();
    private static final String EXTENSION_ID = "s3-vs-extension.js";
    private static final int EXPECTED_PARAMETER_COUNT = 10;
    private static final String PROJECT_VERSION = MavenProjectVersionGetter.getCurrentProjectVersion();
    private static ExasolTestSetup exasolTestSetup;
    private static ExtensionManagerSetup setup;
    private static S3TestSetup s3TestSetup;
    private static String s3BucketName;

    @BeforeAll
    static void setup() throws FileNotFoundException, BucketAccessException, TimeoutException {
        exasolTestSetup = new ExasolTestSetupFactory(IntegrationTestSetup.CLOUD_SETUP_CONFIG).getTestSetup();
        setup = ExtensionManagerSetup.create(exasolTestSetup, ExtensionBuilder.createDefaultNpmBuilder(
                EXTENSION_SOURCE_DIR, EXTENSION_SOURCE_DIR.resolve("dist").resolve(EXTENSION_ID)));
        s3TestSetup = new AwsS3TestSetup();
        s3BucketName = "extension-test.s3.virtual-schema-test-bucket-" + System.currentTimeMillis();
        s3TestSetup.createBucket(s3BucketName);
        exasolTestSetup.getDefaultBucket().uploadFile(IntegrationTestSetup.ADAPTER_JAR_LOCAL_PATH,
                IntegrationTestSetup.ADAPTER_JAR);
    }

    @AfterAll
    static void teardown() throws Exception {
        if (s3TestSetup != null) {
            s3TestSetup.emptyS3Bucket(s3BucketName);
            s3TestSetup.deleteBucket(s3BucketName);
        }
        if (setup != null) {
            setup.close();
        }
        // do not delete ADAPTER_JAR as it is required by other integration tests, too
        // and upload immediately after delete fails.
        exasolTestSetup.close();
    }

    @AfterEach
    void cleanup() {
        setup.cleanup();
    }

    @Test
    void listExtensions() {
        final List<ExtensionsResponseExtension> extensions = setup.client().getExtensions();
        assertAll(() -> assertThat(extensions, hasSize(1)), //
                () -> assertThat(extensions.get(0).getName(), equalTo("S3 Virtual Schema")),
                () -> assertThat(extensions.get(0).getInstallableVersions().get(0).getName(), equalTo(PROJECT_VERSION)),
                () -> assertThat(extensions.get(0).getInstallableVersions().get(0).isLatest(), is(true)),
                () -> assertThat(extensions.get(0).getInstallableVersions().get(0).isDeprecated(), is(false)),
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
                () -> assertThat(installations.get(0).getVersion(), equalTo(PROJECT_VERSION)));
    }

    @Test
    void listInstallations_findsOwnInstallation() {
        setup.client().install();
        final List<InstallationsResponseInstallation> installations = setup.client().getInstallations();
        assertAll(() -> assertThat(installations, hasSize(1)), //
                () -> assertThat(installations.get(0).getName(),
                        equalTo(ExtensionManagerSetup.EXTENSION_SCHEMA_NAME + ".S3_FILES_ADAPTER")),
                () -> assertThat(installations.get(0).getVersion(), equalTo(PROJECT_VERSION)));
    }

    @Test
    void getExtensionDetailsFailsForUnknownVersion() {
        setup.client().assertRequestFails(() -> setup.client().getExtensionDetails("unknownVersion"),
                equalTo("Version 'unknownVersion' not supported, can only use '" + PROJECT_VERSION + "'."),
                equalTo(404));
    }

    @Test
    void getExtensionDetailsSuccess() {
        final ExtensionDetailsResponse extensionDetails = setup.client().getExtensionDetails(PROJECT_VERSION);
        final List<ParamDefinition> parameters = extensionDetails.getParameterDefinitions();
        final ParamDefinition param1 = new ParamDefinition().id("virtualSchemaName")
                .name("Name of the new virtual schema").definition(Map.of("id", "virtualSchemaName", "name",
                        "Name of the new virtual schema", "required", true, "scope", "general", "type", "string"));
        assertAll(() -> assertThat(extensionDetails.getId(), equalTo("s3-vs-extension.js")),
                () -> assertThat(extensionDetails.getVersion(), equalTo(PROJECT_VERSION)),
                () -> assertThat(parameters, hasSize(EXPECTED_PARAMETER_COUNT)),
                () -> assertThat(parameters.get(0), equalTo(param1)));
    }

    @Test
    void install_createsScripts() {
        setup.client().install();
        assertScriptsExist();
    }

    @Test
    void install_worksIfCalledTwice() {
        setup.client().install();
        setup.client().install();
        assertScriptsExist();
    }

    @Test
    void install_failsForUnsupportedVersion() {
        final ExtensionManagerClient client = setup.client();
        client.assertRequestFails(() -> client.install("unsupported"),
                equalTo("Installing version 'unsupported' not supported, try '" + PROJECT_VERSION + "'."),
                equalTo(400));
        setup.exasolMetadata().assertNoScripts();
    }

    @Test
    void createInstanceFailsWithoutRequiredParameters() {
        final ExtensionManagerClient client = setup.client();
        client.install();
        client.assertRequestFails(() -> client.createInstance(List.of()), startsWith(
                "invalid parameters: Failed to validate parameter 'Name of the new virtual schema': This is a required parameter."),
                equalTo(400));
    }

    @Test
    void uninstall_failsForUnknownVersion() {
        setup.client().assertRequestFails(() -> setup.client().uninstall("unknownVersion"),
                equalTo("Uninstalling version 'unknownVersion' not supported, try '" + PROJECT_VERSION + "'."),
                equalTo(400));
    }

    @Test
    void uninstall_succeedsForNonExistingInstallation() {
        assertDoesNotThrow(() -> setup.client().uninstall());
    }

    @Test
    void uninstall_removesAdapters() {
        setup.client().install();
        assertAll(() -> assertScriptsExist(), //
                () -> assertThat(setup.client().getInstallations(), hasSize(1)));
        setup.client().uninstall(PROJECT_VERSION);
        assertAll(() -> assertThat(setup.client().getInstallations(), is(empty())),
                () -> setup.exasolMetadata().assertNoScripts());
    }

    @Test
    void upgradeFailsWhenNotInstalled() {
        setup.client().assertRequestFails(() -> setup.client().upgrade(),
                "extension is not installed, the following scripts are missing: S3_FILES_ADAPTER, IMPORT_FROM_S3_DOCUMENT_FILES",
                404);
    }

    @Test
    void upgradeFailsWhenAlreadyUpToDate() {
        setup.client().install();
        setup.client().assertRequestFails(() -> setup.client().upgrade(),
                "Extension is already installed in latest version " + PROJECT_VERSION, 412);
    }

    @Test
    void upgradeFromPreviousVersion() throws InterruptedException, BucketAccessException, TimeoutException,
            FileNotFoundException, URISyntaxException {
        final PreviousExtensionVersion previousVersion = createPreviousVersion();
        previousVersion.prepare();
        previousVersion.install();
        final String virtualTable = createVirtualSchema(previousVersion.getExtensionId(), PREVIOUS_VERSION);
        verifyVirtualTableContainsData(virtualTable);
        assertInstalledVersion("EXA_EXTENSIONS.S3_FILES_ADAPTER", PREVIOUS_VERSION, previousVersion));
        previousVersion.upgrade();
        assertInstalledVersion("EXA_EXTENSIONS.S3_FILES_ADAPTER", PROJECT_VERSION, previousVersion);
        verifyVirtualTableContainsData(virtualTable);
    }

    private PreviousExtensionVersion createPreviousVersion() {
        return setup.previousVersionManager().newVersion().currentVersion(PROJECT_VERSION) //
                .previousVersion(PREVIOUS_VERSION) //
                .adapterFileName(PREVIOUS_VERSION_JAR_FILE) //
                .extensionFileName(EXTENSION_ID) //
                .project("s3-document-files-virtual-schema") //
                .build();
    }

    private void assertInstalledVersion(final String expectedName, final String expectedVersion,
            final PreviousExtensionVersion previousVersion) {
        // The extension is installed twice (previous and current version), so each one returns one installation.
        assertThat(setup.client().getInstallations(),
                containsInAnyOrder(
                        new InstallationsResponseInstallation().name(expectedName).version(expectedVersion)
                                .id(EXTENSION_ID), //
                        new InstallationsResponseInstallation().name(expectedName).version(expectedVersion)
                                .id(previousVersion.getExtensionId())));
    }

    @Test
    void virtualSchemaWorks() throws SQLException {
        setup.client().install();
        final String virtualTable = createVirtualSchema();
        verifyVirtualTableContainsData(virtualTable);
    }

    private String createVirtualSchema() {
        return createVirtualSchema(EXTENSION_ID, PROJECT_VERSION);
    }

    private String createVirtualSchema(final String extensionId, final String extensionVersion) {
        final String prefix = "vs-works-test-" + System.currentTimeMillis() + "/";
        upload(prefix + "test-data-1.json", "{\"id\": 1, \"name\": \"abc\" }");
        upload(prefix + "test-data-2.json", "{\"id\": 2, \"name\": \"xyz\" }");
        final String virtualSchemaName = "MY_VS";
        final EdmlDefinition mappingDefinition = getMappingDefinition(prefix + "test-data-*.json");
        createInstance(extensionId, extensionVersion, virtualSchemaName, mappingDefinition);
        return virtualSchemaName + "." + mappingDefinition.getDestinationTable();
    }

    private void verifyVirtualTableContainsData(final String virtualTable) {
        try (final ResultSet result = exasolTestSetup.createConnection().createStatement()
                .executeQuery("SELECT ID, NAME FROM " + virtualTable + " ORDER BY ID ASC")) {
            assertThat(result, table().row(1L, "abc").row(2L, "xyz").matches());
        } catch (final SQLException exception) {
            throw new AssertionError("Assertion query failed", exception);
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
        setup.client().install();
        final String name = "my_virtual_SCHEMA";
        createInstance(name);
        assertThat(setup.client().listInstances("unknownVersion"),
                allOf(hasSize(1), equalTo(List.of(new Instance().id(name).name(name)))));
    }

    @Test
    void createInstanceCreatesDbObjects() {
        setup.client().install();
        final String name = "my_virtual_SCHEMA";
        createInstance(name);

        setup.exasolMetadata().assertConnection(table().row("MY_VIRTUAL_SCHEMA_CONNECTION",
                "Created by Extension Manager for S3 Virtual Schema my_virtual_SCHEMA").matches());
        setup.exasolMetadata()
                .assertVirtualSchema(table()
                        .row("my_virtual_SCHEMA", "SYS", "EXA_EXTENSIONS.S3_FILES_ADAPTER", not(emptyOrNullString()))
                        .matches());
        assertThat(setup.client().listInstances(),
                allOf(hasSize(1), equalTo(List.of(new Instance().id(name).name(name)))));
    }

    @Test
    void createTwoInstances() {
        setup.client().install();
        createInstance("vs1");
        createInstance("vs2");

        setup.exasolMetadata()
                .assertConnection(table()
                        .row("VS1_CONNECTION", "Created by Extension Manager for S3 Virtual Schema vs1")
                        .row("VS2_CONNECTION", "Created by Extension Manager for S3 Virtual Schema vs2").matches());
        setup.exasolMetadata()
                .assertVirtualSchema(table()
                        .row("vs1", "SYS", "EXA_EXTENSIONS.S3_FILES_ADAPTER", not(emptyOrNullString()))
                        .row("vs2", "SYS", "EXA_EXTENSIONS.S3_FILES_ADAPTER", not(emptyOrNullString())).matches());

        assertThat(setup.client().listInstances(), allOf(hasSize(2),
                equalTo(List.of(new Instance().id("vs1").name("vs1"), new Instance().id("vs2").name("vs2")))));
    }

    @Test
    void createInstanceWithSingleQuote() {
        setup.client().install();
        createInstance("Quoted'schema");
        setup.exasolMetadata().assertConnection(table()
                .row("QUOTED'SCHEMA_CONNECTION", "Created by Extension Manager for S3 Virtual Schema Quoted'schema")
                .matches());
        setup.exasolMetadata().assertVirtualSchema(table()
                .row("Quoted'schema", "SYS", "EXA_EXTENSIONS.S3_FILES_ADAPTER", not(emptyOrNullString())).matches());
    }

    @Test
    void deleteNonExistingInstance() {
        assertDoesNotThrow(() -> setup.client().deleteInstance("no-such-instance"));
    }

    @Test
    void deleteFailsForUnknownVersion() {
        setup.client().assertRequestFails(() -> setup.client().deleteInstance("unknownVersion", "no-such-instance"),
                equalTo("Version 'unknownVersion' not supported, can only use '" + PROJECT_VERSION + "'."),
                equalTo(404));
    }

    @Test
    void deleteExistingInstance() {
        setup.client().install();
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
        createInstance(EXTENSION_ID, PROJECT_VERSION, virtualSchemaName, mapping);
    }

    private void createInstance(final String extensionId, final String extensionVersion, final String virtualSchemaName,
            final EdmlDefinition mapping) {
        setup.addVirtualSchemaToCleanupQueue(virtualSchemaName);
        setup.addConnectionToCleanupQueue(virtualSchemaName.toUpperCase() + "_CONNECTION");
        final String instanceName = setup.client().createInstance(extensionId, extensionVersion,
                createValidParameters(virtualSchemaName, mapping));
        assertThat(instanceName, equalTo(virtualSchemaName));
    }

    private List<ParameterValue> createValidParameters(final String virtualSchemaName, final EdmlDefinition mapping) {
        final List<ParameterValue> parameters = new ArrayList<>(
                List.of(param("virtualSchemaName", virtualSchemaName), param("awsRegion", s3TestSetup.getRegion()), //
                        param("s3Bucket", s3BucketName), //
                        param("awsAccessKeyId", s3TestSetup.getUsername()), //
                        param("awsSecretAccessKey", s3TestSetup.getPassword()), //
                        param("mapping", new EdmlSerializer().serialize(mapping))));
        getInDatabaseS3Address().map(address -> param("awsEndpointOverride", address)) //
                .ifPresent(parameters::add);
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

    private Optional<String> getInDatabaseS3Address() {
        return s3TestSetup.getEntrypoint()
                .map(endpoint -> exasolTestSetup.makeTcpServiceAccessibleFromDatabase(endpoint))
                .map(InetSocketAddress::toString);
    }

    private ParameterValue param(final String name, final String value) {
        return new ParameterValue().name(name).value(value);
    }

    private void assertScriptsExist() {
        final String jarDirective = "%jar /buckets/bfsdefault/default/" + IntegrationTestSetup.ADAPTER_JAR + ";";
        final String comment = "Created by Extension Manager for S3 Virtual Schema extension " + PROJECT_VERSION;
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
        final String adapterJarBucketFsPath = "/buckets/bfsdefault/default/"
                + IntegrationTestSetup.ADAPTER_JAR_LOCAL_PATH.getFileName().toString();
        final ExasolSchema schema = setup.createExtensionSchema();
        schema.createAdapterScriptBuilder(adapterScriptName)
                .bucketFsContent("com.exasol.adapter.RequestDispatcher", adapterJarBucketFsPath).language(Language.JAVA)
                .build();
        schema.createUdfBuilder(importScriptName).language(UdfScript.Language.JAVA).inputType(InputType.SET)
                .parameter(GenericUdfCallHandler.PARAMETER_DOCUMENT_FETCHER, "VARCHAR(2000000)")
                .parameter(GenericUdfCallHandler.PARAMETER_SCHEMA_MAPPING_REQUEST, "VARCHAR(2000000)")
                .parameter(GenericUdfCallHandler.PARAMETER_CONNECTION_NAME, "VARCHAR(500)").emits()
                .bucketFsContent(UdfEntryPoint.class.getName(), adapterJarBucketFsPath).build();
    }
}
