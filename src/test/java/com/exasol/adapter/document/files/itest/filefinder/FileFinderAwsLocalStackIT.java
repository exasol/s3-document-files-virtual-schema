package com.exasol.adapter.document.files.itest.filefinder;

import org.junit.jupiter.api.*;

import com.exasol.adapter.document.files.s3testsetup.S3ContainerSetup;

/**
 * See parent class {@link AbstractFileFinderIT} for actual test cases which are generic for all test setup flavors
 * implementing interface {@code S3ContainerSetup}.
 */
@Tag("integration")
class FileFinderAwsLocalStackIT extends AbstractFileFinderIT {

    static final S3ContainerSetup SETUP = S3ContainerSetup.awsLocalStack();

    @BeforeAll
    static void beforeAll() {
        beforeAll(SETUP);
    }

    @AfterAll
    static void afterAll() {
        afterAll(SETUP);
    }
}