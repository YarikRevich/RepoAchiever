package com.repoachiever.dto;

import com.repoachiever.entity.common.ClusterContextEntity;
import com.repoachiever.model.CredentialsFieldsFull;
import com.repoachiever.model.Provider;
import com.repoachiever.model.Exporter;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

/**
 * Represents repository content unit.
 */
@Getter
@AllArgsConstructor(staticName = "of")
public class RepositoryContentUnitDto {
    private String location;

    private Provider provider;

    private Optional<Exporter> exporter;

    private CredentialsFieldsFull credentials;
}