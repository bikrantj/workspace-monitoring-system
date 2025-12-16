package com.bikrantj.client.clientruntime.data;

import java.time.Instant;

public abstract class MonitoringData {

    public final Instant timestamp;
    public final String workspaceId;
    public final String clientIdentifier;

    protected MonitoringData(
            String workspaceId,
            String clientIdentifier,
            Instant timestamp
    ) {
        this.workspaceId = workspaceId;
        this.clientIdentifier = clientIdentifier;
        this.timestamp = timestamp;
    }
}