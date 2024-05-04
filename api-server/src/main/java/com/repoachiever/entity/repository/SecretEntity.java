package com.repoachiever.entity.repository;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents entity used to describe registered secrets.
 */
@Getter
@AllArgsConstructor(staticName = "of")
public class SecretEntity {
    private Integer id;

    private Integer session;

    private String token;
}
