package com.bikrantj.shared.requests;

import java.util.List;

public class MonitoringPayload {

    private String clientId;
    private String workspaceId;
    private ScreenshotPayload screenshot;
    private List<ProcessPayload> processes;

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

    public ScreenshotPayload getScreenshot() {
        return screenshot;
    }

    public void setScreenshot(ScreenshotPayload screenshot) {
        this.screenshot = screenshot;
    }

    public List<ProcessPayload> getProcesses() {
        return processes;
    }

    public void setProcesses(List<ProcessPayload> processes) {
        this.processes = processes;
    }
}
