package com.repoachiever.service.state;

import lombok.Getter;
import lombok.Setter;

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
}
