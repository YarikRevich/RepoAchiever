package com.repoachiever.service.state;

import com.repoachiever.dto.ClusterAllocationDto;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * Service used to operate as a collection of application state properties.
 */
public class StateService {
    /**
     * Represents RepoAchiever Cluster topology state guard.
     */
    @Getter
    private final static ReentrantLock topologyStateGuard = new ReentrantLock();


    /**
     * Represents a set of all available RepoAchiever Cluster allocations.
     */
    @Getter
    private final static List<ClusterAllocationDto> clusterAllocations = new ArrayList<>();

    /**
     * Retrieves RepoAchiever allocations with the given workspace unit key.
     *
     * @param workspaceUnitKey given workspace unit key.
     * @return filtered RepoAchiever allocations according to the given workspace unit key.
     */
    public static List<ClusterAllocationDto> getClusterAllocationsByWorkspaceUnitKey(String workspaceUnitKey) {
        return clusterAllocations.
                stream().
                filter(element -> Objects.equals(element.getWorkspaceUnitKey(), workspaceUnitKey)).
                collect(Collectors.toList());
    }

    /**
     * Adds new RepoAchiever Cluster allocation.
     *
     * @param allocation given RepoAchiever Cluster allocation.
     */
    public static void addClusterAllocation(ClusterAllocationDto allocation) {
        clusterAllocations.add(allocation);
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
        names.forEach(
                element1 -> clusterAllocations.removeIf(element2 -> Objects.equals(element2.getName(), element1)));
    }
}
