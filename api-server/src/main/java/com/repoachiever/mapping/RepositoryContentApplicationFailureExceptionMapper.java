package com.repoachiever.mapping;

import com.repoachiever.exception.RepositoryContentApplicationFailureException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * Represents mapper for RepositoryContentApplicationFailureExceptionMapper exception.
 */
@Provider
public class RepositoryContentApplicationFailureExceptionMapper
        implements ExceptionMapper<RepositoryContentApplicationFailureException> {
    @Override
    public Response toResponse(RepositoryContentApplicationFailureException e) {
        return Response.status(Response.Status.BAD_REQUEST.getStatusCode())
                .entity(e.getMessage())
                .build();
    }
}
