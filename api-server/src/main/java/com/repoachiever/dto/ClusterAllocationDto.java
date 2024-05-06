package com.repoachiever.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents RepoAchiever Cluster allocation.
 */
@Getter
@AllArgsConstructor(staticName = "of")
public class ClusterAllocationDto {
    /**
     * Represents name of RepoAchiever Cluster allocation.
     */
    private String name;

    /**
     * Represents process identificator of RepoAchiever Cluster allocation.
     */
    private Integer pid;

    /**
     * Represents context used for RepoAchiever Cluster configuration.
     */
    private String context;

    /**
     * Represents workspace unit key used to target RepoAchiever Cluster results.
     */
    private String workspaceUnitKey;
}
