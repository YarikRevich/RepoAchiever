package com.repoachiever;

import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.service.config.ConfigService;
import com.repoachiever.service.config.common.ValidConfigService;
import com.repoachiever.service.scheduler.SchedulerService;
import com.repoachiever.service.scheduler.executor.CommandExecutorService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

@Component
@Import({
  SchedulerService.class,
  ConfigService.class,
  ValidConfigService.class,
  CommandExecutorService.class,
  PropertiesEntity.class
})
public class App implements ApplicationRunner {
  private static final Logger logger = LogManager.getLogger(App.class);

  @Autowired private ValidConfigService validConfigService;

  @Autowired private SchedulerService schedulerService;

  @Override
  public void run(ApplicationArguments args) {
    try {
      validConfigService.validate();
    } catch (Exception e) {
      logger.fatal(e.getMessage());
      return;
    }

    schedulerService.start();
  }
}
