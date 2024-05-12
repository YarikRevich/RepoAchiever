package com.repoachiever.logging.common;

/**
 * Contains helpful tools used for logging configuration.
 */
public class LoggingConfigurationHelper {
    /**
     * Checks if the given log message contains given prefix.
     *
     * @param prefix given transferable message prefix.
     * @param message given log message.
     * @return result of the check.
     */
    public static Boolean isMessageTransferable(String prefix, String message) {
        return message.contains(prefix);
    }

    /**
     * Formats transferable message with the given prefix.
     *
     * @param prefix given transferable message prefix.
     * @param message given formatted transferable log message.
     * @return formatted transferable message.
     */
    public static String extractTransferableMessage(String prefix, String message) {
        return message.replaceAll(prefix, "");
    }

    /**
     * Formats transferable message with the given prefix.
     *
     * @param prefix given transferable message prefix.
     * @param message given log message.
     * @return formatted transferable message.
     */
    public static String getTransferableMessage(String prefix, String message) {
        return String.format("%s %s", prefix, message);
    }
}
