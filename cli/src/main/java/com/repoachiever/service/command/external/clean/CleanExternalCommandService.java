package com.repoachiever.service.command.external.clean;

import com.repoachiever.converter.ConfigCredentialsToContentCredentialsConverter;
import com.repoachiever.converter.ConfigProviderToContentProviderConverter;
import com.repoachiever.dto.CleanExternalCommandDto;
import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.exception.ApiServerOperationFailureException;
import com.repoachiever.exception.VersionMismatchException;
import com.repoachiever.model.ContentCleanup;
import com.repoachiever.model.VersionInfoResult;
import com.repoachiever.service.client.content.clean.CleanContentClientService;
import com.repoachiever.service.client.info.version.VersionInfoClientService;
import com.repoachiever.service.command.common.ICommand;
import com.repoachiever.service.visualization.state.VisualizationState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Represents clean external command service. */
@Service
public class CleanExternalCommandService implements ICommand<CleanExternalCommandDto> {
    @Autowired
    private PropertiesEntity properties;

    @Autowired private VisualizationState visualizationState;

    /**
     * @see ICommand
     */
    @Override
    public void process(CleanExternalCommandDto cleanExternalCommand) throws ApiServerOperationFailureException {
        visualizationState.getLabel().pushNext();

        VersionInfoClientService versionInfoClientService =
                new VersionInfoClientService(cleanExternalCommand.getConfig().getApiServer().getHost());

        VersionInfoResult versionInfoResult = versionInfoClientService.process(null);

        if (!versionInfoResult.getExternalApi().getHash().equals(properties.getGitCommitId())) {
            throw new ApiServerOperationFailureException(new VersionMismatchException().getMessage());
        }

        CleanContentClientService cleanContentClientService =
                new CleanContentClientService(cleanExternalCommand.getConfig().getApiServer().getHost());

        ContentCleanup request = ContentCleanup.of(
                cleanExternalCommand.getLocation(),
                ConfigProviderToContentProviderConverter.convert(
                        cleanExternalCommand.getConfig().getService().getProvider()),
                ConfigCredentialsToContentCredentialsConverter.convert(
                        cleanExternalCommand.getConfig().getService().getProvider(),
                        cleanExternalCommand.getConfig().getService().getCredentials()));

        cleanContentClientService.process(request);

        visualizationState.getLabel().pushNext();
    }
}