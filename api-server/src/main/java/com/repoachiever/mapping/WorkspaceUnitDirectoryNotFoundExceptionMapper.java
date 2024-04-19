package com.repoachiever.mapping;

import com.repoachiever.exception.WorkspaceUnitDirectoryNotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/** Represents mapper for WorkspaceUnitDirectoryNotFoundException exception. */
@Provider
public class WorkspaceUnitDirectoryNotFoundExceptionMapper
    implements ExceptionMapper<WorkspaceUnitDirectoryNotFoundException> {
  @Override
  public Response toResponse(WorkspaceUnitDirectoryNotFoundException e) {
    return Response.status(Response.Status.BAD_REQUEST.getStatusCode())
        .entity(e.getMessage())
        .build();
  }
}
