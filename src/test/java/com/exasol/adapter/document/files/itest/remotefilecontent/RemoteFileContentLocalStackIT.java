package com.exasol.adapter.document.files.itest.remotefilecontent;

import org.junit.jupiter.api.*;

import com.exasol.adapter.document.files.s3testsetup.S3ContainerSetup;

/**
 * See parent class {@link AbstractRemoteFileContentIT} for actual test cases which are generic for all container
 * flavors implementing interface {@code S3ContainerSetup}.
 */
@Tag("integration")
public class RemoteFileContentLocalStackIT extends AbstractRemoteFileContentIT {

    private static final S3ContainerSetup SETUP = S3ContainerSetup.localStack();

    @BeforeAll
    static void beforeAll() {
        beforeAll(SETUP);
    }

    @AfterAll
    static void afterAll() {
        afterAll(SETUP);
    }
}
