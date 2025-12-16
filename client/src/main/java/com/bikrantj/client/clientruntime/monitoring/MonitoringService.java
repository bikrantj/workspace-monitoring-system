package com.bikrantj.client.clientruntime.monitoring;

import com.bikrantj.client.clientruntime.collectors.DataCollector;
import com.bikrantj.client.clientruntime.data.MonitoringData;
import com.bikrantj.client.clientruntime.transport.MonitoringTransport;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class MonitoringService {

    private final MonitoringContext context;
    private final List<DataCollector<? extends MonitoringData>> collectors;
    private final MonitoringTransport transport;

    @SafeVarargs
    public MonitoringService(
            MonitoringContext context,
            MonitoringTransport transport,
            DataCollector<? extends MonitoringData>... collectors
    ) {
        this.context = context;
        this.transport = transport;
        this.collectors = List.of(collectors);
    }

    public void collectAndSend() {
        Instant timestamp = Instant.now();
        List<MonitoringData> collectedData = new ArrayList<>();

        for (DataCollector<? extends MonitoringData> collector : collectors) {
            try {
                MonitoringData data =
                        collector.collect(context, timestamp);
                collectedData.add(data);
            } catch (Exception e) {
                System.err.println("Collector failed: " + e.getMessage());
            }
        }

        transport.send(collectedData);
    }
}