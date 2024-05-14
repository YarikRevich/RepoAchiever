package com.repoachiever.mapping;

import com.repoachiever.exception.ExporterFieldIsNotValidException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/** Represents mapper for ExporterFieldIsNotValidException exception. */
@Provider
public class ExporterFieldIsNotValidExceptionMapper
        implements ExceptionMapper<ExporterFieldIsNotValidException> {
    @Override
    public Response toResponse(ExporterFieldIsNotValidException e) {
        return Response.status(Response.Status.BAD_REQUEST.getStatusCode())
                .entity(e.getMessage())
                .build();
    }
}