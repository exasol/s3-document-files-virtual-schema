package com.exasol.adapter.document.files;

import java.util.Objects;

/**
 * This class represents a reference to a S3 object.
 */
final class S3ObjectDescription {
    private final String key;
    private final long size;

    /**
     * Create a new instance.
     * 
     * @param key  key of the S3 object
     * @param size size of the S3 object in bytes
     */
    public S3ObjectDescription(final String key, final long size) {
        this.key = key;
        this.size = size;
    }

    /**
     * Get the of the S3 object.
     * 
     * @return key of the S3 object
     */
    public String getKey() {
        return key;
    }

    /**
     * Get the size of the S3 object in bytes.
     * 
     * @return size of the S3 object in bytes
     */
    public long getSize() {
        return size;
    }

    @Override
    public String toString() {
        return "S3ObjectDescription [key=" + key + ", size=" + size + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, size);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final S3ObjectDescription other = (S3ObjectDescription) obj;
        return Objects.equals(key, other.key) && size == other.size;
    }
}
