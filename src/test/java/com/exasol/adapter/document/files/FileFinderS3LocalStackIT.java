package com.exasol.adapter.document.files;

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import com.exasol.adapter.document.files.container.LocalStackS3Container;
import com.exasol.adapter.document.files.s3testsetup.LocalStackS3TestSetup;

/**
 * See parent class {@link S3FileFinderIT} for actual test cases which are generic for all container flavors
 * implementing interface {@code S3InterfaceContainer}.
 */
@Tag("integration")
@Testcontainers
class FileFinderS3LocalStackIT extends S3FileFinderIT {

    @Container
    static final LocalStackContainer CONTAINER = new LocalStackContainer(
            DockerImageName.parse(LocalStackS3TestSetup.LOCALSTACK_CONTAINER)) //
                    .withServices(S3) //
                    .withReuse(true);

    @BeforeAll
    static void beforeAll() {
        beforeAll(new LocalStackS3Container(CONTAINER));
    }
}