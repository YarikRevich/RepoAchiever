package com.repoachiever.service.hand.external.topology;

import com.repoachiever.converter.ConfigCredentialsToContentCredentialsConverter;
import com.repoachiever.converter.ConfigProviderToContentProviderConverter;
import com.repoachiever.entity.ConfigEntity;
import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.exception.ApiServerOperationFailureException;
import com.repoachiever.model.TopologyInfoApplication;
import com.repoachiever.model.TopologyInfoUnit;
import com.repoachiever.service.client.info.topology.TopologyInfoClientService;
import com.repoachiever.service.hand.common.ICommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Represents topology external command service.
 */
@Service
public class TopologyExternalCommandService implements ICommand<List<TopologyInfoUnit>, ConfigEntity> {
    @Autowired
    private PropertiesEntity properties;

    /**
     * @see ICommand
     */
    public List<TopologyInfoUnit> process(ConfigEntity config) throws ApiServerOperationFailureException {
        TopologyInfoClientService topologyInfoClientService =
                new TopologyInfoClientService(config.getApiServer().getHost());

        TopologyInfoApplication request = TopologyInfoApplication.of(
                ConfigProviderToContentProviderConverter.convert(
                        config.getService().getProvider()),
                ConfigCredentialsToContentCredentialsConverter.convert(
                        config.getService().getProvider(),
                        config.getService().getCredentials()));

        return topologyInfoClientService.process(request);
    }
}
