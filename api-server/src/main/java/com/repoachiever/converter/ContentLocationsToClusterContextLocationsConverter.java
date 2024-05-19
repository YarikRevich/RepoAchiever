package com.repoachiever.converter;

import com.repoachiever.model.LocationsUnit;
import com.repoachiever.entity.common.ClusterContextEntity;

import java.util.List;

/**
 * Represents RepoAchiever Cluster content locations to context locations converter.
 */
public class ContentLocationsToClusterContextLocationsConverter {

    /**
     * Converts given content locations to context locations.
     *
     * @param contentLocations given content locations to be converted.
     * @return converted context locations.
     */
    public static List<ClusterContextEntity.Content.Location> convert(List<LocationsUnit> contentLocations) {
        return contentLocations
                .stream()
                .map(element -> ClusterContextEntity.Content.Location.of(
                        element.getName(), element.getAdditional()))
                .toList();
    }
}
