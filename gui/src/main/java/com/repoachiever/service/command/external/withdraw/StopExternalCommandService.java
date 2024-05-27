package com.repoachiever.service.command.external.withdraw;

import com.repoachiever.dto.StopExternalCommandResultDto;
import com.repoachiever.service.command.common.ICommand;
import com.repoachiever.service.command.external.withdraw.provider.aws.AWSStopExternalCommandService;
import com.repoachiever.service.config.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Represents stop external command service. */
@Service
public class StopExternalCommandService implements ICommand<StopExternalCommandResultDto> {
  @Autowired private ConfigService configService;

  @Autowired private AWSStopExternalCommandService stopExternalCommandService;

  /**
   * @see ICommand
   */
  public StopExternalCommandResultDto process() {
    return switch (configService.getConfig().getCloud().getProvider()) {
      case AWS -> stopExternalCommandService.process();
    };
  }
}
