package com.repoachiever.entity.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Represents additional content file, including prs, issues and releases, which are pulled from selected
 * resource provider.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class AdditionalContentFileEntity {
    @JsonProperty("data")
    private String data;
}