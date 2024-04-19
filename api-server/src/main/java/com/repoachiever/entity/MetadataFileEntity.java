package com.repoachiever.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Represents metadata file, including pull requests, issues and releases, which are pulled from selected resource provider.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class MetadataFileEntity {
    @JsonProperty("data")
    private String data;

    @JsonProperty("hash")
    private String hash;
}