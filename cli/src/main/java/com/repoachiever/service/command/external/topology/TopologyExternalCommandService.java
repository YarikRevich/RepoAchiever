package com.repoachiever.service.command.external.topology;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.repoachiever.converter.*;
import com.repoachiever.entity.ConfigEntity;
import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.exception.*;
import com.repoachiever.model.TopologyInfoApplication;
import com.repoachiever.model.TopologyInfoUnit;
import com.repoachiever.model.VersionInfoResult;
import com.repoachiever.service.client.info.topology.TopologyInfoClientService;
import com.repoachiever.service.client.info.version.VersionInfoClientService;
import com.repoachiever.service.command.common.ICommand;
import com.repoachiever.service.config.ConfigService;
import com.repoachiever.service.visualization.state.VisualizationState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Represents topology external command service.
 */
@Service
public class TopologyExternalCommandService implements ICommand<ConfigEntity> {
    @Autowired
    private PropertiesEntity properties;

    @Autowired
    private VisualizationState visualizationState;

    /**
     * @see ICommand
     */
    public void process(ConfigEntity config) throws ApiServerOperationFailureException {
        visualizationState.getLabel().pushNext();

        VersionInfoClientService versionInfoClientService =
                new VersionInfoClientService(config.getApiServer().getHost());

        VersionInfoResult versionInfoResult = versionInfoClientService.process(null);

        if (!versionInfoResult.getExternalApi().getHash().equals(properties.getGitCommitId())) {
            throw new ApiServerOperationFailureException(new VersionMismatchException().getMessage());
        }

        visualizationState.getLabel().pushNext();

        visualizationState.getLabel().pushNext();

        TopologyInfoClientService topologyInfoClientService =
                new TopologyInfoClientService(config.getApiServer().getHost());

        TopologyInfoApplication request = TopologyInfoApplication.of(
                ConfigProviderToContentProviderConverter.convert(
                        config.getService().getProvider()),
                ConfigCredentialsToContentCredentialsConverter.convert(
                        config.getService().getProvider(),
                        config.getService().getCredentials()));

        List<TopologyInfoUnit> topologyInfoResult = topologyInfoClientService.process(request);

        try {
            visualizationState.addResult(OutputToVisualizationConverter.convert(topologyInfoResult));
        } catch (JsonProcessingException e) {
            throw new ApiServerOperationFailureException(e.getMessage());
        }

        visualizationState.getLabel().pushNext();
    }
}
