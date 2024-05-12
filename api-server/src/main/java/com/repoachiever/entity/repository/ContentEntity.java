package com.repoachiever.entity.repository;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents entity used to describe registered locations.
 */
@Getter
@AllArgsConstructor(staticName = "of")
public class ContentEntity {
    private Integer id;

    private String location;

    private Integer provider;

    private Integer secret;
}