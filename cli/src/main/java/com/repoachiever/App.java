package com.repoachiever;

import com.repoachiever.entity.PropertiesEntity;
import com.repoachiever.service.client.content.download.DownloadContentClientService;
import com.repoachiever.service.client.info.version.VersionInfoClientService;
import com.repoachiever.service.command.BaseCommandService;
import com.repoachiever.service.command.external.clean.CleanExternalCommandService;
import com.repoachiever.service.command.external.cleanall.CleanAllExternalCommandService;
import com.repoachiever.service.command.external.content.ContentExternalCommandService;
import com.repoachiever.service.command.external.download.DownloadExternalCommandService;
import com.repoachiever.service.command.external.apply.ApplyExternalCommandService;
import com.repoachiever.service.command.external.topology.TopologyExternalCommandService;
import com.repoachiever.service.command.external.withdraw.WithdrawExternalCommandService;
import com.repoachiever.service.command.external.version.VersionExternalCommandService;
import com.repoachiever.service.command.internal.health.HealthCheckInternalCommandService;
import com.repoachiever.service.config.ConfigService;
import com.repoachiever.service.visualization.VisualizationService;
import com.repoachiever.service.visualization.label.apply.ApplyCommandVisualizationLabel;
import com.repoachiever.service.visualization.label.clean.CleanCommandVisualizationLabel;
import com.repoachiever.service.visualization.label.cleanall.CleanAllCommandVisualizationLabel;
import com.repoachiever.service.visualization.label.content.ContentCommandVisualizationLabel;
import com.repoachiever.service.visualization.label.download.DownloadCommandVisualizationLabel;
import com.repoachiever.service.visualization.label.topology.TopologyCommandVisualizationLabel;
import com.repoachiever.service.visualization.label.withdraw.WithdrawCommandVisualizationLabel;
import com.repoachiever.service.visualization.label.version.VersionCommandVisualizationLabel;
import com.repoachiever.service.visualization.state.VisualizationState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

/**
 * Represents initialization point for the RepoAchiever CLI application.
 */
@Component
@Import({
        BaseCommandService.class,
        ApplyExternalCommandService.class,
        WithdrawExternalCommandService.class,
        CleanExternalCommandService.class,
        CleanAllExternalCommandService.class,
        ContentExternalCommandService.class,
        DownloadExternalCommandService.class,
        TopologyExternalCommandService.class,
        VersionExternalCommandService.class,
        VersionExternalCommandService.class,
        HealthCheckInternalCommandService.class,
        ApplyExternalCommandService.class,
        WithdrawExternalCommandService.class,
        BuildProperties.class,
        PropertiesEntity.class,
        ConfigService.class,
        ApplyCommandVisualizationLabel.class,
        WithdrawCommandVisualizationLabel.class,
        CleanCommandVisualizationLabel.class,
        CleanAllCommandVisualizationLabel.class,
        ContentCommandVisualizationLabel.class,
        DownloadCommandVisualizationLabel.class,
        TopologyCommandVisualizationLabel.class,
        VersionCommandVisualizationLabel.class,
        VisualizationService.class,
        VisualizationState.class
})
public class App implements ApplicationRunner, ExitCodeGenerator {
    private int exitCode;

    @Autowired
    private BaseCommandService baseCommandService;

    @Override
    public void run(ApplicationArguments args) {
        CommandLine cmd = new CommandLine(baseCommandService);

        exitCode = cmd.execute(args.getSourceArgs());
    }

    @Override
    public int getExitCode() {
        return exitCode;
    }
}
