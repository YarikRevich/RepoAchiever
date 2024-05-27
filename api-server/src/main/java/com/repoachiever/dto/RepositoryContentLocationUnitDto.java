package com.repoachiever.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents repository content location unit.
 */
@Getter
@AllArgsConstructor(staticName = "of")
public class RepositoryContentLocationUnitDto {
    private String location;

    private Boolean additional;
}