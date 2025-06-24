package com.exasol.adapter.document.files;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.exasol.adapter.document.documentfetcher.files.RemoteFile;
import com.exasol.adapter.document.files.connection.S3ConnectionProperties;
import com.exasol.adapter.document.files.stringfilter.StringFilter;
import com.exasol.adapter.document.iterators.CloseableIterator;

class S3FileFinderTest {

    private Logger mockLogger;
    private S3FileFinder s3FileFinder;

    private StringFilter mockFilePattern;
    private com.exasol.adapter.document.files.stringfilter.matcher.Matcher mockMatcher;

    @BeforeEach
    void setUp() {
        mockLogger = mock(Logger.class);
        when(mockLogger.isLoggable(Level.FINE)).thenReturn(true);

        mockFilePattern = mock(StringFilter.class);
        mockMatcher = mock(com.exasol.adapter.document.files.stringfilter.matcher.Matcher.class);
        when(mockFilePattern.getDirectoryIgnoringMatcher()).thenReturn(mockMatcher);

        S3ConnectionProperties mockConnectionProperties = mock(S3ConnectionProperties.class);
        when(mockConnectionProperties.getAwsRegion()).thenReturn("eu-central-1");
        when(mockConnectionProperties.isAnonymous()).thenReturn(false);
        when(mockConnectionProperties.hasAwsSessionToken()).thenReturn(false);
        when(mockConnectionProperties.getAwsAccessKeyId()).thenReturn("dummy-access-key");
        when(mockConnectionProperties.getAwsSecretAccessKey()).thenReturn("dummy-secret-key");
        s3FileFinder = new S3FileFinder(mockFilePattern, mockConnectionProperties, mockLogger) {
            @Override
            CloseableIterator<S3ObjectDescription> getQuickFilteredObjectKeys() {
                // Provide a simple CloseableIterator implementation over 2 mock S3ObjectDescriptions
                S3ObjectDescription obj1 = mock(S3ObjectDescription.class);
                when(obj1.getKey()).thenReturn("file1.txt");
                S3ObjectDescription obj2 = mock(S3ObjectDescription.class);
                when(obj2.getKey()).thenReturn("file2.log");

                List<S3ObjectDescription> objs = List.of(obj1, obj2);

                return new CloseableIterator<>() {
                    private int index = 0;

                    @Override
                    public boolean hasNext() {
                        return index < objs.size();
                    }

                    @Override
                    public S3ObjectDescription next() {
                        return objs.get(index++);
                    }

                    @Override
                    public void close() {
                        // no-op
                    }
                };
            }

            @Override
            RemoteFile getS3Object(S3ObjectDescription s3ObjectDescription) {
                return new RemoteFile(s3ObjectDescription.getKey(), 12345L, null);
            }
        };
    }

    @Test
    void testLoadFiles_logsExpectedMessages() {
        when(mockMatcher.matches("file1.txt")).thenReturn(true);
        when(mockMatcher.matches("file2.log")).thenReturn(false);

        CloseableIterator<RemoteFile> iterator = s3FileFinder.loadFiles();

        // Fully consume the iterator to trigger all filtering and logging
        while (iterator.hasNext()) {
            RemoteFile file = iterator.next();
            // Assert only "file1.txt" passes the filter
            assertEquals("file1.txt", file.getResourceName());
        }

        // Capture all lazy log messages (Supplier<String>)
        @SuppressWarnings("unchecked")
        ArgumentCaptor<Supplier<String>> captor = ArgumentCaptor.forClass(Supplier.class);
        verify(mockLogger, atLeast(1)).fine(captor.capture());

        // Join all log messages by evaluating each Supplier
        String allLogs = captor.getAllValues().stream()
                .map(Supplier::get)
                .reduce("", (a, b) -> a + "\n" + b);

        // Assert logs contain expected fragments
        assertAll(
                () -> assertTrue(allLogs.contains("Starting to load files using S3 file pattern matcher.")),
                () -> assertTrue(allLogs.contains("Checking if key matches pattern: file1.txt => true")),
                () -> assertTrue(allLogs.contains("Checking if key matches pattern: file2.log => false")),
                () -> assertTrue(allLogs.contains("Filtered object keys with file pattern matcher.")),
                () -> assertTrue(allLogs.contains("Transformed S3 object to RemoteFile: file1.txt"))
        );
    }
}