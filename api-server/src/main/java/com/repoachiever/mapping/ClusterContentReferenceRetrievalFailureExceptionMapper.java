package com.repoachiever.mapping;

import com.repoachiever.exception.ClusterContentReferenceRetrievalFailureException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * Represents mapper for ClusterContentReferenceRetrievalFailureException exception.
 */
@Provider
public class ClusterContentReferenceRetrievalFailureExceptionMapper
        implements ExceptionMapper<ClusterContentReferenceRetrievalFailureException> {
    @Override
    public Response toResponse(ClusterContentReferenceRetrievalFailureException e) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                .entity(e.getMessage())
                .build();
    }
}