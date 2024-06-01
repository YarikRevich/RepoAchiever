package com.repoachiever.resource.communication;

import com.repoachiever.dto.SuspenderDto;
import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.logging.common.LoggingConfigurationHelper;
import com.repoachiever.service.communication.cluster.IClusterCommunicationService;
import com.repoachiever.service.state.StateService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.CountDownLatch;

/**
 * Contains implementation of communication provider for RepoAchiever Cluster.
 */
@Component
public class ClusterCommunicationResource extends UnicastRemoteObject implements IClusterCommunicationService {
    private static final Logger logger = LogManager.getLogger(ClusterCommunicationResource.class);

    @Autowired
    private PropertiesEntity properties;

    public ClusterCommunicationResource() throws RemoteException {
    }

    /**
     * @see IClusterCommunicationService
     */
    @Override
    public void performSuspend() throws RemoteException {
        StateService.getSuspendGuard().lock();

        StateService.getSuspended().set(true);

        for (SuspenderDto suspender : StateService.getSuspenders()) {
            try {
                suspender
                        .getAwaiter()
                        .get()
                        .await();
            } catch (InterruptedException e) {
                throw new RemoteException(e.getMessage());
            }
        }

        StateService.getSuspendGuard().unlock();
    }

    /**
     * @see IClusterCommunicationService
     */
    @Override
    public void performServe() throws RemoteException {
        StateService.getSuspendGuard().lock();

        StateService.getSuspended().set(false);

        StateService.getSuspendGuard().unlock();
    }

    /**
     * @see IClusterCommunicationService
     */
    @Override
    public Boolean retrieveHealthCheck() throws RemoteException {
        return true;
    }

    /**
     * @see IClusterCommunicationService
     */
    @Override
    public String retrieveVersion() throws RemoteException {
        return properties.getGitCommitId();
    }
}