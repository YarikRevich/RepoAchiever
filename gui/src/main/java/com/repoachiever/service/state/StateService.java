package com.repoachiever.service.state;

import com.repoachiever.model.ContentRetrievalResult;
import java.io.File;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

/**
 * Service used to operate as a collection of application state properties.
 */
public class StateService {
    @Getter
    @Setter
    private static Double prevMainWindowHeight;

    @Getter
    @Setter
    private static Double mainWindowHeight;

    @Getter
    @Setter
    private static Double prevMainWindowWidth;

    @Getter
    @Setter
    private static Double mainWindowWidth;

    /**
     *
     */
    @Getter
    private static final CountDownLatch mainWindowWidthUpdateMutex = new CountDownLatch(1);

    /**
     *
     */
    @Getter
    private static final CountDownLatch mainWindowHeightUpdateMutex = new CountDownLatch(1);

    /**
     *
     */
    @Getter
    private static final CountDownLatch startupGuard = new CountDownLatch(1);

    /**
     * Represents state when connection with RepoAchiever API Server is established or not.
     */
    @Getter
    @Setter
    private static Boolean connectionEstablished = false;

    /**
     * Represents configuration file location reference.
     */
    @Getter
    @Setter
    private static File configLocation;

    /**
     * Represents retrieved configuration content.
     */
    @Getter
    @Setter
    private static ContentRetrievalResult content;

    /**
     * Checks if window height has changed.
     *
     * @return result of the check.
     */
    @SneakyThrows
    public static Boolean isWindowHeightChanged() {
        if (Objects.isNull(prevMainWindowHeight) && !Objects.isNull(mainWindowHeight)) {
            return true;
        } else if (Objects.isNull(prevMainWindowHeight)) {
            mainWindowHeightUpdateMutex.await();

            return false;
        }

        return !prevMainWindowHeight.equals(mainWindowHeight);
    }

    /**
     * Checks if window width has changed.
     *
     * @return result of the check.
     */
    @SneakyThrows
    public static Boolean isWindowWidthChanged() {
        if (Objects.isNull(prevMainWindowWidth) && !Objects.isNull(mainWindowWidth)) {
            return true;
        } else if (Objects.isNull(prevMainWindowWidth)) {
            mainWindowWidthUpdateMutex.await();

            return false;
        }

        return !prevMainWindowWidth.equals(mainWindowWidth);
    }

    /**
     * Synchronizes main window height.
     */
    public static void synchronizeWindowHeight() {
        prevMainWindowHeight = mainWindowHeight;
    }

    /**
     * Synchronizes main window width.
     */
    public static void synchronizeWindowWidth() {
        prevMainWindowWidth = mainWindowWidth;
    }
}
