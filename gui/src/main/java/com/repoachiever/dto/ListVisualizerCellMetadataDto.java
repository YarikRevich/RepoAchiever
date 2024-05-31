package com.repoachiever.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents list visualizer cell metadata.
 */
@Getter
@AllArgsConstructor(staticName = "of")
public class ListVisualizerCellMetadataDto {
    /**
     * Represents state if content is present or not.
     */
    private Boolean empty;

    /**
     * Represents state if configuration file is opened or not.
     */
    private Boolean opened;
}
