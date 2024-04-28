package com.repoachiever.dto;

import lombok.AllArgsConstructor;

/**
 * Represents payload for Grafana dashboard import operation.
 */
@AllArgsConstructor(staticName = "of")
public class GrafanaDashboardImportDto {
    private String dashboard;

    private Integer folderId;

    private Boolean overwrite;
}
