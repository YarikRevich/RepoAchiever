package com.repoachiever.dto;

import com.repoachiever.entity.ConfigEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents visualizer cell input.
 */
@Getter
@AllArgsConstructor(staticName = "of")
public class VisualizerCellDto {
    private String name;

    private Boolean additional;
}
