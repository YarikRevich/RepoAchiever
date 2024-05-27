package com.repoachiever.service.command.external.topology;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.repoachiever.entity.ConfigEntity;
import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.model.TopologyInfoApplication;
import com.repoachiever.model.TopologyInfoUnit;
import com.repoachiever.model.VersionInfoResult;
import com.repoachiever.service.client.info.topology.TopologyInfoClientService;
import com.repoachiever.service.client.info.version.VersionInfoClientService;
import com.repoachiever.service.command.common.ICommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Represents topology external command service.
 */
@Service
public class TopologyExternalCommandService implements ICommand<ConfigEntity> {
    @Autowired
    private PropertiesEntity properties;

    /**
     * @see ICommand
     */
    public void process(ConfigEntity config) throws ApiServerOperationFailureException {
        VersionInfoClientService versionInfoClientService =
                new VersionInfoClientService(config.getApiServer().getHost());

        VersionInfoResult versionInfoResult = versionInfoClientService.process(null);

        if (!versionInfoResult.getExternalApi().getHash().equals(properties.getGitCommitId())) {
            throw new ApiServerOperationFailureException(new VersionMismatchException().getMessage());
        }

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
    }
}
