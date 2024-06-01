package com.repoachiever.service.hand.external.cleanall;

import com.repoachiever.converter.ConfigCredentialsToContentCredentialsConverter;
import com.repoachiever.converter.ConfigProviderToContentProviderConverter;
import com.repoachiever.entity.ConfigEntity;
import com.repoachiever.exception.ApiServerOperationFailureException;
import com.repoachiever.model.ContentCleanupAll;
import com.repoachiever.service.client.content.clean.all.CleanAllContentClientService;
import com.repoachiever.service.hand.common.ICommand;
import org.springframework.stereotype.Service;

/** Represents cleanall external command service. */
@Service
public class CleanAllExternalCommandService implements ICommand<Void, ConfigEntity> {
    /**
     * @see ICommand
     */
    @Override
    public Void process(ConfigEntity config) throws ApiServerOperationFailureException {
        CleanAllContentClientService cleanAllContentClientService =
                new CleanAllContentClientService(config.getApiServer().getHost());

        ContentCleanupAll request = ContentCleanupAll.of(
                ConfigProviderToContentProviderConverter.convert(
                        config.getService().getProvider()),
                ConfigCredentialsToContentCredentialsConverter.convert(
                        config.getService().getProvider(),
                        config.getService().getCredentials()));

        return cleanAllContentClientService.process(request);
    }
}