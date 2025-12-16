package com.bikrantj.client.clientruntime.monitoring;

import com.bikrantj.shared.model.Client;

public class MonitoringContext {

    private final Client client;

    public MonitoringContext(Client client) {
        this.client = client;
    }

    public String getWorkspaceId() {
        return client.getWorkspaceId();
    }

    public String getClientIdentifier() {
        return client.getClientIdentifier();
    }

    public String getClientId() {
        return client.getId();
    }
}