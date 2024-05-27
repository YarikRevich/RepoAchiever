package com.repoachiever.service.state;

import com.repoachiever.dto.ClusterAllocationDto;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * Service used to operate as a collection of application state properties.
 */
public class StateService {
    /**
     * Represents if RepoAchiever API Server application has been started.
     */
    @Getter
    @Setter
    private static Boolean started = false;

    /**
     * Represents RepoAchiever API Server application startup guard.
     */
    @Getter
    private final static CountDownLatch startGuard = new CountDownLatch(1);

    /**
     * Represents RepoAchiever Cluster topology state guard.
     */
    @Getter
    private final static ReentrantLock topologyStateGuard = new ReentrantLock();

    /**
     * Represents RepoAchiever Cluster communication guard.
     */
    @Getter
    private final static ReentrantLock communicationGuard = new ReentrantLock();

    /**
     * Represents a set of all available RepoAchiever Cluster allocations.
     */
    @Getter
    private final static List<ClusterAllocationDto> clusterAllocations = new ArrayList<>();

    /**
     * Retrieves RepoAchiever Cluster allocations with the given workspace unit key.
     *
     * @param workspaceUnitKey given workspace unit key.
     * @return filtered RepoAchiever Cluster allocations according to the given workspace unit key.
     */
    public static List<ClusterAllocationDto> getClusterAllocationsByWorkspaceUnitKey(String workspaceUnitKey) {
        return clusterAllocations.
                stream()
                .filter(element -> Objects.equals(element.getWorkspaceUnitKey(), workspaceUnitKey))
                .toList();
    }

    /**
     * Retrieves RepoAchiever Cluster allocation with the given workspace unit key and name.
     *
     * @param workspaceUnitKey given workspace unit key.
     * @param name             given RepoAchiever Cluster allocation name.
     * @return filtered RepoAchiever Cluster allocations according to the given workspace unit key.
     */
    public static ClusterAllocationDto getClusterAllocationByWorkspaceUnitKeyAndName(
            String workspaceUnitKey, String name) {
        return clusterAllocations.
                stream()
                .filter(element -> Objects.equals(element.getWorkspaceUnitKey(), workspaceUnitKey) &&
                        Objects.equals(element.getName(), name))
                .toList()
                .getFirst();
    }

    /**
     * Adds new RepoAchiever Cluster allocations.
     *
     * @param allocations given RepoAchiever Cluster allocations.
     */
    public static void addClusterAllocations(List<ClusterAllocationDto> allocations) {
        clusterAllocations.addAll(allocations);
    }

    /**
     * Checks if RepoAchiever Cluster allocations with the given name exists.
     *
     * @param name given RepoAchiever Cluster allocation.
     * @return result of the check.
     */
    public static Boolean isClusterAllocationPresentByName(String name) {
        return clusterAllocations
                .stream()
                .anyMatch(element -> Objects.equals(element.getName(), name));
    }

    /**
     * Removes RepoAchiever Cluster allocations with the given names.
     *
     * @param names given RepoAchiever Cluster allocation names.
     */
    public static void removeClusterAllocationByNames(List<String> names) {
        names.forEach(
                element1 -> clusterAllocations.removeIf(element2 -> Objects.equals(element2.getName(), element1)));
    }
}
