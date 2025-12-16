package com.bikrantj.shared.model;


import java.time.Instant;

public class MonitoringSnapshot {

    private String id;
    private String clientId;
    private String workspaceId;
    private Instant collectedAt;

    public MonitoringSnapshot(
            String id,
            String clientId,
            String workspaceId,
            Instant collectedAt
    ) {
        this.id = id;
        this.clientId = clientId;
        this.workspaceId = workspaceId;
        this.collectedAt = collectedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(String workspaceId) {
        this.workspaceId = workspaceId;
    }

    public Instant getCollectedAt() {
        return collectedAt;
    }

    public void setCollectedAt(Instant collectedAt) {
        this.collectedAt = collectedAt;
    }
}

