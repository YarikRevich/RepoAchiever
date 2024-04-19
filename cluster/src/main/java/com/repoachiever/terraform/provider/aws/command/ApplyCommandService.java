package com.repoachiever.terraform.provider.aws.command;

import com.repoachiever.model.CredentialsFields;
import com.repoachiever.model.Provider;
import com.repoachiever.service.terraform.provider.aws.common.AWSProviderConfigurationHelper;
import process.SProcess;
import process.SProcessExecutor;
import process.SProcessExecutor.OS;

import java.nio.file.Paths;

/** Represents Terraform apply command. */
public class ApplyCommandService extends SProcess {
  private final String command;
  private final OS osType;

  public ApplyCommandService(
      String agentContext,
      String workspaceUnitDirectory,
      CredentialsFields credentials,
      String terraformDirectory,
      String gitCommitId) {
    this.osType = SProcessExecutor.getCommandExecutor().getOSType();

    this.command =
        switch (osType) {
          case WINDOWS -> null;
          case UNIX, MAC, ANY -> String.format(
              "cd %s && %s terraform apply %s -input=false -no-color -auto-approve",
              Paths.get(terraformDirectory, Provider.AWS.toString()),
              AWSProviderConfigurationHelper.getEnvironmentVariables(
                  workspaceUnitDirectory, credentials),
              AWSProviderConfigurationHelper.getVariables(agentContext, gitCommitId));
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
