package com.repoachiever.service.command.common;

import com.repoachiever.exception.ApiServerException;

/** Represents common command interface. */
public interface ICommand {
  /** Processes certain request for an external command. */
  void process() throws ApiServerException;
}
