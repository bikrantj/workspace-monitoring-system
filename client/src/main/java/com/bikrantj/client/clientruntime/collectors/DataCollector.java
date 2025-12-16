package com.bikrantj.client.clientruntime.collectors;


import com.bikrantj.client.clientruntime.data.MonitoringData;
import com.bikrantj.client.clientruntime.monitoring.MonitoringContext;

import java.time.Instant;

public interface DataCollector<T extends MonitoringData> {
    T collect(MonitoringContext context, Instant timestamp) throws Exception;
}