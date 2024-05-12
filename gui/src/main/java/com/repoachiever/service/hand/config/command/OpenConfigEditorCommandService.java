package com.repoachiever.service.hand.config.command;

import java.nio.file.Paths;
import process.SProcess;
import process.SProcessExecutor;

/** Represents command, which is responsible for a startup of the configuration file editor. */
public class OpenConfigEditorCommandService extends SProcess {
  private final String command;
  private final SProcessExecutor.OS osType;

  public OpenConfigEditorCommandService(String configRootPath, String configUserFilePath) {
    this.osType = SProcessExecutor.getCommandExecutor().getOSType();

    this.command =
        switch (osType) {
          case MAC -> String.format(
              "open -eW %s",
              Paths.get(System.getProperty("user.home"), configRootPath, configUserFilePath));
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
