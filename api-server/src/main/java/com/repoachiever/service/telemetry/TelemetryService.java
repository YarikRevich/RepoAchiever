package com.repoachiever.service.telemetry;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.SneakyThrows;
import org.json.JSONException;
import org.json.JSONObject;
import xyz.farhanfarooqui.JRocket.Client;
import xyz.farhanfarooqui.JRocket.JRocketServer;
import xyz.farhanfarooqui.JRocket.ServerListeners.OnClientConnectListener;
import xyz.farhanfarooqui.JRocket.ServerListeners.OnClientDisconnectListener;
import xyz.farhanfarooqui.JRocket.ServerListeners.OnReceiveListener;

import java.io.IOException;
import java.net.Socket;

/**
 * Provides access to gather information and expose it to telemetry representation tool.
 */
@ApplicationScoped
public class TelemetryService {
    private PrometheusMeterRegistry prometheusRegistry;

    /**
     *
     */
    @PostConstruct
    public void configure() {
        this.prometheusRegistry =
                new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);

        Metrics.globalRegistry.add(prometheusRegistry);

//        prometheusRegistry.scrape();

//        new Socket()
    }

//
//
//
//    private Socket clientSocket;
//    private PrintWriter out;
//    private BufferedReader in;
//
//    public void startConnection(String ip, int port) {
//        clientSocket = new Socket(ip, port);
//        out = new PrintWriter(clientSocket.getOutputStream(), true);
//        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//    }

}
