package com.repoachiever.service.command.config.open;

import process.SProcess;
import process.SProcessExecutor;

/** Represents command, which is responsible for a startup of the configuration file editor. */
public class OpenConfigCommandService extends SProcess {
  private final String command;

  private final SProcessExecutor.OS osType;

  public OpenConfigCommandService(String configLocation) {
    this.osType = SProcessExecutor.getCommandExecutor().getOSType();

    this.command =
        switch (osType) {
          case MAC -> String.format("open -eW %s", configLocation);
          case WINDOWS, UNIX, ANY -> null;
        };
  }

  @Override
  public String getCommand() {
    return command;
  }

  @Override
  public SProcessExecutor.OS getOSType() {
    return osType;
  }
}
