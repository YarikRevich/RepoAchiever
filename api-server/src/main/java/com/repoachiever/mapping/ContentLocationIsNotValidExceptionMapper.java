package com.repoachiever.mapping;

import com.repoachiever.exception.ContentLocationIsNotValidException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/** Represents mapper for ContentLocationIsNotValidException exception. */
@Provider
public class ContentLocationIsNotValidExceptionMapper
        implements ExceptionMapper<ContentLocationIsNotValidException> {
    @Override
    public Response toResponse(ContentLocationIsNotValidException e) {
        return Response.status(Response.Status.BAD_REQUEST.getStatusCode())
                .entity(e.getMessage())
                .build();
    }
}
