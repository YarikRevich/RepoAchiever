package com.repoachiever.mapping;

import com.repoachiever.exception.SecretsConversionException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/** Represents mapper for SecretsConversionException exception. */
@Provider
public class SecretsConversionExceptionMapper
    implements ExceptionMapper<SecretsConversionException> {
  @Override
  public Response toResponse(SecretsConversionException e) {
    return Response.status(Response.Status.BAD_REQUEST.getStatusCode())
        .entity(e.getMessage())
        .build();
  }
}
