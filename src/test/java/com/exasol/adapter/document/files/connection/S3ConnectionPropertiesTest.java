package com.exasol.adapter.document.files.connection;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class S3ConnectionPropertiesTest {
    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(S3ConnectionProperties.class).verify();
    }
}
