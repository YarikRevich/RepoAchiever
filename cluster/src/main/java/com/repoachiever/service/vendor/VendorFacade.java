package com.repoachiever.service.vendor;

import com.repoachiever.dto.GitHubLocationNotationDto;
import com.repoachiever.exception.GitHubGraphQlClientContentRetrievalFailureException;
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
     * Retrieves amount of commits for the repository with the given name and given branch.
     *
     * @throws LocationDefinitionsAreNotValidException if given location definitions are not valid.
     * @throws GitHubGraphQlClientContentRetrievalFailureException if GitHub GraphQL client content retrieval fails.
     * @return retrieved amount of repository commits with the given name and given branch.
     */
    public Integer getCommitAmount(String location) throws
            LocationDefinitionsAreNotValidException,
            GitHubGraphQlClientContentRetrievalFailureException {
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
