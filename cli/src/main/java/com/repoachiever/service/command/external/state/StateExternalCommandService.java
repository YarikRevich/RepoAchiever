package com.repoachiever.service.command.external.state;

import com.repoachiever.exception.ApiServerException;
import com.repoachiever.service.command.common.ICommand;
import com.repoachiever.service.command.external.state.provider.aws.AWSStateExternalCommandService;
import com.repoachiever.service.config.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Represents state external command service. */
@Service
public class StateExternalCommandService implements ICommand {
  @Autowired private ConfigService configService;

  @Autowired private AWSStateExternalCommandService awsStateExternalCommandService;

  /**
   * @see ICommand
   */
  public void process() throws ApiServerException {
    switch (configService.getConfig().getCloud().getProvider()) {
      case AWS -> awsStateExternalCommandService.process();
    }
  }
}
