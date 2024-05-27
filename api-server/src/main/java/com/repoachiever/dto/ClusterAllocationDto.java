package com.repoachiever.dto;

import com.repoachiever.model.LocationsUnit;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

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
     * Represents workspace unit key used to target RepoAchiever Cluster results.
     */
    private String workspaceUnitKey;

    /**
     * Represents locations selected for RepoAchiever Cluster allocation.
     */
    private List<LocationsUnit> locations;

    /**
     * Represents process identificator of RepoAchiever Cluster allocation.
     */
    private Integer pid;

    /**
     * Represents context used for RepoAchiever Cluster configuration.
     */
    private String context;
}
