package com.repoachiever.service.vendor.git.github;

import com.repoachiever.service.client.github.IGitHubClientService;
import com.repoachiever.service.vendor.common.VendorConfigurationHelper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.apache.http.HttpStatus;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;

/**
 * Service used to represent GitHub external service operations.
 */
@ApplicationScoped
public class GitGitHubVendorService {
    @Inject
    @RestClient
    IGitHubClientService gitHubClientService;

    /**
     * Checks if the given token is valid.
     *
     * @param token given token to be validated.
     * @return result of the check.
     */
    public Boolean isTokenValid(String token) {
        try {
            Response response = gitHubClientService
                    .getOctocat(VendorConfigurationHelper.getWrappedToken(token));

            return response.getStatus() == HttpStatus.SC_OK;
        } catch (WebApplicationException e) {
            return false;
        }
    }

    /**
     * Checks if the given content locations are valid.
     *
     * @param token given authorization token.
     * @param locations given location names.
     * @return result of the check.
     */
    public Boolean areLocationsValid(String token, List<String> locations) {
        for (String location : locations) {
            try {
                Response response = gitHubClientService
                        .getRepository(
                                VendorConfigurationHelper.getWrappedToken(token),
                                location);

                return response.getStatus() == HttpStatus.SC_OK;
            } catch (WebApplicationException e) {
                return false;
            }
        }

        return false;
    }
}
