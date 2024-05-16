package com.repoachiever.service.vendor.git.github;

import com.repoachiever.dto.GitHubLocationNotationDto;
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
                    .getOctocat(token);

            return response.getStatus() == HttpStatus.SC_OK;
        } catch (WebApplicationException e) {
            return false;
        }
    }

    /**
     * Checks if the given content locations are valid.
     *
     * @param token     given authorization token.
     * @param locations given location in GitHub notation.
     * @return result of the check.
     */
    public Boolean areLocationsValid(String token, List<GitHubLocationNotationDto> locations) {
        return locations
                .stream()
                .allMatch(element -> {
                    try {
                        Response response;

                        if (element.getBranch().isPresent()) {
                            response = gitHubClientService
                                    .getRepository(
                                            token,
                                            element.getOwner(),
                                            element.getName(),
                                            element.getBranch().get());
                        } else {
                            response = gitHubClientService
                                    .getRepository(
                                            token,
                                            element.getOwner(),
                                            element.getName());
                        }

                        if (response.getStatus() != HttpStatus.SC_OK) {
                            return false;
                        }
                    } catch (WebApplicationException e) {
                        return false;
                    }

                    return true;
                });
    }
}
