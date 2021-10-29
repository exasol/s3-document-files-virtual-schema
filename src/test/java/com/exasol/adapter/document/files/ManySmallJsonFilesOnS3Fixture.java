package com.exasol.adapter.document.files;

import static java.util.logging.Level.INFO;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import jakarta.json.*;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.http.async.SdkAsyncHttpClient;
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.iam.IamClient;
import software.amazon.awssdk.services.iam.model.Policy;
import software.amazon.awssdk.services.iam.model.Role;
import software.amazon.awssdk.services.lambda.LambdaAsyncClient;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.*;
import software.amazon.awssdk.services.lambda.model.Runtime;
import software.amazon.awssdk.services.sts.StsClient;

class ManySmallJsonFilesOnS3Fixture implements AutoCloseable {
    private static final String LAMBDA_FUNCTION_NAME = "create-json-files";
    private static final String ROLE_NAME = LAMBDA_FUNCTION_NAME + "-role";
    private static final String POLICY_NAME = LAMBDA_FUNCTION_NAME + "-policy";
    private static final int FILES_PER_LAMBDA = 5_000;
    private static final Logger LOGGER = Logger.getLogger(ManySmallJsonFilesOnS3Fixture.class.getName());
    private static final String ACTION_CREATE = "create";
    private static final String ACTION_DELETE = "delete";
    private final List<Closeable> createdResources = new ArrayList<>();
    private final String accountId;
    private final String bucket;
    private final LambdaAsyncClient asyncLambdaClient;
    private final int numberOfJsonFiles;

    public ManySmallJsonFilesOnS3Fixture(final String bucket, final int numberOfJsonFiles) throws IOException {
        try {
            this.bucket = bucket;
            this.numberOfJsonFiles = numberOfJsonFiles;
            final AwsCredentialsProvider credentialsProvider = TestConfig.instance().getAwsCredentialsProvider();
            this.accountId = StsClient.builder().credentialsProvider(credentialsProvider).build().getCallerIdentity()
                    .account();
            final LambdaClient lambdaClient = LambdaClient.builder().credentialsProvider(credentialsProvider).build();
            this.asyncLambdaClient = LambdaAsyncClient.builder().httpClient(getHttpClientWithIncreasedTimeouts())
                    .credentialsProvider(credentialsProvider).build();
            deployFunction(credentialsProvider, lambdaClient);
            runLambdas(ACTION_CREATE);
            this.createdResources.add(() -> runLambdas(ACTION_DELETE));
        } catch (final Exception exception) {
            close();
            throw exception;
        }
    }

    private void deployFunction(final AwsCredentialsProvider credentialsProvider, final LambdaClient lambdaClient)
            throws IOException {
        final Role role = createRoleForLambda(credentialsProvider);
        final SdkBytes zipBytes = getZipedCreateFilesLambda();
        lambdaClient.createFunction(builder -> builder.functionName(LAMBDA_FUNCTION_NAME)
                .architectures(Architecture.ARM64).code(FunctionCode.builder().zipFile(zipBytes).build())
                .role(role.arn()).runtime(Runtime.NODEJS14_X).handler("createJsonFilesLambda.handler").timeout(15 * 60)
                .tags(Map.of("exa:owner", TestConfig.instance().getOwner(), "exa:project", "S3VS")));
        this.createdResources
                .add(() -> lambdaClient.deleteFunction(request -> request.functionName(LAMBDA_FUNCTION_NAME)));
    }

    private SdkAsyncHttpClient getHttpClientWithIncreasedTimeouts() {
        return NettyNioAsyncHttpClient.builder().readTimeout(Duration.ofMinutes(16))
                .connectionAcquisitionTimeout(Duration.ofMinutes(1)).writeTimeout(Duration.ofMinutes(1))
                .connectionTimeout(Duration.ofMinutes(1)).maxConcurrency(600).build();
    }

    private Role createRoleForLambda(final AwsCredentialsProvider credentialsProvider) {
        final IamClient iamClient = IamClient.builder().region(Region.AWS_GLOBAL)
                .credentialsProvider(credentialsProvider).build();
        final Policy policy = iamClient
                .createPolicy(request -> request.policyName(POLICY_NAME).policyDocument(getPolicyDocument())).policy();
        this.createdResources.add(() -> iamClient.deletePolicy(request -> request.policyArn(policy.arn())));
        final Role role = iamClient.createRole(request -> request.roleName(ROLE_NAME)
                .assumeRolePolicyDocument(getAssumeRolePolicyDocument()).path("/service-role/")).role();
        this.createdResources.add(() -> iamClient.deleteRole(request -> request.roleName(role.roleName())));
        iamClient.attachRolePolicy(request -> request.roleName(ROLE_NAME).policyArn(policy.arn()));
        this.createdResources.add(
                () -> iamClient.detachRolePolicy(request -> request.policyArn(policy.arn()).roleName(role.roleName())));
        waitForRoleBeingFullyCreated();
        return role;
    }

    @SuppressWarnings("java:S2925") // we have no alternative to active waiting here since AWS is only eventual
                                    // consistent
    private void waitForRoleBeingFullyCreated() {
        try {
            Thread.sleep(30000);
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while waiting.");
        }
    }

    private String getPolicyDocument() {
        final String policyTemplate = getResourceAsString("createJsonFilesLambda/policy.json");
        return policyTemplate.replaceAll("\\Q{ACCOUNT}\\E", this.accountId).replaceAll("\\Q{BUCKET}\\E", this.bucket);
    }

    private String getAssumeRolePolicyDocument() {
        return getResourceAsString("createJsonFilesLambda/assumeRolePolicyDocument.json");
    }

    private String getResourceAsString(final String resourceName) {
        try (final InputStream stream = getClass().getClassLoader().getResourceAsStream(resourceName)) {
            return new String(Objects.requireNonNull(stream).readAllBytes(), StandardCharsets.UTF_8);
        } catch (final IOException exception) {
            throw new UncheckedIOException("Failed to read test resource.", exception);
        }
    }

    private SdkBytes getZipedCreateFilesLambda() throws IOException {
        final Path createJsonFilesLambda = Path.of("src/test/resources/createJsonFilesLambda/createJsonFilesLambda.js");
        try (final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                final ZipOutputStream zip = new ZipOutputStream(byteArrayOutputStream)) {
            final ZipEntry entry = new ZipEntry("createJsonFilesLambda.js");
            zip.putNextEntry(entry);
            Files.copy(createJsonFilesLambda, zip);
            zip.closeEntry();
            zip.close();
            return SdkBytes.fromByteArray(byteArrayOutputStream.toByteArray());
        }
    }

    private void runLambdas(final String action) {
        if (this.numberOfJsonFiles % FILES_PER_LAMBDA != 0) {
            throw new IllegalArgumentException("Number of JSON files must be a multiple of 1000");
        }
        final int numberOfLambdas = this.numberOfJsonFiles / FILES_PER_LAMBDA;
        if (numberOfLambdas > 1000) {
            throw new IllegalArgumentException("More then 1000 lambdas are currently not supported.");
        }
        LOGGER.log(INFO, "Performing {0} of {1} files using {2} lambda functions.",
                new Object[] { action, this.numberOfJsonFiles, numberOfLambdas });
        final List<CompletableFuture<InvokeResponse>> lambdaFutures = new ArrayList<>(numberOfLambdas);
        for (int lambdaCounter = 0; lambdaCounter < numberOfLambdas; lambdaCounter++) {
            final var future = startLambda(lambdaCounter * FILES_PER_LAMBDA, action);
            future.exceptionally((x) -> {
                LOGGER.severe("lambda error:" + x.getMessage());
                throw new IllegalStateException("Failed to run lambda", x);
            });
            lambdaFutures.add(future);
        }
        waitForLambdasToFinish(lambdaFutures);
        LOGGER.log(INFO, "{0} done", action);
    }

    private void waitForLambdasToFinish(final List<CompletableFuture<InvokeResponse>> lambdaFutures) {
        try {
            final CompletableFuture<Void> combinedFuture = CompletableFuture
                    .allOf(lambdaFutures.toArray(CompletableFuture[]::new));
            combinedFuture.get();
            for (final var future : lambdaFutures) {
                final InvokeResponse invokeResponse = future.get();
                if (invokeResponse.functionError() != null) {
                    throw new IllegalStateException("Failed to run lambda function: "
                            + invokeResponse.payload().asString(StandardCharsets.UTF_8));
                }
            }
        } catch (final InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while running lambda functions.");
        } catch (final ExecutionException exception) {
            throw new IllegalStateException("One or more lambda functions failed.", exception);
        }
    }

    private CompletableFuture<InvokeResponse> startLambda(final int offset, final String action) {
        final JsonObjectBuilder eventBuilder = Json.createObjectBuilder();
        eventBuilder.add("bucket", this.bucket);
        eventBuilder.add("offset", offset);
        eventBuilder.add("numberOfFiles", FILES_PER_LAMBDA);
        eventBuilder.add("action", action);
        final ByteArrayOutputStream bufferStream = new ByteArrayOutputStream();
        final JsonWriter jsonWriter = Json.createWriter(bufferStream);
        jsonWriter.write(eventBuilder.build());
        return this.asyncLambdaClient.invoke(
                request -> request.functionName(LAMBDA_FUNCTION_NAME).invocationType(InvocationType.REQUEST_RESPONSE)
                        .payload(SdkBytes.fromByteArray(bufferStream.toByteArray())));
    }

    @Override
    public void close() throws IOException {
        Collections.reverse(this.createdResources);
        for (final Closeable resource : this.createdResources) {
            resource.close();
        }
    }
}
