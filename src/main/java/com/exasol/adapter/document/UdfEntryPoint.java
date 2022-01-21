package com.exasol.adapter.document;

import com.exasol.ExaIterator;
import com.exasol.ExaMetadata;
import com.exasol.adapter.document.files.S3VsConstants;

/**
 * UDF entry point for the files virtual schema.
 * <p>
 * !!! Don't change class name / package. They are referred in the UDF definition and should stay compatible.
 * </p>
 */
public class UdfEntryPoint {
    private UdfEntryPoint() {
        // static class
    }

    /**
     * This method is called by the Exasol database when the IMPORT_FROM_ UDF is called.
     *
     * @param exaMetadata exasol metadata
     * @param exaIterator iterator
     * @throws Exception if data can't get loaded
     */
    @SuppressWarnings({ "java:S112", "java:S1130" }) // Exception is too generic and not thrown. This signature is
                                                     // however given by the
                                                     // UDF framework
    public static void run(final ExaMetadata exaMetadata, final ExaIterator exaIterator) throws Exception {
        new GenericUdfCallHandler(S3VsConstants.USER_GUIDE_URL).run(exaMetadata, exaIterator);
    }
}
