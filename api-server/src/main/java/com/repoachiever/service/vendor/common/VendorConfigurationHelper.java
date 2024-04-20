package com.repoachiever.service.vendor.common;

/**
 * Contains helpful tools used for vendor configuration.
 */
public class VendorConfigurationHelper {
    /**
     * Converts given raw token value to a wrapped format.
     *
     * @param token given raw token value.
     * @return wrapped token.
     */
    public static String getWrappedToken(String token) {
        return String.format("Bearer %s", token);
    }
}
