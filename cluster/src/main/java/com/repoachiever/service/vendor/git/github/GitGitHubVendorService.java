package com.repoachiever.service.vendor.git.github;

import com.repoachiever.dto.GitHubCommitAmountResponseDto;
import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.exception.GitHubGraphQlClientContentRetrievalFailureException;
import com.repoachiever.exception.GitHubGraphQlClientDocumentNotFoundException;
import com.repoachiever.service.config.ConfigService;
import com.repoachiever.service.vendor.common.VendorConfigurationHelper;
import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.core.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Objects;

/**
 * Service used to represent GitHub external service operations.
 */
@Service
public class GitGitHubVendorService {
    @Autowired
    private PropertiesEntity properties;

    @Autowired
    private ConfigService configService;

    private HttpGraphQlClient graphQlClient;

    private String document;

    /**
     * Performs initial GraphQL client and HTTP client configuration.
     *
     * @throws GitHubGraphQlClientDocumentNotFoundException if GitHub GraphQL client document is not found.
     */
    @PostConstruct
    private void configure() throws GitHubGraphQlClientDocumentNotFoundException {
        WebClient client = WebClient.builder()
                .baseUrl(properties.getGraphQlClientGitHubUrl())
                .defaultHeader(
                        HttpHeaders.AUTHORIZATION,
                        VendorConfigurationHelper.getWrappedToken(
                                configService.getConfig().getService().getCredentials().getToken()))
                .build();

        this.graphQlClient = HttpGraphQlClient.builder(client).build();

        Resource resource = new ClassPathResource(properties.getGraphQlClientGitHubDocumentLocation());

        try {
            resource.getContentAsString(Charset.defaultCharset());
        } catch (IOException e) {
            throw new GitHubGraphQlClientDocumentNotFoundException(e.getMessage());
        }
    }

    /**
     * Retrieves amount of commits for the repository with the given name and given branch.
     *
     * @param owner given repository owner.
     * @param name given repository name.
     * @param branch given repository branch.
     * @throws GitHubGraphQlClientContentRetrievalFailureException if GitHub GraphQL client content retrieval fails.
     * @return retrieved amount of repository commits with the given name and given branch.
     */
    public Integer getCommitAmount(String owner, String name, String branch) throws
            GitHubGraphQlClientContentRetrievalFailureException {
        GitHubCommitAmountResponseDto gitHubCommitAmountResponse;

        try {
            gitHubCommitAmountResponse = graphQlClient
                    .document(document)
                    .variables(new HashMap<>() {
                        {
                            put(port, 3000);
                        }
                    })
                    .retrieve("CommitAmount")
                    .toEntity(GitHubCommitAmountResponseDto.class)
                    .block();
        } catch (WebClientResponseException e) {
            throw new GitHubGraphQlClientContentRetrievalFailureException(e.getResponseBodyAsString());
        } catch (WebClientRequestException e) {
            throw new GitHubGraphQlClientContentRetrievalFailureException(e.getMessage());
        }

        if (Objects.isNull(gitHubCommitAmountResponse)) {
            throw new GitHubGraphQlClientContentRetrievalFailureException();
        }

        return gitHubCommitAmountResponse
                .getData()
                .getRepository()
                .getRef()
                .getTarget()
                .getHistory()
                .getTotalCount();
    }
}
