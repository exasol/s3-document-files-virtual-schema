package com.exasol.adapter.document.files;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.exasol.adapter.document.files.container.MinioContainer;
import com.exasol.adapter.document.files.container.S3InterfaceContainer;

/**
 * See parent class {@link S3FileFinderIT} for actual test cases which are generic for all container flavors
 * implementing interface {@code S3InterfaceContainer}.
 */
@Tag("integration")
@Testcontainers
class FileFinderMinioIT extends S3FileFinderIT {

    @Container
    private static final S3InterfaceContainer container = new MinioContainer();

    @BeforeAll
    static void beforeAll() {
        beforeAll(container);
    }

}