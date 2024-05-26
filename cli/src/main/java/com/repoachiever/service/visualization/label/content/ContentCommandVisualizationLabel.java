package com.repoachiever.service.visualization.label.content;

import com.repoachiever.dto.VisualizationLabelDto;
import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.service.visualization.common.IVisualizationLabel;

import java.util.ArrayDeque;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Represents label set used for content command service.
 */
@Service
public class ContentCommandVisualizationLabel implements IVisualizationLabel {
    private final ArrayDeque<VisualizationLabelDto> stepsQueue = new ArrayDeque<>();

    private final ArrayDeque<String> batchQueue = new ArrayDeque<>();

    private final ReentrantLock mutex = new ReentrantLock();

    public ContentCommandVisualizationLabel(@Autowired PropertiesEntity properties) {
        stepsQueue.addAll(
                List.of(
                        VisualizationLabelDto.of(
                                properties.getProgressVisualizationHealthCheckRequestLabel(), 10),
                        VisualizationLabelDto.of(
                                properties.getProgressVisualizationHealthCheckResponseLabel(), 30),
                        VisualizationLabelDto.of(
                                properties.getProgressVisualizationVersionRequestLabel(), 40),
                        VisualizationLabelDto.of(
                                properties.getProgressVisualizationVersionResponseLabel(), 60),
                        VisualizationLabelDto.of(
                                properties.getProgressVisualizationContentRequestLabel(), 70),
                        VisualizationLabelDto.of(
                                properties.getProgressVisualizationContentResponseLabel(), 100)));
    }

    /**
     * @see IVisualizationLabel
     */
    @Override
    public Boolean isEmpty() {
        return stepsQueue.isEmpty();
    }

    /**
     * @see IVisualizationLabel
     */
    @Override
    public Boolean isNext() {
        mutex.lock();

        try {
            return !batchQueue.isEmpty();
        } finally {
            mutex.unlock();
        }
    }

    /**
     * @see IVisualizationLabel
     */
    @Override
    public void pushNext() {
        mutex.lock();

        batchQueue.push(stepsQueue.pop().toString());

        mutex.unlock();
    }

    /**
     * @see IVisualizationLabel
     */
    @Override
    public String getCurrent() {
        mutex.lock();

        try {
            return batchQueue.pollLast();
        } finally {
            mutex.unlock();
        }
    }
}
