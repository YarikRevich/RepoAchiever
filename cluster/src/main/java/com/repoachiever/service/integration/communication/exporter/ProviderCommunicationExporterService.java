package com.repoachiever.service.integration.communication.exporter;

import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.service.config.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProviderCommunicationExporterService {
    @Autowired
    private PropertiesEntity properties;

    @Autowired
    private ConfigService configService;

//    @Bean
//    public RmiServiceExporter rmiServiceExporter() throws Exception {
//        Registry registry;
//
//        try {
//            registry = LocateRegistry.getRegistry(
//                    configService.getConfig().getCommunication().getPort());
//        } catch (RemoteException e) {
//            return null;
//        }
//
//        RmiServiceExporter exporter = new RmiServiceExporter();
//        exporter.setRegistry(registry);
//        exporter.setServiceName(ProviderCommunicationConfigurationHelper.getBindName(
//                configService.getConfig().getCommunication().getPort() + 1,
//                configService.getConfig().getName()));
//        exporter.setServiceInterface(ICommunicationService.class);
//        exporter.setServicePort(configService.getConfig().getCommunication().getPort() + 1);
//        exporter.setService(new CommunicationProviderResource(properties));
//        return exporter;
//    }
}
