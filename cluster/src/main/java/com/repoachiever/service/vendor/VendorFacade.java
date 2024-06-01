package com.repoachiever.service.vendor;

import com.repoachiever.converter.JsonToGitHubPullRequestsConverter;
import com.repoachiever.dto.AdditionalContentDto;
import com.repoachiever.dto.GitHubLocationNotationDto;
import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.exception.GitHubContentRetrievalFailureException;
import com.repoachiever.exception.LocationDefinitionsAreNotValidException;
import com.repoachiever.logging.common.LoggingConfigurationHelper;
import com.repoachiever.service.config.ConfigService;
import com.repoachiever.service.vendor.common.VendorConfigurationHelper;
import com.repoachiever.service.vendor.git.github.GitGitHubVendorService;
import lombok.ToString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * Provides high-level access to VCS vendor operations.
 */
@Service
public class VendorFacade {
    private static final Logger logger = LogManager.getLogger(VendorFacade.class);

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
     * Retrieves raw content from the repository with the given raw location definition and given record identification.
     *
     * @param location given raw location definition.
     * @param record   given record identification.
     * @return retrieved raw content from the repository with the given raw location definition and given record
     * identification.
     * @throws LocationDefinitionsAreNotValidException if given location definitions are not valid.
     * @throws GitHubContentRetrievalFailureException  if GitHub client content retrieval fails.
     */
    public DataBuffer getRecordRawContent(String location, String record) throws
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

    /**
     * Retrieves additional content from the repository with the given raw location definition and given record identification.
     *
     * @param location given raw location definition.
     * @return retrieved additional content from the repository with the given raw location definition and given record
     * identification.
     * @throws LocationDefinitionsAreNotValidException if given location definitions are not valid.
     * @throws GitHubContentRetrievalFailureException  if GitHub client content retrieval fails.
     */
    public AdditionalContentDto getAdditionalContent(String location) throws
            LocationDefinitionsAreNotValidException,
            GitHubContentRetrievalFailureException {
        return switch (configService.getConfig().getService().getProvider()) {
            case EXPORTER -> null;
            case GIT_GITHUB -> {
                GitHubLocationNotationDto locationGitHubNotation =
                        vendorConfigurationHelper.parseLocationGitHubNotation(location);

                List<String> hashes = new ArrayList<>();

                Map<String, String> data = new HashMap<>();

                String rawPullRequestsContent = gitGitHubVendorService.getPullRequests(
                        locationGitHubNotation.getOwner(), locationGitHubNotation.getName());

                List<Object> pullRequestsContent =
                        JsonToGitHubPullRequestsConverter.convert(rawPullRequestsContent);

                if (Objects.isNull(pullRequestsContent)) {
                    yield null;
                }

                if (!pullRequestsContent.isEmpty()) {
                    hashes.add(rawPullRequestsContent);

                    data.put(properties.getGitHubAdditionalPullRequestsName(), rawPullRequestsContent);
                }

                if (data.isEmpty()) {
                    yield null;
                }

                yield AdditionalContentDto.of(
                        vendorConfigurationHelper.createAdditionalContentHash(hashes.toArray(String[]::new)), data);
            }
        };
    }
}
