package com.repoachiever.mapping;

import com.repoachiever.exception.ClusterApplicationFailureException;
import com.repoachiever.exception.RepositoryApplicationFailureException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * Represents mapper for RepositoryApplicationFailureException exception.
 */
@Provider
public class RepositoryApplicationFailureExceptionMapper
        implements ExceptionMapper<RepositoryApplicationFailureException> {
    @Override
    public Response toResponse(RepositoryApplicationFailureException e) {
        return Response.status(Response.Status.BAD_REQUEST.getStatusCode())
                .entity(e.getMessage())
                .build();
    }
}
