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

    private final Boolean empty;

    private Boolean stub = false;

    /**
     * Creates stub instance.
     *
     * @return created stub instance.
     */
    public static ListVisualizerCellInputDto stub() {
        ListVisualizerCellInputDto result =
                ListVisualizerCellInputDto.of(null, null, null);

        result.stub = true;

        return result;
    }
}
