package com.repoachiever.entity;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.apache.commons.lang3.StringUtils;

/**
 * Exposes access to properties setup to be used for further configuration.
 */
@Getter
@Configuration
public class PropertiesEntity {
    private static final String GIT_CONFIG_PROPERTIES_FILE = "git.properties";

    @Value(value = "${REPOACHIEVER_CLUSTER_CONTEXT:null}")
    private String clusterContext;

    @Value(value = "${logging.transfer.frequency}")
    private Integer loggingTransferFrequency;

    @Value(value = "${logging.transferable.prefix}")
    private String loggingTransferablePrefix;

    @Value(value = "${logging.state.frequency}")
    private Integer loggingStateFrequency;

    @Value(value = "${logging.state-finalizer.frequency}")
    private Integer loggingStateFinalizerFrequency;

    @Value(value = "${git.commit.id.abbrev}")
    private String gitCommitId;

    /**
     * Adds custom properties to resource configurations.
     *
     * @return modified property sources configurer.
     */
    @Bean
    private static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        PropertySourcesPlaceholderConfigurer propsConfig = new PropertySourcesPlaceholderConfigurer();
        propsConfig.setLocation(new ClassPathResource(GIT_CONFIG_PROPERTIES_FILE));
        propsConfig.setIgnoreResourceNotFound(true);
        propsConfig.setIgnoreUnresolvablePlaceholders(true);
        return propsConfig;
    }

    /**
     * Removes the last symbol in git commit id of the repository.
     *
     * @return chopped repository git commit id.
     */
    public String getGitCommitId() {
        return StringUtils.chop(gitCommitId);
    }
}
