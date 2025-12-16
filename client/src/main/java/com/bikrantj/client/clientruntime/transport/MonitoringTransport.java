package com.bikrantj.client.clientruntime.transport;

import com.bikrantj.client.clientruntime.data.MonitoringData;

import java.util.List;

public interface MonitoringTransport {
    void send(List<MonitoringData> data);
}
