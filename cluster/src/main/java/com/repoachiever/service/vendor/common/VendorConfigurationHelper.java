package com.repoachiever.service.vendor.common;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.repoachiever.dto.GitHubLocationNotationDto;
import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.exception.LocationDefinitionsAreNotValidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Contains helpful tools used for vendor configuration.
 */
@Component
public class VendorConfigurationHelper {
    @Autowired
    private PropertiesEntity properties;

    /**
     * Converts given raw token value to a wrapped format.
     *
     * @param token given raw token value.
     * @return wrapped token.
     */
    public String getWrappedToken(String token) {
        return String.format("Bearer %s", token);
    }

    /**
     * Parses given raw location definition using GitHub notation.
     *
     * @param location given raw location definition.
     * @throws LocationDefinitionsAreNotValidException if location definition is not valid.
     * @return parsed location definition using GitHub notation.
     */
    public GitHubLocationNotationDto parseLocationGitHubNotation(String location)
            throws LocationDefinitionsAreNotValidException {
        Pattern pattern = Pattern.compile(properties.getGitHubLocationNotation());

        Matcher matcher = pattern.matcher(location);

        if (!matcher.find()) {
            throw new LocationDefinitionsAreNotValidException();
        }

        if (matcher.groupCount() == 4) {
            return GitHubLocationNotationDto.of(
                    matcher.group(1),
                    matcher.group(2),
                    Optional.of(matcher.group(4)));
        }

        return GitHubLocationNotationDto.of(
                matcher.group(1),
                matcher.group(2),
                Optional.empty());
    }
}