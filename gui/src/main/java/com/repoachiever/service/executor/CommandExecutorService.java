package com.repoachiever.service.executor;

import com.repoachiever.dto.CommandExecutorOutputDto;
import com.repoachiever.exception.CommandExecutorException;
import java.io.IOException;
import org.springframework.stereotype.Service;
import process.SProcess;
import process.SProcessExecutor;
import process.exceptions.NonMatchingOSException;
import process.exceptions.SProcessNotYetStartedException;

/**
 * Represents command executor service to execute commands used for provider deployment operations.
 */
@Service
public class CommandExecutorService {
  private final SProcessExecutor processExecutor;

  CommandExecutorService() {
    this.processExecutor = SProcessExecutor.getCommandExecutor();
  }

  /**
   * Returns type of the currently used operating system.
   *
   * @return retrieved current operating system type.
   */
  public SProcessExecutor.OS getOSType() {
    return processExecutor.getOSType();
  }

  /**
   * Executes given command.
   *
   * @param command standalone command
   * @return output result, which consists of stdout and stderr.
   * @throws CommandExecutorException when any execution step failed.
   */
  public CommandExecutorOutputDto executeCommand(SProcess command) throws CommandExecutorException {
    try {
      processExecutor.executeCommand(command);
    } catch (IOException | NonMatchingOSException e) {
      throw new CommandExecutorException(e.getMessage());
    }

    try {
      command.waitForCompletion();
    } catch (SProcessNotYetStartedException | InterruptedException e) {
      throw new CommandExecutorException(e.getMessage());
    }

    String commandErrorOutput;

    try {
      commandErrorOutput = command.getErrorOutput();
    } catch (SProcessNotYetStartedException e) {
      throw new CommandExecutorException(e.getMessage());
    }

    String commandNormalOutput;

    try {
      commandNormalOutput = command.getNormalOutput();
    } catch (SProcessNotYetStartedException e) {
      throw new CommandExecutorException(e.getMessage());
    }

    return CommandExecutorOutputDto.of(commandNormalOutput, commandErrorOutput);
  }
}
