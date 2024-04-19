package com.repoachiever.mapping;

import com.repoachiever.exception.WorkspaceUnitInternalConfigFileNotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/** Represents mapper for WorkspaceUnitInternalConfigFileNotFoundException exception. */
@Provider
public class WorkspaceUnitInternalConfigFileNotFoundExceptionMapper
    implements ExceptionMapper<WorkspaceUnitInternalConfigFileNotFoundException> {
  @Override
  public Response toResponse(WorkspaceUnitInternalConfigFileNotFoundException e) {
    return Response.status(Response.Status.BAD_REQUEST.getStatusCode())
        .entity(e.getMessage())
        .build();
  }
}
