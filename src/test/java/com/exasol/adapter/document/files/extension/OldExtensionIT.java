package com.exasol.adapter.document.files.extension;

import static com.exasol.matcher.ResultSetStructureMatcher.table;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;

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

import com.exasol.adapter.document.edml.*;
import com.exasol.adapter.document.edml.serializer.EdmlSerializer;
import com.exasol.adapter.document.files.IntegrationTestSetup;
import com.exasol.adapter.document.files.s3testsetup.AwsS3TestSetup;
import com.exasol.adapter.document.files.s3testsetup.S3TestSetup;
import com.exasol.bucketfs.BucketAccessException;
import com.exasol.exasoltestsetup.ExasolTestSetup;
import com.exasol.exasoltestsetup.ExasolTestSetupFactory;
import com.exasol.extensionmanager.client.model.InstallationsResponseInstallation;
import com.exasol.extensionmanager.client.model.ParameterValue;
import com.exasol.extensionmanager.itest.*;
import com.exasol.extensionmanager.itest.builder.ExtensionBuilder;
import com.exasol.mavenprojectversiongetter.MavenProjectVersionGetter;

import software.amazon.awssdk.core.sync.RequestBody;

/**
 * This test class can be deleted once version 2.8.3 was released and {@link ExtensionIT#upgradeFromPreviousVersion()}
 * is deleted.
 */
class OldExtensionIT {
    private static final String PREVIOUS_VERSION = "2.8.2";
    private static final String PREVIOUS_VERSION_JAR_FILE = "document-files-virtual-schema-dist-7.3.6-s3-"
            + PREVIOUS_VERSION + ".jar";
    private static final Path EXTENSION_SOURCE_DIR = Paths.get("extension").toAbsolutePath();
    private static final String EXTENSION_ID = "s3-vs-extension.js";
    private static final String PROJECT_VERSION = MavenProjectVersionGetter.getCurrentProjectVersion();
    private static ExasolTestSetup exasolTestSetup;
    private static ExtensionManagerSetup setup;
    private static S3TestSetup s3TestSetup;
    private static String s3BucketName;

    @BeforeAll
    static void setup() throws FileNotFoundException, BucketAccessException, TimeoutException {
        if (System.getProperty("com.exasol.dockerdb.image") == null) {
            System.setProperty("com.exasol.dockerdb.image", "8.23.1");
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
    void upgradeFromPreviousVersion() throws InterruptedException, BucketAccessException, TimeoutException,
            FileNotFoundException, URISyntaxException {
        final PreviousExtensionVersion previousVersion = createPreviousVersion();
        previousVersion.prepare();
        previousVersion.install();
        final String virtualTable = createVirtualSchema(previousVersion.getExtensionId(), PREVIOUS_VERSION);
        verifyVirtualTableContainsData(virtualTable);
        assertInstalledVersion(PREVIOUS_VERSION, previousVersion);
        previousVersion.upgrade();
        assertInstalledVersion(PROJECT_VERSION, previousVersion);
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

    private void assertInstalledVersion(final String expectedVersion, final PreviousExtensionVersion previousVersion) {
        // The extension is installed twice (previous and current version), so each one returns one installation.
        assertThat(setup.client().getInstallations(),
                containsInAnyOrder(
                        new InstallationsResponseInstallation().name("S3 Virtual Schema").version(expectedVersion)
                                .id(EXTENSION_ID), //
                        new InstallationsResponseInstallation().name("EXA_EXTENSIONS.S3_FILES_ADAPTER")
                                .version(expectedVersion).id(previousVersion.getExtensionId())));
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

    private void upload(final String key, final String content) {
        s3TestSetup.upload(s3BucketName, key, RequestBody.fromString(content));
    }

    private void createInstance(final String extensionId, final String extensionVersion, final String virtualSchemaName,
            final EdmlDefinition mapping) {
        setup.addVirtualSchemaToCleanupQueue(virtualSchemaName);
        setup.addConnectionToCleanupQueue(virtualSchemaName.toUpperCase() + "_CONNECTION");
        final String instanceName = setup.client().createInstance(extensionId, extensionVersion,
                createValidParameters(virtualSchemaName, mapping, extensionVersion));
        assertThat(instanceName, equalTo(virtualSchemaName));
    }

    private List<ParameterValue> createValidParameters(final String virtualSchemaName, final EdmlDefinition mapping,
            final String extensionVersion) {
        final List<ParameterValue> parameters = new ArrayList<>();
        parameters.addAll(List.of( //
                param("awsRegion", s3TestSetup.getRegion()), //
                param("s3Bucket", s3BucketName), //
                param("awsAccessKeyId", s3TestSetup.getUsername()), //
                param("awsSecretAccessKey", s3TestSetup.getPassword())));
        if (extensionVersion.equals(PREVIOUS_VERSION)) {
            parameters.add(param("virtualSchemaName", virtualSchemaName));
            parameters.add(param("mapping", new EdmlSerializer().serialize(mapping)));
        } else {
            parameters.add(param("base-vs.virtual-schema-name", virtualSchemaName));
            parameters.add(param("MAPPING", new EdmlSerializer().serialize(mapping)));
        }

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
}
