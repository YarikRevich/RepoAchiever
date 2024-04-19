package com.repoachiever.service.command.external.version;

import com.repoachiever.dto.VersionExternalCommandResultDto;
import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.exception.ApiServerException;
import com.repoachiever.model.ApplicationInfoResult;
import com.repoachiever.service.client.command.VersionClientCommandService;
import com.repoachiever.service.command.common.ICommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Represents version external command service. */
@Service
public class VersionExternalCommandService implements ICommand<VersionExternalCommandResultDto> {
  @Autowired private PropertiesEntity properties;

  @Autowired private VersionClientCommandService versionClientCommandService;

  /**
   * @see ICommand
   */
  public VersionExternalCommandResultDto process() {
    ApplicationInfoResult applicationInfoResult;
    try {
      applicationInfoResult = versionClientCommandService.process(null);
    } catch (ApiServerException e) {
      return VersionExternalCommandResultDto.of(null, false, e.getMessage());
    }

    return VersionExternalCommandResultDto.of(
        applicationInfoResult.getExternalApi().getHash(), true, null);
  }
}
