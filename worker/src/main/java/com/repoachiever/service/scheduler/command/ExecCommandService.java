package com.repoachiever.service.scheduler.command;

import process.SProcess;
import process.SProcessExecutor;

/** */
public class ExecCommandService extends SProcess {
  private final String command;
  private final SProcessExecutor.OS osType;

  public ExecCommandService(String input) {
    this.osType = SProcessExecutor.getCommandExecutor().getOSType();

    this.command =
        switch (osType) {
          case WINDOWS -> null;
          case UNIX, MAC, ANY -> input;
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
