package com.repoachiever.terraform.provider.aws;

import com.repoachiever.converter.AgentContextToJsonConverter;
import com.repoachiever.converter.DeploymentRequestsToAgentContextConverter;
import com.repoachiever.dto.CommandExecutorOutputDto;
import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.entity.VariableFileEntity;
import com.repoachiever.exception.CommandExecutorException;
import com.repoachiever.model.TerraformDeploymentApplication;
import com.repoachiever.model.TerraformDestructionApplication;
import com.repoachiever.service.terraform.provider.ITerraformProvider;
import com.repoachiever.service.terraform.provider.aws.command.ApplyCommandService;
import com.repoachiever.service.terraform.provider.aws.command.DestroyCommandService;
import com.repoachiever.service.terraform.provider.aws.command.InitCommandService;
import com.repoachiever.service.terraform.provider.aws.command.OutputCommandService;
import com.repoachiever.service.terraform.provider.executor.CommandExecutorService;
import com.repoachiever.service.workspace.WorkspaceService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.io.IOException;
import java.util.Objects;

/** Represents Terraform provider implementation for AWS vendor. */
@ApplicationScoped
public class AWSTerraformProviderService implements ITerraformProvider {
  @Inject PropertiesEntity properties;

  @Inject WorkspaceService workspaceService;

  @Inject CommandExecutorService commandExecutorService;

  /**
   * @see ITerraformProvider
   */
  public String apply(TerraformDeploymentApplication terraformDeploymentApplication)
      throws TerraformException {
    String workspaceUnitKey =
        workspaceService.createUnitKey(
            terraformDeploymentApplication.getCredentials().getSecrets().getAccessKey(),
            terraformDeploymentApplication.getCredentials().getSecrets().getSecretKey());

    if (!workspaceService.isUnitDirectoryExist(workspaceUnitKey)) {
      try {
        workspaceService.createUnitDirectory(workspaceUnitKey);
      } catch (IOException e) {
        throw new TerraformException(e.getMessage());
      }
    }

    String workspaceUnitDirectory;

    try {
      workspaceUnitDirectory = workspaceService.getUnitDirectory(workspaceUnitKey);
    } catch (WorkspaceUnitDirectoryNotFoundException e) {
      throw new TerraformException(e.getMessage());
    }

    InitCommandService initCommandService =
        new InitCommandService(
            workspaceUnitDirectory,
            terraformDeploymentApplication.getCredentials(),
            properties.getTerraformDirectory());

    CommandExecutorOutputDto initCommandOutput;

    try {
      initCommandOutput = commandExecutorService.executeCommand(initCommandService);
    } catch (CommandExecutorException e) {
      throw new TerraformException(e.getMessage());
    }

    String initCommandErrorOutput = initCommandOutput.getErrorOutput();

    if (Objects.nonNull(initCommandErrorOutput) && !initCommandErrorOutput.isEmpty()) {
      throw new TerraformException(initCommandErrorOutput);
    }

    String agentContext =
        AgentContextToJsonConverter.convert(
            DeploymentRequestsToAgentContextConverter.convert(
                terraformDeploymentApplication.getRequests()));

    try {
      workspaceService.createVariableFile(
          workspaceUnitDirectory, VariableFileEntity.of(agentContext, properties.getGitCommitId()));
    } catch (IOException e) {
      throw new TerraformException(e.getMessage());
    }

    ApplyCommandService applyCommandService =
        new ApplyCommandService(
            agentContext,
            workspaceUnitDirectory,
            terraformDeploymentApplication.getCredentials(),
            properties.getTerraformDirectory(),
            properties.getGitCommitId());

    CommandExecutorOutputDto applyCommandOutput;

    try {
      applyCommandOutput = commandExecutorService.executeCommand(applyCommandService);
    } catch (CommandExecutorException e) {
      throw new TerraformException(e.getMessage());
    }

    String applyCommandErrorOutput = applyCommandOutput.getErrorOutput();

    if (Objects.nonNull(applyCommandErrorOutput) && !applyCommandErrorOutput.isEmpty()) {
      throw new TerraformException(applyCommandErrorOutput);
    }

    OutputCommandService outputCommandService =
        new OutputCommandService(
            workspaceUnitDirectory,
            terraformDeploymentApplication.getCredentials(),
            properties.getTerraformDirectory());

    CommandExecutorOutputDto outputCommandOutput;

    try {
      outputCommandOutput = commandExecutorService.executeCommand(outputCommandService);
    } catch (CommandExecutorException e) {
      throw new TerraformException(e.getMessage());
    }

    String outputCommandErrorOutput = outputCommandOutput.getErrorOutput();

    if (Objects.nonNull(outputCommandErrorOutput) && !outputCommandErrorOutput.isEmpty()) {
      throw new TerraformException(outputCommandErrorOutput);
    }

    return outputCommandOutput.getNormalOutput();
  }

  /**
   * @see ITerraformProvider
   */
  public void destroy(TerraformDestructionApplication terraformDestructionApplication)
      throws TerraformException {
    String workspaceUnitKey =
        workspaceService.createUnitKey(
            terraformDestructionApplication.getCredentials().getSecrets().getAccessKey(),
            terraformDestructionApplication.getCredentials().getSecrets().getSecretKey());

    if (!workspaceService.isUnitDirectoryExist(workspaceUnitKey)) {
      throw new TerraformException(new WorkspaceUnitDirectoryNotFoundException().getMessage());
    }

    String workspaceUnitDirectory;

    try {
      workspaceUnitDirectory = workspaceService.getUnitDirectory(workspaceUnitKey);
    } catch (WorkspaceUnitDirectoryNotFoundException e) {
      throw new TerraformException(e.getMessage());
    }

    if (!workspaceService.isVariableFileExist(workspaceUnitDirectory)) {
      throw new TerraformException(new WorkspaceUnitVariableFileNotFoundException().getMessage());
    }

    VariableFileEntity variableFile;
    try {
      variableFile = workspaceService.getVariableFileContent(workspaceUnitDirectory);
    } catch (VariableFileNotFoundException e) {
      throw new RuntimeException(e);
    }

    DestroyCommandService destroyCommandService =
        new DestroyCommandService(
            variableFile.getAgentContext(),
            workspaceUnitDirectory,
            terraformDestructionApplication.getCredentials(),
            properties.getTerraformDirectory(),
            variableFile.getAgentVersion());

    CommandExecutorOutputDto destroyCommandOutput;

    try {
      destroyCommandOutput = commandExecutorService.executeCommand(destroyCommandService);
    } catch (CommandExecutorException e) {
      throw new TerraformException(e.getMessage());
    }

    String destroyCommandErrorOutput = destroyCommandOutput.getErrorOutput();

    if (Objects.nonNull(destroyCommandErrorOutput) && !destroyCommandErrorOutput.isEmpty()) {
      throw new TerraformException(destroyCommandErrorOutput);
    }

    try {
      workspaceService.removeUnitDirectory(workspaceUnitKey);
    } catch (IOException e) {
      throw new TerraformException(e.getMessage());
    }
  }
}
