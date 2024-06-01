package com.repoachiever.service.hand.external.download;

import com.repoachiever.converter.ConfigCredentialsToContentCredentialsConverter;
import com.repoachiever.converter.ConfigProviderToContentProviderConverter;
import com.repoachiever.dto.DownloadExternalCommandDto;
import com.repoachiever.exception.ApiServerOperationFailureException;
import com.repoachiever.model.ContentDownload;
import com.repoachiever.service.client.content.download.DownloadContentClientService;
import com.repoachiever.service.hand.common.ICommand;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Represents download external command service.
 */
@Service
public class DownloadExternalCommandService implements ICommand<byte[], DownloadExternalCommandDto> {
    /**
     * @see ICommand
     */
    @Override
    public byte[] process(DownloadExternalCommandDto downloadExternalCommand) throws ApiServerOperationFailureException {
        DownloadContentClientService downloadContentClientService =
                new DownloadContentClientService(downloadExternalCommand.getConfig().getApiServer().getHost());

        ContentDownload request = ContentDownload.of(
                downloadExternalCommand.getLocation(),
                ConfigProviderToContentProviderConverter.convert(
                        downloadExternalCommand.getConfig().getService().getProvider()),
                ConfigCredentialsToContentCredentialsConverter.convert(
                        downloadExternalCommand.getConfig().getService().getProvider(),
                        downloadExternalCommand.getConfig().getService().getCredentials()));

        return downloadContentClientService.process(request);
    }
}