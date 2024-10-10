package com.repoachiever.service.vendor.common;

import com.repoachiever.dto.GitHubLocationNotationDto;
import com.repoachiever.entity.common.PropertiesEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Contains helpful tools used for vendor configuration.
 */
@ApplicationScoped
public class VendorConfigurationHelper {
    @Inject
    PropertiesEntity properties;

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
     * @return parsed location definition using GitHub notation.
     */
    public GitHubLocationNotationDto parseLocationGitHubNotation(String location) {
        Pattern pattern = Pattern.compile(properties.getGitHubLocationNotation());

        Matcher matcher = pattern.matcher(location);

        matcher.find();

        String optionalMatch = matcher.group(4);

        if (matcher.groupCount() == 4 && Objects.nonNull(optionalMatch)) {
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

    /**
     * Checks if given vendor external API is available.
     *
     * @param base given vendor external API base.
     * @return result of the check.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public Boolean isVendorAvailable(String base) {
        try {
            InetAddress.getByName(base);
        } catch (IOException e) {
            return false;
        }

        return true;
    }
}
