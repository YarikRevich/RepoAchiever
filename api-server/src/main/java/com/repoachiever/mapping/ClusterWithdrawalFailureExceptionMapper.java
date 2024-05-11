package com.repoachiever.mapping;

import com.repoachiever.exception.ClusterWithdrawalFailureException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * Represents mapper for ClusterWithdrawalFailureExceptionMapper exception.
 */
@Provider
public class ClusterWithdrawalFailureExceptionMapper
        implements ExceptionMapper<ClusterWithdrawalFailureException> {
    @Override
    public Response toResponse(ClusterWithdrawalFailureException e) {
        return Response.status(Response.Status.BAD_REQUEST.getStatusCode())
                .entity(e.getMessage())
                .build();
    }
}
