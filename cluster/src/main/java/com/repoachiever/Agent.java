package com.repoachiever;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Agent {
  public static void main(String[] args) {
    SpringApplication application = new SpringApplication(App.class);
    System.exit(SpringApplication.exit(application.run(args)));
  }
}
