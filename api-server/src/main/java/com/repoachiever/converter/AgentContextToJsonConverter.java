package com.repoachiever.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.repoachiever.entity.ClusterContextEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** Represents agent context to json converter. */
public class AgentContextToJsonConverter {
  private static final Logger logger = LogManager.getLogger(AgentContextToJsonConverter.class);

  public static String convert(ClusterContextEntity content) {
    ObjectMapper mapper =
        new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES, true)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
            .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

    try {
      return mapper.writeValueAsString(content);
    } catch (JsonProcessingException e) {
      logger.fatal(e.getMessage());
    }

    return null;
  }
}
