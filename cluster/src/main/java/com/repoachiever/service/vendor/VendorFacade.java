package com.repoachiever.service.vendor;

import com.repoachiever.dto.GitHubLocationNotationDto;
import com.repoachiever.exception.GitHubContentRetrievalFailureException;
import com.repoachiever.exception.LocationDefinitionsAreNotValidException;
import com.repoachiever.service.config.ConfigService;
import com.repoachiever.service.vendor.common.VendorConfigurationHelper;
import com.repoachiever.service.vendor.git.github.GitGitHubVendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/** Provides high-level access to VCS vendor operations. */
@Service
public class VendorFacade {
    @Autowired
    private ConfigService configService;

    @Autowired
    private VendorConfigurationHelper vendorConfigurationHelper;

    @Autowired
    private GitGitHubVendorService gitGitHubVendorService;

    /**
     * Retrieves latest commit hash for the repository with the given name and given branch.
     *
     * @param location given raw location definition.
     * @throws LocationDefinitionsAreNotValidException if given location definitions are not valid.
     * @throws GitHubContentRetrievalFailureException if GitHub GraphQL client content retrieval fails.
     * @return retrieved latest commit hash for repository with the given name and given branch.
     */
    public String getLatestCommitHash(String location) throws
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
     * Retrieves amount of commits for the repository with the given name and given branch.
     *
     * @param location given raw location definition.
     * @throws LocationDefinitionsAreNotValidException if given location definitions are not valid.
     * @throws GitHubContentRetrievalFailureException if GitHub GraphQL client content retrieval fails.
     * @return retrieved amount of repository commits with the given name and given branch.
     */
    public Integer getCommitAmount(String location) throws
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
}
