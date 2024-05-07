package com.repoachiever.service.command.prometheus;

import com.repoachiever.service.command.prometheus.common.PrometheusConfigurationHelper;
import process.SProcess;
import process.SProcessExecutor;

/**
 * Represents Prometheus deployment command.
 */
public class DeployCommandService extends SProcess {
    private final String command;
    private final SProcessExecutor.OS osType;

    public DeployCommandService(
            String name, String image, Integer port, String configLocation, String internalLocation) {
        this.osType = SProcessExecutor.getCommandExecutor().getOSType();

        this.command = switch (osType) {
            case WINDOWS -> null;
            case UNIX, MAC, ANY -> String.format(
                    "docker run -d %s %s --name %s %s %s %s",
                    PrometheusConfigurationHelper.getDockerVolumes(configLocation, internalLocation),
                    PrometheusConfigurationHelper.getDockerPorts(port),
                    name,
                    image,
                    PrometheusConfigurationHelper.getDockerCommandArguments(),
                    PrometheusConfigurationHelper.getDockerCommandOptions());
        };

        System.out.println(command);
    }

    @Override
    public String getCommand() {
        return command;
    }

    @Override
    public SProcessExecutor.OS getOSType() {
        return osType;
    }
}

//prometheus:
//        image: prom/prometheus:v2.36.2
//        volumes:
//        - ./prometheus/:/etc/prometheus/
//        - prometheus_data:/prometheus
//        command:
//        - '--config.file=/etc/prometheus/prometheus.yml'
//        - '--storage.tsdb.path=/prometheus'
//        - '--web.console.libraries=/usr/share/prometheus/console_libraries'
//        - '--web.console.templates=/usr/share/prometheus/consoles'
//        - '--web.enable-lifecycle'
//        - '--web.enable-admin-api'
//        ports:
//        - 9090:9090