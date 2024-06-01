package com.repoachiever.service.hand.external.withdraw;

import com.repoachiever.converter.ConfigCredentialsToContentCredentialsConverter;
import com.repoachiever.converter.ConfigProviderToContentProviderConverter;
import com.repoachiever.entity.ConfigEntity;
import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.exception.ApiServerOperationFailureException;
import com.repoachiever.model.ContentWithdrawal;
import com.repoachiever.service.client.content.withdraw.WithdrawContentClientService;
import com.repoachiever.service.hand.common.ICommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Represents withdraw external command service. */
@Service
public class WithdrawExternalCommandService implements ICommand<Void, ConfigEntity> {
  @Autowired private PropertiesEntity properties;

  /**
   * @see ICommand
   */
  @Override
  public Void process(ConfigEntity config) throws ApiServerOperationFailureException {
    WithdrawContentClientService withdrawContentClientService =
            new WithdrawContentClientService(config.getApiServer().getHost());

    ContentWithdrawal request = ContentWithdrawal.of(
            ConfigProviderToContentProviderConverter.convert(
                    config.getService().getProvider()),
            ConfigCredentialsToContentCredentialsConverter.convert(
                    config.getService().getProvider(),
                    config.getService().getCredentials()));

    return withdrawContentClientService.process(request);
  }
}
