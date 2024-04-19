package com.repoachiever.service.command.external.start;

import com.repoachiever.exception.ApiServerException;
import com.repoachiever.model.*;
import com.repoachiever.service.command.common.ICommand;
import com.repoachiever.service.command.external.start.provider.aws.AWSStartExternalCommandService;
import com.repoachiever.service.config.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Represents start external command service. */
@Service
public class StartExternalCommandService implements ICommand {
  @Autowired private ConfigService configService;

  @Autowired private AWSStartExternalCommandService awsStartExternalCommandService;

  /**
   * @see ICommand
   */
  public void process() throws ApiServerException {
    switch (configService.getConfig().getCloud().getProvider()) {
      case AWS -> awsStartExternalCommandService.process();
    }
  }
}
