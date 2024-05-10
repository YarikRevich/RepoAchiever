package com.repoachiever.dto;

import com.repoachiever.entity.common.ClusterContextEntity;
import com.repoachiever.model.CredentialsFieldsFull;
import com.repoachiever.model.Provider;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents repository content unit.
 */
@Getter
@AllArgsConstructor(staticName = "of")
public class RepositoryContentUnitDto {
    private String location;

    private Provider provider;

    private CredentialsFieldsFull credentials;
}