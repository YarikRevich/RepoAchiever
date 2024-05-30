package com.repoachiever.service.hand.external.content;

import com.repoachiever.converter.ConfigCredentialsToContentCredentialsConverter;
import com.repoachiever.converter.ConfigProviderToContentProviderConverter;
import com.repoachiever.entity.ConfigEntity;
import com.repoachiever.exception.ApiServerOperationFailureException;
import com.repoachiever.model.ContentRetrievalApplication;
import com.repoachiever.model.ContentRetrievalResult;
import com.repoachiever.service.client.content.ContentClientService;
import com.repoachiever.service.hand.common.ICommand;
import org.springframework.stereotype.Service;

/** Represents content external command service. */
@Service
public class ContentExternalCommandService implements ICommand<ContentRetrievalResult, ConfigEntity> {
    /**
     * @see ICommand
     */
    @Override
    public ContentRetrievalResult process(ConfigEntity config) throws ApiServerOperationFailureException {
        ContentClientService contentClientService = new ContentClientService(config.getApiServer().getHost());

        ContentRetrievalApplication request = ContentRetrievalApplication.of(
                ConfigProviderToContentProviderConverter.convert(
                        config.getService().getProvider()),
                ConfigCredentialsToContentCredentialsConverter.convert(
                        config.getService().getProvider(),
                        config.getService().getCredentials()));

        return contentClientService.process(request);
    }
}