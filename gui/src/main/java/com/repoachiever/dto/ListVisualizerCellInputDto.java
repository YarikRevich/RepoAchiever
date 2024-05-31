package com.repoachiever.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Represents list visualizer cell input.
 */
@Getter
@RequiredArgsConstructor(staticName = "of")
public class ListVisualizerCellInputDto {
    private final String name;

    private final Boolean active;

    private Boolean empty = false;

    /**
     * Creates empty instance.
     *
     * @return created empty instance.
     */
    public static ListVisualizerCellInputDto empty() {
        ListVisualizerCellInputDto result =
                ListVisualizerCellInputDto.of(null, null);

        result.empty = true;

        return result;
    }
}
