package com.repoachiever.service.workspace.common;

/**
 * Contains helpful tools used for workspace configuration.
 */
public class WorkspaceConfigurationHelper {

    /**
     * Creates folder definition with the help of the given folder name for ZIP achieve.
     *
     * @param name given folder name value.
     * @return wrapped token.
     */
    public static String getZipFolderDefinition(String name) {
        return String.format("%s/", name);
    }
}