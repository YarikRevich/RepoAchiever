package com.repoachiever.entity.repository;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

/**
 * Represents entity used to describe registered locations.
 */
@Getter
@AllArgsConstructor(staticName = "of")
public class ContentEntity {
    private Integer id;

    private String location;

    private Boolean additional;

    private Integer provider;

    private Optional<Integer> exporter;

    private Integer secret;
}