package com.exasol.adapter.document.files;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.internal.verification.AtLeast;

import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Exception;

class RequestRateRetryIteratorTest {

    @Test
    void testRetrying() {
        final ThrottledIterator source = spy(new ThrottledIterator());
        final RequestRateRetryIterator iterator = new RequestRateRetryIterator(source);
        final AtomicInteger resultCounter = new AtomicInteger();
        iterator.forEachRemaining(x -> resultCounter.incrementAndGet());
        assertThat(resultCounter.get(), Matchers.equalTo(100));
        verify(source, new AtLeast(110)).next();
    }

    @Test
    void testOtherExceptionsPass() {
        final AwsServiceException exception = S3Exception.builder().message("Something else went wrong").build();
        final Iterator<ListObjectsV2Response> source = mock(Iterator.class);
        when(source.hasNext()).thenReturn(true);
        when(source.next()).thenThrow(exception);
        final RequestRateRetryIterator iterator = new RequestRateRetryIterator(source);
        final AwsServiceException actualException = assertThrows(AwsServiceException.class, iterator::next);
        assertThat(actualException.getMessage(), equalTo(exception.getMessage()));
    }

    private static class ThrottledIterator implements Iterator<ListObjectsV2Response> {
        private int counter = 0;

        @Override
        public boolean hasNext() {
            return this.counter < 100;
        }

        @Override
        public ListObjectsV2Response next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            if (Math.random() < 0.8) {
                throw S3Exception.builder().message("Please reduce your request rate.").build();
            }
            this.counter++;
            return null;
        }
    }
}