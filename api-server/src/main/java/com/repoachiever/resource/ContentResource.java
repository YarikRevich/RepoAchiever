package com.repoachiever.resource;

import com.repoachiever.api.ContentResourceApi;
import com.repoachiever.model.ContentResult;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.File;

/** Contains implementation of ContentResource. */
@ApplicationScoped
public class ContentResource implements ContentResourceApi {
    /**
     * Implementation for declared in OpenAPI configuration v1ContentDownloadGet method.
     *
     * @param id the location identificator of the content to be downloaded.
     * @return Downloaded content result.
     */
    @Override
    public File v1ContentDownloadGet(Integer id) {
        return null;
    }

    /**
     * Implementation for declared in OpenAPI configuration v1ContentGet method.
     *
     * @return Retrieved content result.
     */
    @Override
    public ContentResult v1ContentGet() {
        return null;
    }
}
