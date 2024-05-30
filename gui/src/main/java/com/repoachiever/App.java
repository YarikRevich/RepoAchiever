package com.repoachiever;

import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.service.config.ConfigService;
import com.repoachiever.service.element.layout.scene.main.start.MainStartSceneLayout;
import com.repoachiever.service.element.layout.scene.main.start.common.MainStartMenuButtonBox;
import com.repoachiever.service.element.layout.scene.settings.SettingsGeneralSceneLayout;
import com.repoachiever.service.element.progressbar.main.start.MainStartCircleProgressBar;
import com.repoachiever.service.element.scene.main.start.MainStartScene;
import com.repoachiever.service.element.scene.settings.SettingsGeneralScene;
import com.repoachiever.service.integration.apiserver.healthcheck.ApiServerHealthCheckService;
import com.repoachiever.service.element.font.FontLoader;
import com.repoachiever.service.integration.element.ElementConfigService;
import com.repoachiever.service.element.stage.MainStage;
import com.repoachiever.service.event.state.LocalState;
import com.repoachiever.service.scheduler.SchedulerConfigurationHelper;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Represents initialization point for the RepoAchiever GUI application.
 */
@Component
public class App extends Application {
    private ConfigurableApplicationContext applicationContext;

    @Autowired
    private LocalState localState;

    @Autowired
    private ApiServerHealthCheckService apiServerHealthCheckService;

    @Autowired
    private ElementConfigService elementConfigService;

    @Autowired
    private FontLoader fontLoader;

    @Autowired
    private MainStage mainStage;

    /**
     * @see Application
     */
    public void launch() {
        Application.launch();
    }

    /**
     * @see Application
     */
    @Override
    public void init() {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("apple.awt.UIElement", "true");

        ApplicationContextInitializer<GenericApplicationContext> initializer =
                applicationContext -> {
                    applicationContext.registerBean(Application.class, () -> App.this);
                    applicationContext.registerBean(Parameters.class, this::getParameters);
                    applicationContext.registerBean(HostServices.class, this::getHostServices);
                };

        applicationContext =
                new SpringApplicationBuilder()
                        .sources(GUI.class)
                        .initializers(initializer)
                        .run(getParameters().getRaw().toArray(new String[0]));
    }

    /**
     * @see Application
     */
    @Override
    public void stop() {
        applicationContext.close();
        SchedulerConfigurationHelper.close();

        Platform.exit();
    }

    /**
     * @see Application
     */
    @Override
    @SneakyThrows
    public void start(Stage stage) {
        mainStage.getContent().show();
    }
}
