package com.repoachiever.converter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.repoachiever.exception.ContentUnitsToJsonConversionFailureException;
import com.repoachiever.model.ContentUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Represents content units to Json converter.
 */
public class ContentUnitsToJsonConverter {
    private static final Logger logger = LogManager.getLogger(ContentUnitsToJsonConverter.class);

    /**
     * Converts given content units to Json.
     *
     * @param content given content to be converted.
     * @return converted content units.
     * @throws ContentUnitsToJsonConversionFailureException if content units to Json operation fails.
     */
    public static String convert(List<ContentUnit> content) throws ContentUnitsToJsonConversionFailureException {
        ObjectMapper mapper =
                new ObjectMapper()
                        .configure(SerializationFeature.INDENT_OUTPUT, true);

        try {
            return mapper.writeValueAsString(content);
        } catch (JsonProcessingException e) {
            throw new ContentUnitsToJsonConversionFailureException(e.getMessage());
        }
    }
}