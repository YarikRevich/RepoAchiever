package com.repoachiever.service.integration.vendor;

import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.exception.ApiServerOperationFailureException;
import com.repoachiever.service.apiserver.resource.ApiServerCommunicationResource;
import com.repoachiever.service.state.StateService;
import com.repoachiever.service.vendor.VendorFacade;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Service used to perform RepoAchiever Cluster vendor operations.
 */
@Component
public class VendorConfigService {
    @Autowired
    private PropertiesEntity properties;

    @Autowired
    private VendorFacade vendorFacade;

    private final static ScheduledExecutorService scheduledExecutorService =
            Executors.newSingleThreadScheduledExecutor();

    /**
     * Performs vendor availability check operations.
     */
    @PostConstruct
    private void process() {
        scheduledExecutorService.scheduleWithFixedDelay(() -> {
            StateService.getVendorAvailability().set(vendorFacade.isVendorAvailable());
        }, 0, properties.getRestClientDynamicTimeout(), TimeUnit.MILLISECONDS);
    }
}