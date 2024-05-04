package com.repoachiever.mapping;

import com.repoachiever.exception.ClusterApplicationFailureException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * Represents mapper for ClusterApplicationFailureException exception.
 */
@Provider
public class ClusterApplicationFailureExceptionMapper
        implements ExceptionMapper<ClusterApplicationFailureException> {
    @Override
    public Response toResponse(ClusterApplicationFailureException e) {
        return Response.status(Response.Status.BAD_REQUEST.getStatusCode())
                .entity(e.getMessage())
                .build();
    }
}
