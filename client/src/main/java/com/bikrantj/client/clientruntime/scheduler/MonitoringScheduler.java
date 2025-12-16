package com.bikrantj.client.clientruntime.scheduler;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MonitoringScheduler {

    private final ScheduledExecutorService executor =
            Executors.newSingleThreadScheduledExecutor();

    private final Runnable task;
    private final long intervalSeconds;

    public MonitoringScheduler(Runnable task, long intervalSeconds) {
        this.task = task;
        this.intervalSeconds = intervalSeconds;
    }

    public void start() {
        executor.scheduleAtFixedRate(
                task, 0, intervalSeconds, TimeUnit.SECONDS);
    }

    public void stop() {
        executor.shutdownNow();
    }
}