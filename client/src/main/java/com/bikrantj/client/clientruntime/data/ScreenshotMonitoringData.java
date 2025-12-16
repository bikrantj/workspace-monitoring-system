package com.bikrantj.client.clientruntime.data;

import java.time.Instant;

public class ScreenshotMonitoringData extends MonitoringData {

    private final byte[] imageBytes;

    public ScreenshotMonitoringData(
            String workspaceId,
            String clientIdentifier,
            Instant timestamp,
            byte[] imageBytes
    ) {
        super(workspaceId, clientIdentifier, timestamp);
        this.imageBytes = imageBytes;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }
}