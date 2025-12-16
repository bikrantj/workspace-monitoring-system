package com.bikrantj.client.clientruntime.runtime;


import com.bikrantj.client.auth.ClientSession;
import com.bikrantj.client.clientruntime.collectors.ProcessCollector;
import com.bikrantj.client.clientruntime.collectors.ScreenshotCollector;
import com.bikrantj.client.clientruntime.monitoring.MonitoringContext;
import com.bikrantj.client.clientruntime.monitoring.MonitoringService;
import com.bikrantj.client.clientruntime.scheduler.MonitoringScheduler;
import com.bikrantj.client.clientruntime.transport.ApiMonitoringTransport;

public final class ClientRuntime {

    private static ClientRuntimeState state = ClientRuntimeState.STOPPED;
    private static MonitoringScheduler scheduler;

    private ClientRuntime() {
    }

    public static synchronized void start() {
        if (state == ClientRuntimeState.RUNNING) return;

        MonitoringContext context =
                new MonitoringContext(ClientSession.get());

        MonitoringService monitoringService =
                new MonitoringService(
                        context,
                        new ApiMonitoringTransport(context),
                        new ScreenshotCollector(),
                        new ProcessCollector()
                );

        scheduler = new MonitoringScheduler(
                monitoringService::collectAndSend,
                5 // seconds
        );

        scheduler.start();
        state = ClientRuntimeState.RUNNING;
    }

    public static synchronized void stop() {
        if (scheduler != null) {
            scheduler.stop();
            scheduler = null;
        }
        state = ClientRuntimeState.STOPPED;
    }
}
