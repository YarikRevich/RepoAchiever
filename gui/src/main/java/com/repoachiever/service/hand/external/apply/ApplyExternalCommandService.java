package com.repoachiever.service.hand.external.apply;

import com.repoachiever.converter.ConfigCredentialsToContentCredentialsConverter;
import com.repoachiever.converter.ConfigExporterToContentExporterConverter;
import com.repoachiever.converter.ConfigLocationsToContentLocationsConverter;
import com.repoachiever.converter.ConfigProviderToContentProviderConverter;
import com.repoachiever.entity.ConfigEntity;
import com.repoachiever.exception.ApiServerOperationFailureException;
import com.repoachiever.model.ContentApplication;
import com.repoachiever.service.client.content.apply.ApplyContentClientService;
import com.repoachiever.service.hand.common.ICommand;
import org.springframework.stereotype.Service;

/**
 * Represents apply external command service.
 */
@Service
public class ApplyExternalCommandService implements ICommand<Void, ConfigEntity> {
    /**
     * @see ICommand
     */
    @Override
    public Void process(ConfigEntity config) throws ApiServerOperationFailureException {
        ApplyContentClientService applyContentClientService =
                new ApplyContentClientService(config.getApiServer().getHost());

        ContentApplication request = ContentApplication.of(
                ConfigLocationsToContentLocationsConverter.convert(
                        config.getContent().getLocations()),
                ConfigProviderToContentProviderConverter.convert(
                        config.getService().getProvider()),
                ConfigExporterToContentExporterConverter.convert(
                        config.getService().getProvider(),
                        config.getService().getExporter()),
                ConfigCredentialsToContentCredentialsConverter.convert(
                        config.getService().getProvider(),
                        config.getService().getCredentials()));

        return applyContentClientService.process(request);
    }
}
