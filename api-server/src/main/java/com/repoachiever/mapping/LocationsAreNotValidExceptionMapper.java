package com.repoachiever.mapping;

import com.repoachiever.exception.CredentialsAreNotValidException;
import com.repoachiever.exception.LocationsAreNotValidException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/** Represents mapper for LocationsAreNotValidException exception. */
@Provider
public class LocationsAreNotValidExceptionMapper
        implements ExceptionMapper<LocationsAreNotValidException> {
    @Override
    public Response toResponse(LocationsAreNotValidException e) {
        return Response.status(Response.Status.BAD_REQUEST.getStatusCode())
                .entity(e.getMessage())
                .build();
    }
}
