package com.repoachiever.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

/** Represents location parsed using GitHub notation. */
@Getter
@AllArgsConstructor(staticName = "of")
public class GitHubLocationNotationDto {
    private String owner;

    private String name;

    @Setter
    private Optional<String> branch;
}
