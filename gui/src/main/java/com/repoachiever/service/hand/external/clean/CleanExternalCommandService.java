package com.repoachiever.service.hand.external.clean;

import com.repoachiever.converter.ConfigCredentialsToContentCredentialsConverter;
import com.repoachiever.converter.ConfigProviderToContentProviderConverter;
import com.repoachiever.dto.CleanExternalCommandDto;
import com.repoachiever.exception.ApiServerOperationFailureException;
import com.repoachiever.model.ContentCleanup;
import com.repoachiever.service.client.content.clean.CleanContentClientService;
import com.repoachiever.service.hand.common.ICommand;
import org.springframework.stereotype.Service;

/** Represents clean external command service. */
@Service
public class CleanExternalCommandService implements ICommand<Void, CleanExternalCommandDto> {
    /**
     * @see ICommand
     */
    @Override
    public Void process(CleanExternalCommandDto cleanExternalCommand) throws ApiServerOperationFailureException {
        CleanContentClientService cleanContentClientService =
                new CleanContentClientService(cleanExternalCommand.getConfig().getApiServer().getHost());

        ContentCleanup request = ContentCleanup.of(
                cleanExternalCommand.getLocation(),
                ConfigProviderToContentProviderConverter.convert(
                        cleanExternalCommand.getConfig().getService().getProvider()),
                ConfigCredentialsToContentCredentialsConverter.convert(
                        cleanExternalCommand.getConfig().getService().getProvider(),
                        cleanExternalCommand.getConfig().getService().getCredentials()));

        return cleanContentClientService.process(request);
    }
}