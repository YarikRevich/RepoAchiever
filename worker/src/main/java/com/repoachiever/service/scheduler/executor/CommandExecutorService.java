package com.repoachiever.service.scheduler.executor;

import com.repoachiever.dto.CommandExecutorOutputDto;
import com.repoachiever.exception.CommandExecutorException;
import java.io.IOException;
import org.springframework.stereotype.Service;
import process.SProcess;
import process.SProcessExecutor;
import process.exceptions.NonMatchingOSException;
import process.exceptions.SProcessNotYetStartedException;

/** CommandExecutorService provides command execution service. */
@Service
public class CommandExecutorService {
  private final SProcessExecutor processExecutor;

  CommandExecutorService() {
    this.processExecutor = SProcessExecutor.getCommandExecutor();
  }

  /**
   * Executes given command and gathers its output.
   *
   * @param command command to be executed
   * @return CommandExecutorOutputEntity output, which consists of both stdout and stderr
   * @throws CommandExecutorException when command execution fails or output is not gathered
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
