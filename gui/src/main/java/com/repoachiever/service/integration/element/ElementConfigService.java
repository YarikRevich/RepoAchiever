package com.repoachiever.service.integration.element;

import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.service.element.storage.ElementStorage;
import com.repoachiever.service.element.text.common.IElementActualizable;
import com.repoachiever.service.element.text.common.IElementResizable;
import com.repoachiever.service.event.state.LocalState;
import com.repoachiever.service.scheduler.SchedulerConfigurationHelper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Service used to perform RepoAchiever GUI background processes handling operations.
 */
@Component
public class ElementConfigService {
    @Autowired
    private PropertiesEntity properties;

    /**
     * Handles background all the background processes and propagates data to the proper objects if needed.
     */
    @PostConstruct
    private void process() {
        SchedulerConfigurationHelper.scheduleTask(
                () ->
                        ElementStorage.getActualizables()
                                .forEach(IElementActualizable::handleBackgroundUpdates),
                properties.getProcessBackgroundPeriod());

        SchedulerConfigurationHelper.scheduleTask(
                () -> {
                    if (LocalState.isWindowWidthChanged()) {
                        ElementStorage.getResizables().forEach(IElementResizable::handlePrefWidth);

                        LocalState.synchronizeWindowWidth();
                    }
                },
                properties.getProcessWindowWidthPeriod());

        SchedulerConfigurationHelper.scheduleTask(
                () -> {
                    if (LocalState.isWindowHeightChanged()) {
                        ElementStorage.getResizables().forEach(IElementResizable::handlePrefHeight);

                        LocalState.synchronizeWindowHeight();
                    }
                },
                properties.getProcessWindowHeightPeriod());
    }
}
