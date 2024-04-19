package com.repoachiever.service.command.external.stop;

import com.repoachiever.exception.ApiServerException;
import com.repoachiever.model.*;
import com.repoachiever.service.command.common.ICommand;
import com.repoachiever.service.command.external.stop.provider.aws.AWSStopExternalCommandService;
import com.repoachiever.service.config.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Represents stop external command service. */
@Service
public class StopExternalCommandService implements ICommand {
  @Autowired private ConfigService configService;

  @Autowired private AWSStopExternalCommandService stopExternalCommandService;

  /**
   * @see ICommand
   */
  public void process() throws ApiServerException {
    switch (configService.getConfig().getCloud().getProvider()) {
      case AWS -> stopExternalCommandService.process();
    }
  }
}
