package com.repoachiever.service.integration.event.common;

import com.repoachiever.entity.PropertiesEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * Contains helpful tools used for event configuration.
 */
@Component
public class EventConfigurationHelper {
    @Autowired
    private PropertiesEntity properties;

    /**
     * Formats output location with the given base.
     *
     * @param base given output location base.
     * @return formatted output location.
     */
    public String getOutputLocation(String base) {
        return String.format("%s-%s", base, UUID.randomUUID());
    }

    /**
     * Formats swap output location.
     *
     * @return formatted swap output location.
     */
    public String getSwapLocation() {
        return Paths.get(properties.getSwapRoot(), String.format("%s.swp", UUID.randomUUID())).toString();
    }
}