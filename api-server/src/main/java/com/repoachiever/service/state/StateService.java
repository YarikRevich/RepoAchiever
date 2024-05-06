package com.repoachiever.service.state;

import com.repoachiever.dto.ClusterAllocationDto;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Service used to operate as a collection of application state properties.
 */
public class StateService {
    private final static ReentrantLock clusterAllocationsMutex = new ReentrantLock();

    /**
     * Represents exit state used to indicate requested application shutdown.
     */
    @Getter
    private final static List<ClusterAllocationDto> clusterAllocations = new ArrayList<>();

    /**
     * Adds new RepoAchiever Cluster allocation.
     *
     * @param allocation given RepoAchiever Cluster allocation.
     */
    public static void addClusterAllocation(ClusterAllocationDto allocation) {
        clusterAllocationsMutex.lock();

        clusterAllocations.add(allocation);

        clusterAllocationsMutex.unlock();
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
     * Retrieves RepoAchiever Cluster allocation with the given name.
     *
     * @param name given RepoAchiever Cluster allocation name.
     * @return retrieved RepoAchiever Cluster allocation.
     */
    public static ClusterAllocationDto getClusterAllocationByName(String name) {
        return clusterAllocations
                .stream()
                .filter(element -> Objects.equals(element.getName(), name))
                .findFirst()
                .get();
    }

    /**
     * Removes RepoAchiever Cluster allocations with the given names.
     *
     * @param names given RepoAchiever Cluster allocation names.
     */
    public static void removeClusterAllocationByNames(List<String> names) {
        clusterAllocationsMutex.lock();

        names.forEach(
                element1 -> clusterAllocations.removeIf(element2 -> Objects.equals(element2.getName(), element1)));

        clusterAllocationsMutex.unlock();
    }
}
