package com.repoachiever.entity.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents entity used to describe available providers.
 */
@Getter
@AllArgsConstructor(staticName = "of")
public class ProviderEntity {
    private Integer id;

    private String name;
}
