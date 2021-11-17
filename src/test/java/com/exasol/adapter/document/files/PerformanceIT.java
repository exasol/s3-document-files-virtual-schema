package com.exasol.adapter.document.files;

import static org.hamcrest.MatcherAssert.assertThat;

import java.sql.ResultSet;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;

import com.exasol.adapter.document.edml.*;
import com.exasol.adapter.document.edml.serializer.EdmlSerializer;
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

    @AfterEach
    void afterEach() {
        SETUP.dropCreatedObjects();
    }

    @Test
    @Tag("regression")
    void testLoadSalesParquetFiles(final TestInfo testInfo) throws Exception {
        runSalesPositionsTest(testInfo, "parquet_sales_pos_200mb*.parquet");
    }

    @Test
    @Tag("regression")
    void testLoadFewBigSalesParquetFiles(final TestInfo testInfo) throws Exception {
        runSalesPositionsTest(testInfo, "parquet_sales_pos/*.parquet");
    }

    private void runSalesPositionsTest(final TestInfo testInfo, final String source) throws Exception {
        SETUP.createVirtualSchema("SALES_VS", getSalesPositionMapping(source));
        for (int runCounter = 0; runCounter < 5; runCounter++) {
            PerformanceTestRecorder.getInstance().recordExecution(testInfo, () -> {
                final ResultSet resultSet = SETUP.getStatement()
                        .executeQuery("SELECT COUNT(*) FROM (SELECT * FROM SALES_VS.SALES_POSITION)");
                resultSet.next();
                final long size = resultSet.getLong(1);
                assertThat(size, Matchers.equalTo(3621459710L));
            });
        }
    }

    private String getSalesPositionMapping(final String source) {
        final EdmlDefinition edmlDefinition = EdmlDefinition.builder()
                .schema("https://schemas.exasol.com/edml-1.3.0.json")//
                .source(source)//
                .destinationTable("SALES_POSITION")//
                .addSourceReferenceColumn(true)//
                .mapping(buildColumnMappingForSalesPositions()).build();
        return new EdmlSerializer().serialize(edmlDefinition);
    }

    private Fields buildColumnMappingForSalesPositions() {
        return Fields.builder()//
                .mapField("sales_id", ToDecimalMapping.builder().decimalPrecision(12).decimalScale(0).build())
                .mapField("product_id", ToDecimalMapping.builder().decimalPrecision(12).decimalScale(0).build())
                .mapField("exa_row_roles", ToDecimalMapping.builder().decimalPrecision(12).decimalScale(0).build())
                .mapField("sales_price", ToDecimalMapping.builder().decimalPrecision(12).decimalScale(2).build())
                .mapField("product_price", ToDecimalMapping.builder().decimalPrecision(12).decimalScale(2).build())
                .mapField("quantity", ToDecimalMapping.builder().decimalPrecision(4).decimalScale(0).build())
                .mapField("sales_position", ToDecimalMapping.builder().decimalPrecision(4).decimalScale(0).build())
                .build();
    }
}
