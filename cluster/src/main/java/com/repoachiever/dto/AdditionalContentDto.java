package com.repoachiever.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Represents additional content file, which are pulled from selected resource provider.
 */
@Getter
@AllArgsConstructor(staticName = "of")
public class AdditionalContentDto {
    /**
     * Describes name of the additional content in a form of hash, used to distinguish content.
     */
    private String hash;

    /**
     * Retrieved raw content from the external resource.
     */
    private Map<String, String> data;
}