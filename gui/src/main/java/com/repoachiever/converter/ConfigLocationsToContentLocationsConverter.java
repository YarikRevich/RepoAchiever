package com.repoachiever.converter;

import com.repoachiever.entity.ConfigEntity;
import com.repoachiever.model.ContentUnit;
import com.repoachiever.model.LocationsUnit;

import java.util.List;

/**
 * Represents config locations to RepoAchiever API Server content locations converter.
 */
public class ConfigLocationsToContentLocationsConverter {

    /**
     * Converts given config locations to content locations.
     *
     * @param configLocations given config locations to be converted.
     * @return converted content locations.
     */
    public static ContentUnit convert(List<ConfigEntity.Content.Location> configLocations) {
        return ContentUnit.of(configLocations
                .stream()
                .map(element -> LocationsUnit.of(
                        element.getName(), element.getAdditional()))
                .toList());
    }
}
