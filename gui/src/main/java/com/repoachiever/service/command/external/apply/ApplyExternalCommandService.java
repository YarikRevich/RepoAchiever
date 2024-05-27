package com.repoachiever.service.command.external.apply;

import com.repoachiever.entity.ConfigEntity;
import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.service.client.content.apply.ApplyContentClientService;
import com.repoachiever.service.client.info.version.VersionInfoClientService;
import com.repoachiever.service.command.common.ICommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Represents apply external command service. */
@Service
public class ApplyExternalCommandService implements ICommand<ConfigEntity> {
  @Autowired private PropertiesEntity properties;

  /**
   * @see ICommand
   */
  @Override
  public void process(ConfigEntity config) throws ApiServerOperationFailureException {
    VersionInfoClientService versionInfoClientService =
            new VersionInfoClientService(config.getApiServer().getHost());

    VersionInfoResult versionInfoResult = versionInfoClientService.process(null);

    if (!versionInfoResult.getExternalApi().getHash().equals(properties.getGitCommitId())) {
      throw new ApiServerOperationFailureException(new VersionMismatchException().getMessage());
    }

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

    applyContentClientService.process(request);
  }
}