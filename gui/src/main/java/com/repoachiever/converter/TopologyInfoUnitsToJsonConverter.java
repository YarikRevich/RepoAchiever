package com.repoachiever.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.repoachiever.exception.TopologyInfoUnitsToJsonConversionFailureException;
import com.repoachiever.model.TopologyInfoUnit;

import java.util.List;

/**
 * Represents topology info units to Json converter.
 */
public class TopologyInfoUnitsToJsonConverter {

    /**
     * Converts given content units to Json.
     *
     * @param content given content to be converted.
     * @return converted content units.
     * @throws TopologyInfoUnitsToJsonConversionFailureException if topology info units to Json operation fails.
     */
    public static String convert(List<TopologyInfoUnit> content) throws
            TopologyInfoUnitsToJsonConversionFailureException {
        ObjectMapper mapper =
                new ObjectMapper()
                        .configure(SerializationFeature.INDENT_OUTPUT, true);

        try {
            return mapper.writeValueAsString(content);
        } catch (JsonProcessingException e) {
            throw new TopologyInfoUnitsToJsonConversionFailureException(e.getMessage());
        }
    }
}