package com.exasol.adapter.document.files;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.exasol.ExaConnectionInformation;
import com.exasol.adapter.document.documentfetcher.files.SegmentDescription;

class S3FileLoaderTest {

    @Test
    void testCreation() {
        final ExaConnectionInformation connectionInformation = mock(ExaConnectionInformation.class);
        when(connectionInformation.getAddress())
                .thenReturn("https://awsexamplebucket1.s3.us-west-2.amazonaws.com/data-files/");
        when(connectionInformation.getUser()).thenReturn("user");
        when(connectionInformation.getPassword()).thenReturn("pass");
        final S3FileLoader s3FileLoader = new S3FileLoader("book-*.json", SegmentDescription.NO_SEGMENTATION,
                connectionInformation);
        assertThat(s3FileLoader.getFilePattern(),
                equalTo("https://awsexamplebucket1.s3.us-west-2.amazonaws.com/data-files/book-*.json"));
    }

}