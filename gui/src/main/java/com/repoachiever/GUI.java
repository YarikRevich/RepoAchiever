package com.repoachiever;

import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Represents entry point for the RepoAchiever GUI application.
 */
@SpringBootApplication
public class GUI {
    public static void main(String[] args) {
        App app = new App();

        app.launch();
    }
}
