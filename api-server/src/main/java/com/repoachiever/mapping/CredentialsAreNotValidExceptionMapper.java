package com.repoachiever.mapping;

import com.repoachiever.exception.CredentialsAreNotValidException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/** Represents mapper for CredentialsAreNotValidException exception. */
@Provider
public class CredentialsAreNotValidExceptionMapper
        implements ExceptionMapper<CredentialsAreNotValidException> {
    @Override
    public Response toResponse(CredentialsAreNotValidException e) {
        return Response.status(Response.Status.BAD_REQUEST.getStatusCode())
                .entity(e.getMessage())
                .build();
    }
}
