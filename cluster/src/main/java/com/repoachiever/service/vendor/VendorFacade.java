package com.repoachiever.service.vendor;

import com.repoachiever.dto.GitHubLocationNotationDto;
import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.exception.GitHubContentRetrievalFailureException;
import com.repoachiever.exception.LocationDefinitionsAreNotValidException;
import com.repoachiever.service.config.ConfigService;
import com.repoachiever.service.vendor.common.VendorConfigurationHelper;
import com.repoachiever.service.vendor.git.github.GitGitHubVendorService;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;

/**
 * Provides high-level access to VCS vendor operations.
 */
@Service
public class VendorFacade {
    @Autowired
    private PropertiesEntity properties;

    @Autowired
    private ConfigService configService;

    @Autowired
    private VendorConfigurationHelper vendorConfigurationHelper;

    @Autowired
    private GitGitHubVendorService gitGitHubVendorService;

    /**
     * Checks if provided vendor provider is available.
     *
     * @return result of the check.
     */
    public Boolean isVendorAvailable() {
        return switch (configService.getConfig().getService().getProvider()) {
            case EXPORTER -> null;
            case GIT_GITHUB -> vendorConfigurationHelper.isVendorAvailable(properties.getRestClientGitHubUrl());
        };
    }

    /**
     * Retrieves latest record for the repository with the given raw location definition.
     *
     * @param location given raw location definition.
     * @return retrieved latest record for repository with the given raw location definition.
     * @throws LocationDefinitionsAreNotValidException if given location definitions are not valid.
     * @throws GitHubContentRetrievalFailureException  if GitHub client content retrieval fails.
     */
    public String getLatestRecord(String location) throws
            LocationDefinitionsAreNotValidException,
            GitHubContentRetrievalFailureException {
        return switch (configService.getConfig().getService().getProvider()) {
            case EXPORTER -> null;
            case GIT_GITHUB -> {
                GitHubLocationNotationDto locationGitHubNotation =
                        vendorConfigurationHelper.parseLocationGitHubNotation(location);

                if (locationGitHubNotation.getBranch().isEmpty()) {
                    locationGitHubNotation.setBranch(
                            Optional.of(
                                    gitGitHubVendorService.getDefaultBranch(
                                            locationGitHubNotation.getOwner(),
                                            locationGitHubNotation.getName())));
                }

                yield gitGitHubVendorService.getLatestCommitHash(
                        locationGitHubNotation.getOwner(),
                        locationGitHubNotation.getName(),
                        locationGitHubNotation.getBranch().get());
            }
        };
    }

    /**
     * Retrieves amount of records for the repository with the given raw location definition.
     *
     * @param location given raw location definition.
     * @return retrieved amount of repository records with the given raw location definition.
     * @throws LocationDefinitionsAreNotValidException if given location definitions are not valid.
     * @throws GitHubContentRetrievalFailureException  if GitHub client content retrieval fails.
     */
    public Integer getRecordAmount(String location) throws
            LocationDefinitionsAreNotValidException,
            GitHubContentRetrievalFailureException {
        return switch (configService.getConfig().getService().getProvider()) {
            case EXPORTER -> 0;
            case GIT_GITHUB -> {
                GitHubLocationNotationDto locationGitHubNotation =
                        vendorConfigurationHelper.parseLocationGitHubNotation(location);

                if (locationGitHubNotation.getBranch().isEmpty()) {
                    locationGitHubNotation.setBranch(
                            Optional.of(
                                    gitGitHubVendorService.getDefaultBranch(
                                            locationGitHubNotation.getOwner(),
                                            locationGitHubNotation.getName())));
                }

                yield gitGitHubVendorService.getCommitAmount(
                        locationGitHubNotation.getOwner(),
                        locationGitHubNotation.getName(),
                        locationGitHubNotation.getBranch().get());
            }
        };
    }

    /**
     * Retrieves content from the repository with the given raw location definition and given record identification.
     *
     * @param location given raw location definition.
     * @param record   given record identification.
     * @return retrieved content from the repository with the given raw location definition and given record
     * identification.
     * @throws LocationDefinitionsAreNotValidException if given location definitions are not valid.
     * @throws GitHubContentRetrievalFailureException  if GitHub client content retrieval fails.
     */
    public InputStream getRecordContent(String location, String record) throws
            LocationDefinitionsAreNotValidException,
            GitHubContentRetrievalFailureException {
        return switch (configService.getConfig().getService().getProvider()) {
            case EXPORTER -> null;
            case GIT_GITHUB -> {
                GitHubLocationNotationDto locationGitHubNotation =
                        vendorConfigurationHelper.parseLocationGitHubNotation(location);

                yield switch (configService.getConfig().getContent().getFormat()) {
                    case ZIP -> gitGitHubVendorService.getCommitContentAsZip(
                            locationGitHubNotation.getOwner(),
                            locationGitHubNotation.getName(),
                            record);
                    case TAR -> gitGitHubVendorService.getCommitContentAsTar(
                            locationGitHubNotation.getOwner(),
                            locationGitHubNotation.getName(),
                            record);
                };
            }
        };
    }
}
