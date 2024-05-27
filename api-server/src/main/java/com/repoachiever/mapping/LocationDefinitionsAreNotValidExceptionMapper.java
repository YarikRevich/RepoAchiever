package com.repoachiever.mapping;

import com.repoachiever.exception.LocationDefinitionsAreNotValidException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/** Represents mapper for LocationDefinitionsAreNotValidException exception. */
@Provider
public class LocationDefinitionsAreNotValidExceptionMapper
        implements ExceptionMapper<LocationDefinitionsAreNotValidException> {
    @Override
    public Response toResponse(LocationDefinitionsAreNotValidException e) {
        return Response.status(Response.Status.BAD_REQUEST.getStatusCode())
                .entity(e.getMessage())
                .build();
    }
}
