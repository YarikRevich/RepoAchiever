package com.repoachiever.service.command.swap.open;

import process.SProcess;
import process.SProcessExecutor;

/** Represents command, which is responsible for a startup of the given swap file editor. */
public class OpenSwapCommandService extends SProcess {
  private final String command;

  private final SProcessExecutor.OS osType;

  public OpenSwapCommandService(String swapLocation) {
    this.osType = SProcessExecutor.getCommandExecutor().getOSType();

    this.command =
        switch (osType) {
          case MAC -> String.format("open -W -a TextEdit %s", swapLocation);
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
