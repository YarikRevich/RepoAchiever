package com.repoachiever.service.vendor.common;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.repoachiever.dto.GitHubLocationNotationDto;
import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.exception.LocationDefinitionsAreNotValidException;
import com.repoachiever.logging.common.LoggingConfigurationHelper;
import jakarta.xml.bind.DatatypeConverter;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetAddress;
import java.security.MessageDigest;
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

    /**
     * Creates additional content hash from the given segments.
     *
     * @param segments given segments to be used for additional content hash creation.
     * @return created additional content hash from the given segments.
     */
    @SneakyThrows
    public String createAdditionalContentHash(String... segments) {
        MessageDigest md = MessageDigest.getInstance("SHA3-256");
        return DatatypeConverter.printHexBinary(md.digest(String.join(".", segments).getBytes()));
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