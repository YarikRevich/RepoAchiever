package com.repoachiever.converter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.repoachiever.exception.JsonToAdditionalContentDataFailureException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Represents RepoAchiever API Server additional content data to Json converter.
 */
public class JsonToAdditionalContentDataConverter {
    private static final Logger logger = LogManager.getLogger(JsonToAdditionalContentDataConverter.class);

    /**
     * Converts given Json to additional content data.
     *
     * @param request given additional content data to be converted.
     * @return converted additional content data.
     */
    public static Map<String, String> convert(String data) {
        ObjectMapper mapper =
                new ObjectMapper()
                        .configure(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES, true)
                        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
                        .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        ObjectReader reader = mapper.reader().forType(new TypeReference<Map<String, String>>() {
        });

        Map<String, String> result = null;

        try {
            List<Map<String, String>> values = reader.<Map<String, String>>readValues(data).readAll();
            if (values.isEmpty()) {
                logger.fatal(new JsonToAdditionalContentDataFailureException().getMessage());

                return null;
            }

            result = values.getFirst();
        } catch (IOException e) {
            logger.fatal(new JsonToAdditionalContentDataFailureException(e.getMessage()).getMessage());
        }

        return result;
    }
}
