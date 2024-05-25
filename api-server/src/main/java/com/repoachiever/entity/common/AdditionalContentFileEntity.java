package com.repoachiever.entity.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents additional content file, which are pulled from selected resource provider.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class AdditionalContentFileEntity {
    /**
     * Represents data section used to hold different types of additional content.
     */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor(staticName = "of")
    public static class Data {
        @JsonProperty("name")
        private String name;

        @JsonProperty("content")
        private String content;
    }

    @Valid
    @JsonProperty("data")
    private List<Data> data;
}