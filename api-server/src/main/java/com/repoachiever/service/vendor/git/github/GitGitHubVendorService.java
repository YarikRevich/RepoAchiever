package com.repoachiever.service.vendor.git.github;

import com.repoachiever.service.client.github.IGitHubClientService;
import com.repoachiever.service.vendor.common.VendorConfigurationHelper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.apache.http.HttpStatus;
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
        try {
            Response response = gitHubClientService
                    .getOctocat(VendorConfigurationHelper.getWrappedToken(token));

            return response.getStatus() == HttpStatus.SC_OK;
        } catch (WebApplicationException e) {
            return false;
        }
    }
}
