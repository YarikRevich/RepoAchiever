package com.repoachiever.service.command.external.withdraw;

import com.repoachiever.entity.ConfigEntity;
import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.model.ContentWithdrawal;
import com.repoachiever.model.VersionInfoResult;
import com.repoachiever.service.client.content.withdraw.WithdrawContentClientService;
import com.repoachiever.service.client.info.version.VersionInfoClientService;
import com.repoachiever.service.command.common.ICommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Represents withdraw external command service. */
@Service
public class WithdrawExternalCommandService implements ICommand<ConfigEntity> {
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

    WithdrawContentClientService withdrawContentClientService =
            new WithdrawContentClientService(config.getApiServer().getHost());

    ContentWithdrawal request = ContentWithdrawal.of(
            ConfigProviderToContentProviderConverter.convert(
                    config.getService().getProvider()),
            ConfigCredentialsToContentCredentialsConverter.convert(
                    config.getService().getProvider(),
                    config.getService().getCredentials()));

    withdrawContentClientService.process(request);
  }
}