package com.repoachiever.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.repoachiever.entity.common.AdditionalContentFileEntity;
import com.repoachiever.entity.common.ClusterContextEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Represents RepoAchiever Cluster additional content file to Json converter.
 */
public class AdditionalContentFileToJsonConverter {
    private static final Logger logger = LogManager.getLogger(AdditionalContentFileToJsonConverter.class);

    /**
     * Converts given additional content file to Json.
     *
     * @param additionalContent given additional content file to be converted.
     * @return converted additional content file.
     */
    public static String convert(AdditionalContentFileEntity additionalContent) {
        ObjectMapper mapper =
                new ObjectMapper()
                        .configure(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES, true)
                        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
                        .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        try {
            return mapper.writeValueAsString(additionalContent);
        } catch (JsonProcessingException e) {
            logger.fatal(e.getMessage());
        }

        return null;
    }
}