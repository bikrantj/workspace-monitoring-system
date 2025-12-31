package com.bikrantj.shared.responses;

public class ClientListView {

    private String clientId;
    private String clientName;
    private String workspaceName;
    private String ipAddress;
    private String osInfo;
    private String status;
    private String lastHeartbeat;

    public ClientListView() {
    }

    public ClientListView(String clientId, String clientName, String workspaceName, String ipAddress, String osInfo, String status, String lastHeartbeat) {
        this.clientId = clientId;
        this.clientName = clientName;
        this.workspaceName = workspaceName;
        this.ipAddress = ipAddress;
        this.osInfo = osInfo;
        this.status = status;
        this.lastHeartbeat = lastHeartbeat;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getWorkspaceName() {
        return workspaceName;
    }

    public void setWorkspaceName(String workspaceName) {
        this.workspaceName = workspaceName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getOsInfo() {
        return osInfo;
    }

    public void setOsInfo(String osInfo) {
        this.osInfo = osInfo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLastHeartbeat() {
        return lastHeartbeat;
    }

    public void setLastHeartbeat(String lastHeartbeat) {
        this.lastHeartbeat = lastHeartbeat;
    }
    // Getters & setters
}