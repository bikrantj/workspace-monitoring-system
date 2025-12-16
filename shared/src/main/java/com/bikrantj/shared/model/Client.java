package com.bikrantj.shared.model;


import java.time.LocalDateTime;

public class Client {

    private String id;
    private String workspaceId;
    private String clientName;
    private String clientIdentifier;
    private String osInfo;
    private String lastIpAddress;
    private DeviceStatus status;
    private LocalDateTime lastHeartbeat;
    private LocalDateTime createdAt;

    public Client() {
        this.status = DeviceStatus.ONLINE;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    // ---------- Getters & Setters ----------

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

    public DeviceStatus getStatus() {
        return status;
    }

    public void setStatus(DeviceStatus status) {
        this.status = status;
    }

    public LocalDateTime getLastHeartbeat() {
        return lastHeartbeat;
    }

    public void setLastHeartbeat(LocalDateTime lastHeartbeat) {
        this.lastHeartbeat = lastHeartbeat;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // ---------- Domain Helpers ----------

    public void markOnline() {
        this.status = DeviceStatus.ONLINE;
        this.lastHeartbeat = LocalDateTime.now();
    }

    public void markOffline() {
        this.status = DeviceStatus.OFFLINE;
    }
}
