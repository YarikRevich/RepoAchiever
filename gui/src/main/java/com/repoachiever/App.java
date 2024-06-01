package com.repoachiever;

import com.repoachiever.service.integration.apiserver.healthcheck.ApiServerHealthCheckService;
import com.repoachiever.service.element.font.FontLoader;
import com.repoachiever.service.integration.element.ElementConfigService;
import com.repoachiever.service.element.stage.MainStage;
import com.repoachiever.service.integration.event.EventConfigService;
import com.repoachiever.service.scheduler.SchedulerConfigurationHelper;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Represents initialization point for the RepoAchiever GUI application.
 */
@Component
public class App extends Application {
    private ConfigurableApplicationContext applicationContext;

    @Autowired
    private ApiServerHealthCheckService apiServerHealthCheckService;

    @Autowired
    private ElementConfigService elementConfigService;

    @Autowired
    private EventConfigService eventConfigService;

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
