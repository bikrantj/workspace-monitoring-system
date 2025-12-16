package com.bikrantj.shared.requests;

public class CreateClientRequest {
    private String workspaceId;
    private String clientName;
    private String clientIdentifier;
    private String osInfo;
    private String lastIpAddress;

    public CreateClientRequest() {
    }

    public CreateClientRequest(String workspaceId, String clientName, String clientIdentifier, String osInfo, String lastIpAddress) {
        this.workspaceId = workspaceId;
        this.clientName = clientName;
        this.clientIdentifier = clientIdentifier;
        this.osInfo = osInfo;
        this.lastIpAddress = lastIpAddress;
    }

    public String getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(String workspaceId) {
        this.workspaceId = workspaceId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientIdentifier() {
        return clientIdentifier;
    }

    public void setClientIdentifier(String clientIdentifier) {
        this.clientIdentifier = clientIdentifier;
    }

    public String getOsInfo() {
        return osInfo;
    }

    public void setOsInfo(String osInfo) {
        this.osInfo = osInfo;
    }

    public String getLastIpAddress() {
        return lastIpAddress;
    }

    public void setLastIpAddress(String lastIpAddress) {
        this.lastIpAddress = lastIpAddress;
    }
}


/*
* workspace_id      VARCHAR(36)         NOT NULL,
    client_name       VARCHAR(100)        NOT NULL,          -- Name given by admin to identify this computer
    client_identifier VARCHAR(255) UNIQUE NOT NULL,          -- Unique machine ID (MAC address or generated UUID)
    os_info           VARCHAR(255),
    last_ip_address   VARCHAR(45),
* */