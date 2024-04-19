package com.repoachiever.service.vendor.git.github;

import com.repoachiever.service.client.github.IGitHubClientService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class GitGitHubVendorService {
    @Inject
    @RestClient
    IGitHubClientService gitHubClientService;

    /**
     * Checks if the given token is valid.
     *
     * @return result of the check.
     */
    public boolean isTokenValid(String token) {
        System.out.println(gitHubClientService.getOctocat(token).getStatus());

        return false;
    }
}
