package com.repoachiever.entity;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * Exposes access to properties setup to be used for further configuration.
 */
@Getter
@ApplicationScoped
public class PropertiesEntity {
    @ConfigProperty(name = "quarkus.application.version")
    String applicationVersion;

    @ConfigProperty(name = "quarkus.http.host")
    String applicationHost;

    @ConfigProperty(name = "quarkus.http.port")
    Integer applicationPort;

    @ConfigProperty(name = "config.directory")
    String configDirectory;

    @ConfigProperty(name = "workspace.directory")
    String workspaceDirectory;

    @ConfigProperty(name = "workspace.content.directory")
    String workspaceContentDirectory;

    @ConfigProperty(name = "workspace.content.version-amount")
    Integer workspaceContentVersionAmount;

    @ConfigProperty(name = "workspace.metadata.directory")
    String workspaceMetadataDirectory;

    @ConfigProperty(name = "workspace.prs-metadata-file.name")
    String workspacePRsMetadataFileName;

    @ConfigProperty(name = "workspace.issues-metadata-file.name")
    String workspaceIssuesMetadataFileName;

    @ConfigProperty(name = "workspace.releases-metadata-file.name")
    String workspaceReleasesMetadataFileName;

    @ConfigProperty(name = "repoachiever-cluster.context.alias")
    String repoAchieverClusterContextAlias;

    @ConfigProperty(name = "git.commit.id.abbrev")
    String gitCommitId;

    /**
     * Removes the last symbol in git commit id of the repository.
     *
     * @return chopped repository git commit id.
     */
    public String getGitCommitId() {
        return StringUtils.chop(gitCommitId);
    }
}
