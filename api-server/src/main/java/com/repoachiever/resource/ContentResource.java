package com.repoachiever.resource;

import com.repoachiever.api.ContentResourceApi;
import com.repoachiever.exception.CredentialsAreNotValidException;
import com.repoachiever.exception.CredentialsFieldIsNotValidException;
import com.repoachiever.model.*;
import com.repoachiever.repository.facade.RepositoryFacade;
import com.repoachiever.resource.common.ResourceConfigurationHelper;
import com.repoachiever.service.cluster.facade.ClusterFacade;
import com.repoachiever.service.vendor.VendorFacade;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import lombok.SneakyThrows;

import java.io.File;
import java.util.Objects;

/** Contains implementation of ContentResource. */
@ApplicationScoped
public class ContentResource implements ContentResourceApi {
    @Inject
    RepositoryFacade repositoryFacade;

    @Inject
    ClusterFacade clusterFacade;

    @Inject
    VendorFacade vendorFacade;

    /**
     * Implementation for declared in OpenAPI configuration v1ContentPost method.
     *
     * @param contentRetrievalApplication content retrieval application.
     * @return retrieved content result.
     */
    @Override
    public ContentRetrievalResult v1ContentPost(ContentRetrievalApplication contentRetrievalApplication) {
        if (Objects.isNull(contentRetrievalApplication)) {
            throw new BadRequestException();
        }

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
        if (Objects.isNull(contentApplication)) {
            throw new BadRequestException();
        }

        if (!ResourceConfigurationHelper.isExternalCredentialsFieldValid(
                contentApplication.getProvider(), contentApplication.getCredentials().getExternal())) {
            throw new CredentialsFieldIsNotValidException();
        }

        if (!vendorFacade.isExternalCredentialsValid(
                contentApplication.getProvider(), contentApplication.getCredentials().getExternal())) {
            throw new CredentialsAreNotValidException();
        }

        clusterFacade.apply(contentApplication);

        repositoryFacade.apply(contentApplication);
    }

    /**
     * Implementation for declared in OpenAPI configuration v1ContentWithdrawDelete method.
     *
     * @param contentWithdrawal content withdrawal application.
     */
    @Override
    @SneakyThrows
    public void v1ContentWithdrawDelete(ContentWithdrawal contentWithdrawal) {
        if (Objects.isNull(contentWithdrawal)) {
            throw new BadRequestException();
        }

        if (!ResourceConfigurationHelper.isExternalCredentialsFieldValid(
                contentWithdrawal.getProvider(), contentWithdrawal.getCredentials().getExternal())) {
            throw new CredentialsFieldIsNotValidException();
        }

        if (!vendorFacade.isExternalCredentialsValid(
                contentWithdrawal.getProvider(), contentWithdrawal.getCredentials().getExternal())) {
            throw new CredentialsAreNotValidException();
        }

        clusterFacade.destroy(contentWithdrawal);

        repositoryFacade.destroy(contentWithdrawal);
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

    /**
     * Implementation for declared in OpenAPI configuration v1ContentCleanPost method.
     *
     * @param contentCleanup content cleanup application.
     */
    @Override
    public void v1ContentCleanPost(ContentCleanup contentCleanup) {

    }
}
