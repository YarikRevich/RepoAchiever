package com.repoachiever.service.hand.common;

import com.repoachiever.exception.ApiServerOperationFailureException;

/**
 * Represents common command interface.
 *
 * @param <T> type of the command response.
 * @param <K> type of the command request.
 */
public interface ICommand<T, K> {
    /**
     * Processes certain request for an external command.
     *
     * @param input give input.
     * @return command response.
     */
    T process(K input) throws ApiServerOperationFailureException;
}
