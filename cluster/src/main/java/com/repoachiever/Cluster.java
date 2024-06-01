package com.repoachiever;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Represents entry point for the RepoAchiever Cluster application.
 */
@SpringBootApplication
public class Cluster {
  public static void main(String[] args) {
    SpringApplication application = new SpringApplication(App.class);

    System.exit(SpringApplication.exit(application.run(args)));
  }
}
