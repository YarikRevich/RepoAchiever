package com.repoachiever.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * Represents RepoAchiever Cluster additional content data to Json converter.
 */
public class AdditionalContentDataToJsonConverter {
    private static final Logger logger = LogManager.getLogger(AdditionalContentDataToJsonConverter.class);

    /**
     * Converts given additional content data to Json.
     *
     * @param data given additional content data to be converted.
     * @return converted additional content file.
     */
    public static String convert(Map<String, String> data) {
        ObjectMapper mapper =
                new ObjectMapper()
                        .configure(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES, true)
                        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
                        .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        try {
            return mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            logger.fatal(e.getMessage());
        }

        return null;
    }
}