package com.repoachiever.service.command.common;

import com.repoachiever.exception.ApiServerException;

/** Represents common command interface. */
public interface ICommand<T> {
  /** Processes certain request for an external command. */
  T process() throws ApiServerException;
}
