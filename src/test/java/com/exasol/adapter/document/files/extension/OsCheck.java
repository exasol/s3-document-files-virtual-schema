package com.exasol.adapter.document.files.extension;

import java.util.Locale;

/**
 * Helper class to check the operating system this Java VM runs in.
 */
public class OsCheck {
    /**
     * Get the suffix of native executables for the current operating system.
     * 
     * @return the suffix of native executables
     */
    public static String getExecutableSuffix() {
        final String os = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
        if (os.indexOf("win") >= 0) {
            return ".exe";
        } else {
            return "";
        }
    }
}