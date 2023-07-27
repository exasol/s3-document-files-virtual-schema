package com.exasol.adapter.document.files.itest.randomaccessinputstream;

import com.exasol.adapter.document.files.s3testsetup.S3ContainerSetup;

/**
 * See parent class {@link AbstractRandomAccessInputStreamIT} for actual test cases which are generic for all test setup
 * flavors implementing interface {@code S3ContainerSetup}.
 */
class RandomAccessInputStreamLocalStackIT extends AbstractRandomAccessInputStreamIT {
    @Override
    protected void prepareTestSetup(final byte[] bytes) {
        prepareTestSetup(S3ContainerSetup.localStack(), bytes);
    }
}
