package com.repoachiever.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.repoachiever.exception.ContentRetrievalUnitToJsonConversionFailureException;
import com.repoachiever.model.ContentRetrievalUnit;

/**
 * Represents content retrieval unit to Json converter.
 */
public class ContentRetrievalUnitToJsonConverter {

    /**
     * Converts given content retrieval unit to Json.
     *
     * @param content given content retrieval unit to be converted.
     * @return converted content retrieval unit.
     * @throws ContentRetrievalUnitToJsonConversionFailureException if content retrieval unit to Json operation fails.
     */
    public static String convert(ContentRetrievalUnit content) throws ContentRetrievalUnitToJsonConversionFailureException {
        ObjectMapper mapper =
                new ObjectMapper()
                        .configure(SerializationFeature.INDENT_OUTPUT, true);

        try {
            return mapper.writeValueAsString(content);
        } catch (JsonProcessingException e) {
            throw new ContentRetrievalUnitToJsonConversionFailureException(e.getMessage());
        }
    }
}