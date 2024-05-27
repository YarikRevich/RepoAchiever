package com.repoachiever.resource;

import com.repoachiever.api.StateResourceApi;
import com.repoachiever.model.ContentStateApplication;
import com.repoachiever.model.ContentStateApplicationResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.InternalServerErrorException;

import java.util.Objects;

/** Contains implementation of StateResource. */
@ApplicationScoped
public class StateResource implements StateResourceApi {
    /**
     * Implementation for declared in OpenAPI configuration v1StateContentPost method.
     *
     * @param contentStateApplication application used to perform content state retrieval.
     * @return retrieved state content hash.
     */
    @Override
    public ContentStateApplicationResult v1StateContentPost(ContentStateApplication contentStateApplication) {
        throw new InternalServerErrorException();
    }
}
