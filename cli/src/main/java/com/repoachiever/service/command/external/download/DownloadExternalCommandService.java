package com.repoachiever.service.command.external.download;

import com.repoachiever.converter.ConfigCredentialsToContentCredentialsConverter;
import com.repoachiever.converter.ConfigProviderToContentProviderConverter;
import com.repoachiever.dto.DownloadExternalCommandDto;
import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.exception.ApiServerOperationFailureException;
import com.repoachiever.exception.VersionMismatchException;
import com.repoachiever.model.ContentDownload;
import com.repoachiever.model.VersionInfoResult;
import com.repoachiever.service.client.content.download.DownloadContentClientService;
import com.repoachiever.service.client.info.version.VersionInfoClientService;
import com.repoachiever.service.command.common.ICommand;
import com.repoachiever.service.visualization.state.VisualizationState;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Represents download external command service.
 */
@Service
public class DownloadExternalCommandService implements ICommand<DownloadExternalCommandDto> {
    @Autowired
    private PropertiesEntity properties;

    @Autowired
    private VisualizationState visualizationState;

    /**
     * @see ICommand
     */
    @Override
    public void process(DownloadExternalCommandDto downloadExternalCommand) throws ApiServerOperationFailureException {
        visualizationState.getLabel().pushNext();

        VersionInfoClientService versionInfoClientService =
                new VersionInfoClientService(downloadExternalCommand.getConfig().getApiServer().getHost());

        VersionInfoResult versionInfoResult = versionInfoClientService.process(null);

        if (!versionInfoResult.getExternalApi().getHash().equals(properties.getGitCommitId())) {
            throw new ApiServerOperationFailureException(new VersionMismatchException().getMessage());
        }

        visualizationState.getLabel().pushNext();

        visualizationState.getLabel().pushNext();

        DownloadContentClientService downloadContentClientService =
                new DownloadContentClientService(downloadExternalCommand.getConfig().getApiServer().getHost());

        ContentDownload request = ContentDownload.of(
                downloadExternalCommand.getLocation(),
                ConfigProviderToContentProviderConverter.convert(
                        downloadExternalCommand.getConfig().getService().getProvider()),
                ConfigCredentialsToContentCredentialsConverter.convert(
                        downloadExternalCommand.getConfig().getService().getProvider(),
                        downloadExternalCommand.getConfig().getService().getCredentials()));

        byte[] contentDownloadResult = downloadContentClientService.process(request);

        try {
            FileUtils.writeByteArrayToFile(new File(downloadExternalCommand.getOutputLocation()), contentDownloadResult);
        } catch (IOException e) {
            throw new ApiServerOperationFailureException(e.getMessage());
        }

        visualizationState.getLabel().pushNext();
    }
}