package com.repoachiever.service.command.external.start;

import com.repoachiever.dto.StartExternalCommandResultDto;
import com.repoachiever.service.command.common.ICommand;
import com.repoachiever.service.command.external.start.provider.aws.AWSStartExternalCommandService;
import com.repoachiever.service.config.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Represents start external command service. */
@Service
public class StartExternalCommandService implements ICommand<StartExternalCommandResultDto> {
  @Autowired private ConfigService configService;

  @Autowired private AWSStartExternalCommandService awsStartExternalCommandService;

  /**
   * @see ICommand
   */
  public StartExternalCommandResultDto process() {
    return switch (configService.getConfig().getCloud().getProvider()) {
      case AWS -> awsStartExternalCommandService.process();
    };
  }
}
