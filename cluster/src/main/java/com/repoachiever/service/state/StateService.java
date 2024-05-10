package com.repoachiever.service.state;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Service used to operate as a collection of application state properties.
 */
public class StateService {
    /**
     * Represents exit state used to indicate requested application shutdown.
     */
    @Getter
    @Setter
    private static Boolean exit = false;

    /**
     * Represents suspended state used to temporary halt execution of RepoAchiever Cluster allocation. By default
     * RepoAchiever Cluster is considered to be suspended.
     */
    @Getter
    @Setter
    private static Boolean suspended = true;
}
