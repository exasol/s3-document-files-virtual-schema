package com.exasol.adapter.document.files;

import java.sql.ResultSet;

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

    @RepeatedTest(5)
    @Tag("regression")
    void testLoadSalesParquetFiles(final TestInfo testInfo) throws Exception {
        // SETUP.getStatement().executeUpdate("ALTER SESSION SET SCRIPT_OUTPUT_ADDRESS = '127.0.0.1:3000';");
        SETUP.createVirtualSchema("SALES_VS",
                () -> getClass().getClassLoader().getResourceAsStream("performanceTestMapping.json"));
        PerformanceTestRecorder.getInstance().recordExecution(testInfo, () -> {
            final ResultSet resultSet = SETUP.getStatement()
                    .executeQuery("SELECT COUNT(*) FROM SALES_VS.SALES_POSITION");
            resultSet.next();
            final long size = resultSet.getLong(1);
            // assertThat(size, equalTo(27363271L));
            System.out.println("Size: " + size);
        });
    }
}
