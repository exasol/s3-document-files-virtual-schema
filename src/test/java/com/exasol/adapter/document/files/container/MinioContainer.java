package com.exasol.adapter.document.files.container;

import java.net.*;
import java.time.Duration;

import org.testcontainers.Testcontainers;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.localstack.LocalStackContainer.Service;
import org.testcontainers.containers.startupcheck.OneShotStartupCheckStrategy;
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy;
import org.testcontainers.utility.DockerImageName;

public class MinioContainer extends GenericContainer<MinioContainer> implements S3Container {

    private static final int PORT = 9000;
    private static final String ADMIN_ACCESS_KEY = "admin";
    private static final String ADMIN_SECRET_KEY = "12345678";
    private static final String USER_ACCESS_KEY = "bob";
    private static final String USER_SECRET_KEY = "87654321";

    public MinioContainer(final DockerImageName dockerImageName) {
        // parent method uses @NonNull annotation from
        // import org.apache.hadoop.thirdparty.org.checkerframework.checker.nullness.qual.NonNull;
        // Should we do something similar?
        // Objects.nonNull(dockerImageName);
        super(dockerImageName);
        this.withEnv("MINIO_ACCESS_KEY", ADMIN_ACCESS_KEY) //
                .withEnv("MINIO_SECRET_KEY", ADMIN_SECRET_KEY) //
                .withCommand("server /data") //
                .withExposedPorts(PORT) //
                .waitingFor(new HttpWaitStrategy() //
                        .forPath("/minio/health/ready") //
                        .forPort(PORT) //
                        .withStartupTimeout(Duration.ofSeconds(10))) //
                .withReuse(true);
    }

    @Override
    public String getAccessKey() {
        return ADMIN_ACCESS_KEY;
    }

    @Override
    public String getSecretKey() {
        return ADMIN_SECRET_KEY;
    }

    @Override
    public String getRegion() {
        return "us-east-1";
    }

    @Override
    public URI getEndpointOverride(final Service service) {
        try {
            final String address = getHost();
            // resolve IP address and use that as the endpoint so that path-style access is automatically used for S3
            final String ipAddress = InetAddress.getByName(address).getHostAddress();
            return new URI("http://" + ipAddress + ":" + getMappedS3Port()); // getMappedPort(getServicePort(service)));
        } catch (UnknownHostException | URISyntaxException e) {
            throw new IllegalStateException("Cannot obtain endpoint URL", e);
        }
    }

    @Override
    public Integer getMappedS3Port() {
        return getFirstMappedPort();
    }

    public void addUser() {
        final int mappedPort = getMappedS3Port();
        Testcontainers.exposeHostPorts(mappedPort);
        // final String minioServerUrl = String.format("http://%s:%s", getHost(), mappedPort);

        // Minio Java SDK uses s3v4 protocol by default, need to specify explicitly for mc
        final String cmdTpl = "mc config host add myminio http://host.testcontainers.internal:%s %s %s --api s3v4 && "
                + "mc admin user add myminio %s %s readwrite";
        final String cmd = String.format(cmdTpl, mappedPort, ADMIN_ACCESS_KEY, ADMIN_SECRET_KEY, USER_ACCESS_KEY,
                USER_SECRET_KEY);

        try (GenericContainer<?> mcContainer = new GenericContainer<>("minio/mc")) {
            mcContainer.withStartupCheckStrategy(new OneShotStartupCheckStrategy()) //
                    .withCreateContainerCmdModifier(
                            containerCommand -> containerCommand.withTty(true).withEntrypoint("/bin/sh", "-c", cmd)) //
                    .start();
        }
    }

}
