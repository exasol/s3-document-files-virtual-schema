package com.exasol.adapter.document.files;

import static org.hamcrest.MatcherAssert.assertThat;

import java.sql.ResultSet;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;

import com.exasol.adapter.document.files.s3testsetup.AwsS3TestSetup;
import com.exasol.adapter.document.files.s3testsetup.S3TestSetup;
import com.exasol.performancetestrecorder.PerformanceTestRecorder;

class PerformanceIT {
    private static final S3TestSetup AWS_S3_TEST_SETUP = new AwsS3TestSetup();
    private static IntegrationTestSetup SETUP;

    @BeforeAll
    static void beforeAll() throws Exception {
        SETUP = new IntegrationTestSetup(AWS_S3_TEST_SETUP, "tbexastaging");
    }

    @AfterAll
    static void afterAll() throws Exception {
        SETUP.close();
    }

    @Test
    @Tag("regression")
    void testLoadSalesParquetFiles(final TestInfo testInfo) throws Exception {
        SETUP.createVirtualSchema("SALES_VS",
                () -> getClass().getClassLoader().getResourceAsStream("performanceTestMapping.json"));
        for (int runCounter = 0; runCounter < 5; runCounter++) {
            PerformanceTestRecorder.getInstance().recordExecution(testInfo, () -> {
                final ResultSet resultSet = SETUP.getStatement()
                        .executeQuery("SELECT COUNT(*) FROM SALES_VS.SALES_POSITION");
                resultSet.next();
                final long size = resultSet.getLong(1);
                assertThat(size, Matchers.equalTo(3621459710L));
            });
        }
    }
}
