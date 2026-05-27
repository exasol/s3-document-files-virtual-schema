package com.exasol.adapter.document.files;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;

import org.junit.jupiter.api.Test;

import com.exasol.adapter.VirtualSchemaAdapter;
import com.exasol.adapter.document.DocumentAdapter;

class S3DocumentFilesAdapterFactoryTest {
    private final S3DocumentFilesAdapterFactory factory = new S3DocumentFilesAdapterFactory();

    @Test
    void createAdapterReturnsDocumentAdapter() {
        final VirtualSchemaAdapter adapter = this.factory.createAdapter(null);
        assertThat(adapter, instanceOf(DocumentAdapter.class));
    }

    @Test
    void getAdapterVersionReturnsVersion() {
        // Version only available in built artifact
        assertThat(this.factory.getAdapterVersion(), equalTo("UNKNOWN"));
    }

    @Test
    void getAdapterNameReturnsS3DocumentFiles() {
        assertThat(this.factory.getAdapterName(), equalTo(S3DocumentFilesAdapterFactory.ADAPTER_NAME));
    }

    @Test
    void getAdapterProjectShortTagReturnsVss3() {
        assertThat(this.factory.getAdapterProjectShortTag(), equalTo("VSS3"));
    }
}
