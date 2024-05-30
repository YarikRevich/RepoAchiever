package com.repoachiever.service.hand.external.version;

import com.repoachiever.entity.ConfigEntity;
import com.repoachiever.exception.ApiServerOperationFailureException;
import com.repoachiever.model.VersionInfoResult;
import com.repoachiever.service.client.info.version.VersionInfoClientService;
import com.repoachiever.service.hand.common.ICommand;
import org.springframework.stereotype.Service;

/**
 * Represents version external command service.
 */
@Service
public class VersionExternalCommandService implements ICommand<VersionInfoResult, ConfigEntity> {
    /**
     * @see ICommand
     */
    public VersionInfoResult process(ConfigEntity config) throws ApiServerOperationFailureException {
        VersionInfoClientService versionInfoClientService =
                new VersionInfoClientService(config.getApiServer().getHost());

        return versionInfoClientService.process(null);
    }
}
