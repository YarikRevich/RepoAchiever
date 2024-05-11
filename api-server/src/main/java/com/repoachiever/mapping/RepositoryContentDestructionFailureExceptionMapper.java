package com.repoachiever.mapping;

import com.repoachiever.exception.RepositoryContentDestructionFailureException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * Represents mapper for RepositoryContentDestructionFailureExceptionMapper exception.
 */
@Provider
public class RepositoryContentDestructionFailureExceptionMapper
        implements ExceptionMapper<RepositoryContentDestructionFailureException> {
    @Override
    public Response toResponse(RepositoryContentDestructionFailureException e) {
        return Response.status(Response.Status.BAD_REQUEST.getStatusCode())
                .entity(e.getMessage())
                .build();
    }
}
