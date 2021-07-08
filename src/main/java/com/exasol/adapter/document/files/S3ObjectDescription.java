package com.exasol.adapter.document.files;

/**
 * This class represents a reference to a S3 object.
 */
class S3ObjectDescription {
    private final S3Uri uri;
    private final long size;

    /**
     * Create a new instance of {@link S3ObjectDescription}.
     * 
     * @param uri  object's uri
     * @param size object's size
     */
    S3ObjectDescription(final S3Uri uri, final long size) {
        this.uri = uri;
        this.size = size;
    }

    /**
     * Get the object's uri.
     * 
     * @return object's uri
     */
    S3Uri getUri() {
        return this.uri;
    }

    /**
     * Get the object's size.
     * 
     * @return object's size
     */
    long getSize() {
        return this.size;
    }
}
