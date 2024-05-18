package com.repoachiever.service.client.common;

import com.repoachiever.exception.ApiServerException;

/**
 * Represents external resource command interface.
 *
 * @param <T> type of the command response.
 * @param <K> type of the command request.
 */
public interface IClient<T, K> {
  /**
   * Processes certain request for an external command.
   *
   * @param input input to be given as request body.
   * @return command response.
   */
  T process(K input) throws ApiServerException;
}
