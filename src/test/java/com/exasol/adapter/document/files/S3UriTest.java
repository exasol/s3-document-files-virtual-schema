package com.exasol.adapter.document.files;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class S3UriTest {
    @CsvSource({ //
            "https://awsexamplebucket1.s3.us-west-2.amazonaws.com/photos/puppy.jpg, true",
            "http://awsexamplebucket1.s3.us-west-2.amazonaws.com/photos/puppy.jpg, false"//
    })
    @ParameterizedTest
    void testUseSsl(final String url, final boolean expected) {
        assertThat(S3Uri.fromString(url).isUseSsl(), equalTo(expected));
    }

    @Test
    void testBucket() {
        assertThat(S3Uri.fromString("https://awsexamplebucket1.s3.us-west-2.amazonaws.com/test").getBucket(),
                equalTo("awsexamplebucket1"));
    }

    @CsvSource({ //
            "https://awsexamplebucket1.s3.us-west-2.amazonaws.com/photos/puppy.jpg, false, amazonaws.com",
            "https://awsexamplebucket1.s3.us-west-2.aws.example.com/photos/puppy.jpg, true, aws.example.com", //
            "https://awsexamplebucket1.s3.us-west-2.127.0.0.1:1234/photos/puppy.jpg, true, 127.0.0.1:1234"//
    })

    @ParameterizedTest
    void testEndpoint(final String url, final boolean expectedHasEndpointOverride, final String expectedEndpoint) {
        final S3Uri s3Uri = S3Uri.fromString(url);
        assertAll(//
                () -> assertThat(s3Uri.hasEndpointOverride(), equalTo(expectedHasEndpointOverride)),
                () -> assertThat(s3Uri.getEndpointOverride(), equalTo(expectedEndpoint))//
        );
    }

    @Test
    void testRegion() {
        assertThat(S3Uri.fromString("https://awsexamplebucket1.s3.us-west-2.amazonaws.com/test").getRegion(),
                equalTo("us-west-2"));
    }

    @Test
    void testKey() {
        assertThat(S3Uri.fromString("https://awsexamplebucket1.s3.us-west-2.amazonaws.com/test/my-file.json").getKey(),
                equalTo("test/my-file.json"));
    }

    @CsvSource({ //
            "https://awsexamplebucket1.us-west-2.amazonaws.com/photos/puppy.jpg",
            "https://s3.us-west-2.aws.example.com/photos/puppy.jpg", //
            "awsexamplebucket1.s3.us-west-2.127.0.0.1:1234/photos/puppy.jpg"//
    })
    @ParameterizedTest
    void testInvalidSyntax(final String invalidUrl) {
        assertThrows(IllegalArgumentException.class, () -> S3Uri.fromString(invalidUrl));
    }
}