package com.repoachiever.mapping;

import com.repoachiever.exception.InternalConfigNotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/** Represents mapper for InternalConfigNotFoundException exception. */
@Provider
public class InternalConfigNotFoundExceptionMapper
    implements ExceptionMapper<InternalConfigNotFoundException> {
  @Override
  public Response toResponse(InternalConfigNotFoundException e) {
    return Response.status(Response.Status.BAD_REQUEST.getStatusCode())
        .entity(e.getMessage())
        .build();
  }
}
