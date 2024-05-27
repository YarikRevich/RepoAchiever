package com.repoachiever.entity;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

/** Represents application properties used for application configuration. */
@Getter
@Configuration
public class PropertiesEntity {
  private static final String GIT_CONFIG_PROPERTIES_FILE = "git.properties";

  @Value(value = "${config.default.location}")
  private String configDefaultLocation;

  @Value(value = "${window.main.name}")
  private String windowMainName;

  @Value(value = "${window.main.scale.min.width}")
  private Double windowMainScaleMinWidth;

  @Value(value = "${window.main.scale.min.height}")
  private Double windowMainScaleMinHeight;

  @Value(value = "${window.main.scale.max.width}")
  private Double windowMainScaleMaxWidth;

  @Value(value = "${window.main.scale.max.height}")
  private Double windowMainScaleMaxHeight;

  @Value(value = "${window.settings.name}")
  private String windowSettingsName;

  @Value(value = "${window.settings.scale.width}")
  private Double windowSettingsScaleWidth;

  @Value(value = "${window.settings.scale.height}")
  private Double windowSettingsScaleHeight;

  @Value(value = "${process.background.period}")
  private Integer processBackgroundPeriod;

  @Value(value = "${process.healthcheck.period}")
  private Integer processHealthcheckPeriod;

  @Value(value = "${process.readiness.period}")
  private Integer processReadinessPeriod;

  @Value(value = "${process.window.width.period}")
  private Integer processWindowWidthPeriod;

  @Value(value = "${process.window.height.period}")
  private Integer processWindowHeightPeriod;

  @Value(value = "${spinner.initial.delay}")
  private Integer spinnerInitialDelay;

  @Value(value = "${button.basic.size.width}")
  private Double basicButtonSizeWidth;

  @Value(value = "${button.basic.size.height}")
  private Double basicButtonSizeHeight;

  @Value(value = "${scene.general.background.color.r}")
  private Integer generalBackgroundColorR;

  @Value(value = "${scene.general.background.color.g}")
  private Integer generalBackgroundColorG;

  @Value(value = "${scene.general.background.color.b}")
  private Integer generalBackgroundColorB;

  @Value(value = "${scene.common.header.background.color.r}")
  private Integer commonSceneHeaderBackgroundColorR;

  @Value(value = "${scene.common.header.background.color.g}")
  private Integer commonSceneHeaderBackgroundColorG;

  @Value(value = "${scene.common.header.background.color.b}")
  private Integer commonSceneHeaderBackgroundColorB;

  @Value(value = "${scene.common.header.connection.background.color.r}")
  private Integer commonSceneHeaderConnectionStatusBackgroundColorR;

  @Value(value = "${scene.common.header.connection.background.color.g}")
  private Integer commonSceneHeaderConnectionStatusBackgroundColorG;

  @Value(value = "${scene.common.header.connection.background.color.b}")
  private Integer commonSceneHeaderConnectionStatusBackgroundColorB;

  @Value(value = "${scene.common.menu.background.color.r}")
  private Integer commonSceneMenuBackgroundColorR;

  @Value(value = "${scene.common.menu.background.color.g}")
  private Integer commonSceneMenuBackgroundColorG;

  @Value(value = "${scene.common.menu.background.color.b}")
  private Integer commonSceneMenuBackgroundColorB;

  @Value(value = "${scene.common.content.background.color.r}")
  private Integer commonSceneContentBackgroundColorR;

  @Value(value = "${scene.common.content.background.color.g}")
  private Integer commonSceneContentBackgroundColorG;

  @Value(value = "${scene.common.content.background.color.b}")
  private Integer commonSceneContentBackgroundColorB;

  @Value(value = "${scene.common.content.vertical-gap}")
  private Double commonSceneContentVerticalGap;

  @Value(value = "${scene.common.content.bar.horizontal-gap}")
  private Double sceneCommonContentBarHorizontalGap;

  @Value(value = "${scene.common.footer.background.color.r}")
  private Integer commonSceneFooterBackgroundColorR;

  @Value(value = "${scene.common.footer.background.color.g}")
  private Integer commonSceneFooterBackgroundColorG;

  @Value(value = "${scene.common.footer.background.color.b}")
  private Integer commonSceneFooterBackgroundColorB;

  @Value(value = "${image.status.scale}")
  private Double statusImageScale;

  @Value(value = "${font.default.name}")
  private String fontDefaultName;

  @Value(value = "${image.icon.name}")
  private String imageIconName;

  @Value(value = "${image.arrow.name}")
  private String imageArrowName;

  @Value(value = "${image.edit.name}")
  private String imageEditName;

  @Value(value = "${image.refresh.name}")
  private String imageRefreshName;

  @Value(value = "${image.start.name}")
  private String imageStartName;

  @Value(value = "${image.stop.name}")
  private String imageStopName;

  @Value(value = "${image.bar.width}")
  private Integer imageBarWidth;

  @Value(value = "${image.bar.height}")
  private Integer imageBarHeight;

  @Value(value = "${list-view.stub.name}")
  private String listViewStubName;

  @Value(value = "${alert.api-server-unavailable.message}")
  private String alertApiServerUnavailableMessage;

  @Value(value = "${alert.deployment-finished.message}")
  private String alertDeploymentFinishedMessage;

  @Value(value = "${alert.destruction-finished.message}")
  private String alertDestructionFinishedMessage;

  @Value(value = "${alert.version-mismatch.message}")
  private String alertVersionMismatchMessage;

  @Value(value = "${alert.editor-close-reminder.message}")
  private String alertEditorCloseReminderMessage;

  @Value(value = "${graph.css.location}")
  private String graphCssFileLocation;

  @Value(value = "${graph.properties.location}")
  private String graphPropertiesLocation;

  @Value(value = "${config.root}")
  private String configRootPath;

  @Value(value = "${swap.root}")
  private String swapRootPath;

  @Value(value = "${config.user.file}")
  private String configUserFilePath;

  @Value(value = "${api-server.directory}")
  private String apiServerDirectory;

  @Value(value = "${git.commit.id.abbrev}")
  private String gitCommitId;

  @Bean
  private static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
    PropertySourcesPlaceholderConfigurer propsConfig = new PropertySourcesPlaceholderConfigurer();
    propsConfig.setLocation(new ClassPathResource(GIT_CONFIG_PROPERTIES_FILE));
    propsConfig.setIgnoreResourceNotFound(true);
    propsConfig.setIgnoreUnresolvablePlaceholders(true);
    return propsConfig;
  }

  /**
   * Removes the last symbol in git commit id of the repository.
   *
   * @return chopped repository git commit id.
   */
  public String getGitCommitId() {
    return StringUtils.chop(gitCommitId);
  }
}
