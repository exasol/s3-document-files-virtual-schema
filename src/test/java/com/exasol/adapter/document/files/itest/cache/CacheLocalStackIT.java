package com.exasol.adapter.document.files.itest.cache;

import org.junit.jupiter.api.*;

import com.exasol.adapter.document.files.s3testsetup.S3ContainerSetup;

/**
 * See parent class {@link AbstractCacheIT} for actual test cases which are generic for all test setup flavors
 * implementing interface {@code S3ContainerSetup}.
 */
@Tag("integration")
class CacheLocalStackIT extends AbstractCacheIT {

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
