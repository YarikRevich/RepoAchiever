package com.repoachiever.service.client.github;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

/** Represents client for GitHub remote API. */
@Service
public class GitHubClientService {
    private final RestClient restClient = RestClient.create();

//    public void get
}
