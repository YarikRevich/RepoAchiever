package com.repoachiever.logging.common;

/**
 * Contains helpful tools used for logging configuration.
 */
public class LoggingConfigurationHelper {
    private static final String TRANSFERABLE_MESSAGE_PREFIX = "!transferable!";

    /**
     * Checks if the given log message contains given prefix.
     *
     * @param message given log message.
     * @return result of the check.
     */
    public static Boolean isMessageTransferable(String message) {
        return message.contains(TRANSFERABLE_MESSAGE_PREFIX);
    }

    /**
     * Formats transferable message with the given prefix.
     *
     * @param message given formatted transferable log message.
     * @return formatted transferable message.
     */
    public static String extractTransferableMessage(String message) {
        return message.replaceAll(TRANSFERABLE_MESSAGE_PREFIX, "");
    }

    /**
     * Formats transferable message with the given prefix.
     *
     * @param message given log message.
     * @return formatted transferable message.
     */
    public static String getTransferableMessage(String message) {
        return String.format("%s %s", TRANSFERABLE_MESSAGE_PREFIX, message);
    }
}
