package com.exasol.adapter.document.files;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

class TestConfigTest {
    @Test
    void testEqualsContract() {
        EqualsVerifier.forClass(TestConfig.class).suppress(Warning.NONFINAL_FIELDS).verify();
    }
}
