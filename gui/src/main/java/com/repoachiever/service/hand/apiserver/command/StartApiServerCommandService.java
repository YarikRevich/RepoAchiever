package com.repoachiever.service.hand.apiserver.command;

import com.repoachiever.entity.PropertiesEntity;
import process.SProcess;
import process.SProcessExecutor;
import process.SProcessExecutor.OS;

/**
 * Represents command, which is responsible for a startup of the API Server in the background as
 * system service.
 */
public class StartApiServerCommandService extends SProcess {
  private final String command;
  private final OS osType;

  public StartApiServerCommandService(PropertiesEntity properties) {
    this.osType = SProcessExecutor.getCommandExecutor().getOSType();

    this.command =
        switch (osType) {
          case WINDOWS -> null;
          case UNIX, MAC, ANY -> String.format(
              "java -jar %s &", properties.getApiServerDirectory());
        };
  }

  @Override
  public String getCommand() {
    return command;
  }

  @Override
  public OS getOSType() {
    return osType;
  }
}
