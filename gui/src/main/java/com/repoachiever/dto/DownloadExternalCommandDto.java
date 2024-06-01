package com.repoachiever.dto;

import com.repoachiever.entity.ConfigEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents input for download external command.
 */
@Getter
@AllArgsConstructor(staticName = "of")
public class DownloadExternalCommandDto {
    private ConfigEntity config;

    private String location;
}
