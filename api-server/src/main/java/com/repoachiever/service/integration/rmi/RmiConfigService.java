package com.repoachiever.service.integration.rmi;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Service used to perform initial RMI configuration.
 */
@ApplicationScoped
public class RmiConfigService {
    /**
     * Creates RMI register and performs its initial configuration.
     */
    @PostConstruct
    private void process() {
        try {
            LocateRegistry.getRegistry(Registry.REGISTRY_PORT);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
//
//        try {
//            LocateRegistry.getRegistry().bind(String.format("test%d",
//                    10 + (int)(Math.random() * ((100 - 10) + 1))), new TestImpl());
//        } catch (RemoteException | AlreadyBoundException e) {
//            throw new RuntimeException(e);
//        }
    }
}
