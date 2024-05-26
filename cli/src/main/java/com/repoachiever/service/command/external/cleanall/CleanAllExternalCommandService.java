package com.repoachiever.service.command.external.cleanall;

import com.repoachiever.converter.ConfigCredentialsToContentCredentialsConverter;
import com.repoachiever.converter.ConfigProviderToContentProviderConverter;
import com.repoachiever.dto.CleanExternalCommandDto;
import com.repoachiever.entity.ConfigEntity;
import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.exception.ApiServerOperationFailureException;
import com.repoachiever.exception.VersionMismatchException;
import com.repoachiever.model.ContentCleanup;
import com.repoachiever.model.ContentCleanupAll;
import com.repoachiever.model.VersionInfoResult;
import com.repoachiever.service.client.content.clean.CleanContentClientService;
import com.repoachiever.service.client.content.clean.all.CleanAllContentClientService;
import com.repoachiever.service.client.info.version.VersionInfoClientService;
import com.repoachiever.service.command.common.ICommand;
import com.repoachiever.service.visualization.state.VisualizationState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Represents cleanall external command service. */
@Service
public class CleanAllExternalCommandService implements ICommand<ConfigEntity> {
    @Autowired
    private PropertiesEntity properties;

    @Autowired private VisualizationState visualizationState;

    /**
     * @see ICommand
     */
    @Override
    public void process(ConfigEntity config) throws ApiServerOperationFailureException {
        visualizationState.getLabel().pushNext();

        VersionInfoClientService versionInfoClientService =
                new VersionInfoClientService(config.getApiServer().getHost());

        VersionInfoResult versionInfoResult = versionInfoClientService.process(null);

        if (!versionInfoResult.getExternalApi().getHash().equals(properties.getGitCommitId())) {
            throw new ApiServerOperationFailureException(new VersionMismatchException().getMessage());
        }

        CleanAllContentClientService cleanAllContentClientService =
                new CleanAllContentClientService(config.getApiServer().getHost());

        ContentCleanupAll request = ContentCleanupAll.of(
                ConfigProviderToContentProviderConverter.convert(
                        config.getService().getProvider()),
                ConfigCredentialsToContentCredentialsConverter.convert(
                        config.getService().getProvider(),
                        config.getService().getCredentials()));

        cleanAllContentClientService.process(request);

        visualizationState.getLabel().pushNext();
    }
}