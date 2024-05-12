package com.repoachiever.mapping;

import com.repoachiever.exception.LocationsFieldIsNotValidException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/** Represents mapper for LocationsFieldIsNotValidException exception. */
@Provider
public class LocationsFieldIsNotValidExceptionMapper
        implements ExceptionMapper<LocationsFieldIsNotValidException> {
    @Override
    public Response toResponse(LocationsFieldIsNotValidException e) {
        return Response.status(Response.Status.BAD_REQUEST.getStatusCode())
                .entity(e.getMessage())
                .build();
    }
}
