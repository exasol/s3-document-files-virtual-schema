package com.exasol.adapter.document.files;

import lombok.Data;

/**
 * This class represents a reference to a S3 object.
 */
@Data
class S3ObjectDescription {
    private final String key;
    private final long size;
}
