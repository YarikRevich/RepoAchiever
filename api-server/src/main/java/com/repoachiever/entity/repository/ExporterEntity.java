package com.repoachiever.entity.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents entity used to describe available exporter.
 */
@Getter
@AllArgsConstructor(staticName = "of")
public class ExporterEntity {
    private Integer id;

    private String host;
}
