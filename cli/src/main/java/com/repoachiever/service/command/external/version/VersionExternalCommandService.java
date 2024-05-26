package com.repoachiever.service.command.external.version;

import com.repoachiever.entity.ConfigEntity;
import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.exception.*;
import com.repoachiever.model.VersionInfoResult;
import com.repoachiever.service.client.info.version.VersionInfoClientService;
import com.repoachiever.service.command.common.ICommand;
import com.repoachiever.service.visualization.state.VisualizationState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Represents version external command service.
 */
@Service
public class VersionExternalCommandService implements ICommand<ConfigEntity> {
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

        try {
            VersionInfoResult versionInfoResult = versionInfoClientService.process(null);

            visualizationState.addResult(
                    String.format(
                            "API Server version: %s", versionInfoResult.getExternalApi().getHash()));
        } finally {
            visualizationState.addResult(
                    String.format("Client version: %s", properties.getGitCommitId()));
        }
    }
}
