package com.repoachiever.converter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.repoachiever.exception.JsonToGitHubPullRequestsFailureException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;

/**
 * Represents RepoAchiever Cluster Json to GitHub pull requests converter.
 */
public class JsonToGitHubPullRequestsConverter {
    private static final Logger logger = LogManager.getLogger(JsonToGitHubPullRequestsConverter.class);

    /**
     * Converts given Json to GitHub pull requests.
     *
     * @param data given GitHub pull requests to be converted.
     * @return converted GitHub pull requests.
     */
    public static List<Object> convert(String data) {
        ObjectMapper mapper =
                new ObjectMapper()
                        .configure(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES, true)
                        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
                        .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        ObjectReader reader = mapper.reader().forType(new TypeReference<List<Object>>() {
        });

        List<Object> result = null;

        try {
            List<List<Object>> values = reader.<List<Object>>readValues(data).readAll();
            if (values.isEmpty()) {
                logger.fatal(new JsonToGitHubPullRequestsFailureException().getMessage());

                return null;
            }

            result = values.getFirst();
        } catch (IOException e) {
            logger.fatal(new JsonToGitHubPullRequestsFailureException(e.getMessage()).getMessage());
        }

        return result;
    }
}
