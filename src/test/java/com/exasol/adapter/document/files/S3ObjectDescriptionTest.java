package com.exasol.adapter.document.files;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class S3ObjectDescriptionTest {
    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(S3ObjectDescription.class).verify();
    }
}
