package com.repoachiever.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents RepoAchiever Cluster allocation.
 */
@Getter
@AllArgsConstructor(staticName = "of")
public class ClusterAllocationDto {
    private String name;

    private Integer pid;

    private String context;
}
