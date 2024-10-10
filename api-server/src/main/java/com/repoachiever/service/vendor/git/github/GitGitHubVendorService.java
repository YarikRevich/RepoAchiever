package com.repoachiever.service.vendor.git.github;

import com.repoachiever.dto.GitHubRepositoriesDto;
import com.repoachiever.dto.GitHubUserDto;
import com.repoachiever.entity.common.PropertiesEntity;
import com.repoachiever.dto.GitHubLocationNotationDto;
import com.repoachiever.service.client.github.IGitHubClientService;
import com.repoachiever.service.vendor.common.VendorConfigurationHelper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import org.apache.http.HttpStatus;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Service used to represent GitHub external service operations.
 */
@ApplicationScoped
public class GitGitHubVendorService {
    @Inject
    PropertiesEntity properties;

    @Inject
    @RestClient
    IGitHubClientService gitHubClientService;

    /**
     * Retrieves repositories of all types for the provided user.
     * 
     * @param token given access token.
     * @return retrieved user repositories of all types.
     */
    private List<GitHubRepositoriesDto> getRepositories(String token) {
        List<GitHubRepositoriesDto> result = new ArrayList();

        Response response;

        for (Integer page = 1; page < properties.getRestClientGitHubMaxPage(); page++) {
            response = gitHubClientService.getRepositories(
                    token,
                    properties.getRestClientGitHubPageSize(),
                    page,
                    properties.getRestClientGitHubRepoVisibility());

            if (response.getStatus() != HttpStatus.SC_OK) {
                return null;
            }

            List<GitHubRepositoriesDto> part = response
                    .readEntity(new GenericType<List<GitHubRepositoriesDto>>() {
                    });

            if (part.isEmpty()) {
                break;
            }

            result.addAll(part);
        }

        return result;
    };

    /**
     * Checks if the given token is valid.
     *
     * @param token given access token to be validated.
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
        List<GitHubRepositoriesDto> repositories = getRepositories(token);

        if (Objects.isNull(repositories)) {
            return false;
        }

        return locations
                .stream()
                .allMatch(e1 -> {
                    if (e1.getBranch().isPresent()) {
                        if (repositories.stream()
                                .anyMatch(e2 -> Objects.equals(
                                        e2.getName(), e1.getName()) &&
                                        Objects.equals(
                                                e2.getDefaultBranch(), e1.getBranch().get()))) {
                            return true;
                        }

                        try {
                            return gitHubClientService
                                    .getRepository(
                                            token,
                                            e1.getOwner(),
                                            e1.getName(),
                                            e1.getBranch().get())
                                    .getStatus() == HttpStatus.SC_OK;
                        } catch (WebApplicationException e) {
                            System.out.println(e.getMessage());

                            return false;
                        }
                    }

                    return repositories.stream()
                            .anyMatch(e2 -> Objects.equals(e2.getName(), e1.getName()));
                });
    }
}
