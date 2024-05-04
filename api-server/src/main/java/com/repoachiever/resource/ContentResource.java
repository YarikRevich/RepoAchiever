package com.repoachiever.resource;

import com.repoachiever.api.ContentResourceApi;
import com.repoachiever.model.ContentApplication;
import com.repoachiever.model.ContentRetrievalApplication;
import com.repoachiever.model.ContentRetrievalResult;
import com.repoachiever.repository.facade.RepositoryFacade;
import com.repoachiever.service.cluster.facade.ClusterFacade;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.SneakyThrows;

import java.io.File;

/** Contains implementation of ContentResource. */
@ApplicationScoped
public class ContentResource implements ContentResourceApi {
    @Inject
    RepositoryFacade repositoryFacade;

    @Inject
    ClusterFacade clusterFacade;

    /**
     * Implementation for declared in OpenAPI configuration v1ContentPost method.
     *
     * @param contentRetrievalApplication content retrieval application.
     * @return retrieved content result.
     */
    @Override
    public ContentRetrievalResult v1ContentPost(ContentRetrievalApplication contentRetrievalApplication) {
        return ContentRetrievalResult.of(
                repositoryFacade.retrieveLocations(contentRetrievalApplication));
    }

    /**
     * Implementation for declared in OpenAPI configuration v1ContentApplyPost method.
     *
     * @param contentApplication content configuration application.
     */
    @Override
    @SneakyThrows
    public void v1ContentApplyPost(ContentApplication contentApplication) {
        repositoryFacade.apply(contentApplication);

        clusterFacade.apply(contentApplication);
    }

    /**
     * Implementation for declared in OpenAPI configuration v1ContentDownloadGet method.
     *
     * @param location name of content location to be downloaded.
     * @return downloaded content result.
     */
    @Override
    public File v1ContentDownloadGet(String location) {
        return null;
    }
}
