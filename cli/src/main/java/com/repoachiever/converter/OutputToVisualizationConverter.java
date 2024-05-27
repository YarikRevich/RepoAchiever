package com.repoachiever.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Represents output to visualization converter.
 */
public class OutputToVisualizationConverter {

    /**
     * Converts given output to visualization.
     *
     * @param output given output to be converted.
     * @return converted output.
     */
    public static String convert(Object output) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        DefaultPrettyPrinter.Indenter indenter =
                new DefaultIndenter("    ", DefaultIndenter.SYS_LF);
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        printer.indentObjectsWith(indenter);
        printer.indentArraysWith(indenter);

        return mapper.writer(printer).writeValueAsString(output);
    }
}
