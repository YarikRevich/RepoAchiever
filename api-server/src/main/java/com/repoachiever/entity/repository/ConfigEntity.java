package com.repoachiever.entity.repository;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents entity used to describe added configuration.
 */
@Getter
@AllArgsConstructor(staticName = "of")
public class ConfigEntity {
    private Integer id;

    private String name;

    private String hash;
}
