package com.repoachiever.service.command.external.version;

import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.exception.ApiServerException;
import com.repoachiever.model.ApplicationInfoResult;
import com.repoachiever.service.client.command.HealthCheckClientCommandService;
import com.repoachiever.service.client.command.VersionClientCommandService;
import com.repoachiever.service.command.common.ICommand;
import com.repoachiever.service.visualization.state.VisualizationState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Represents version external command service. */
@Service
public class VersionExternalCommandService implements ICommand {
  @Autowired PropertiesEntity properties;

  @Autowired private VersionClientCommandService versionClientCommandService;

  @Autowired private HealthCheckClientCommandService healthCheckClientCommandService;

  @Autowired private VisualizationState visualizationState;

  /**
   * @see ICommand
   */
  public void process() throws ApiServerException {
    visualizationState.getLabel().pushNext();

    try {
      ApplicationInfoResult applicationInfoResult = versionClientCommandService.process(null);

      visualizationState.addResult(
          String.format(
              "API Server version: %s", applicationInfoResult.getExternalApi().getHash()));
    } finally {
      visualizationState.addResult(
          String.format("Client version: %s", properties.getGitCommitId()));
    }
  }
}
