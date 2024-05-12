package com.repoachiever.service.hand.config.command;

import java.nio.file.Paths;
import process.SProcess;
import process.SProcessExecutor;

/** Represents command, which is responsible for a startup of the given swap file editor. */
public class OpenSwapFileEditorCommandService extends SProcess {
  private final String command;
  private final SProcessExecutor.OS osType;

  public OpenSwapFileEditorCommandService(String swapFilePath) {
    this.osType = SProcessExecutor.getCommandExecutor().getOSType();

    this.command =
        switch (osType) {
          case MAC -> String.format("open -W -a TextEdit %s", Paths.get(swapFilePath));
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
