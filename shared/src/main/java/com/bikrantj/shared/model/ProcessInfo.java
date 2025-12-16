package com.bikrantj.shared.model;


import java.time.Instant;

public class ProcessInfo {

    private String id;
    private String snapshotId;
    private String clientId;
    private String workspaceId;
    private String processName;
    private int processId;
    private long memoryUsage;
    private double cpuUsage;
    private String windowTitle;
    private Instant collectedAt;

    public ProcessInfo(
            String id,
            String snapshotId,
            String clientId,
            String workspaceId,
            String processName,
            int processId,
            long memoryUsage,
            double cpuUsage,
            String windowTitle,
            Instant collectedAt
    ) {
        this.id = id;
        this.snapshotId = snapshotId;
        this.clientId = clientId;
        this.workspaceId = workspaceId;
        this.processName = processName;
        this.processId = processId;
        this.memoryUsage = memoryUsage;
        this.cpuUsage = cpuUsage;
        this.windowTitle = windowTitle;
        this.collectedAt = collectedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(String snapshotId) {
        this.snapshotId = snapshotId;
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

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public int getProcessId() {
        return processId;
    }

    public void setProcessId(int processId) {
        this.processId = processId;
    }

    public long getMemoryUsage() {
        return memoryUsage;
    }

    public void setMemoryUsage(long memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

    public double getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public String getWindowTitle() {
        return windowTitle;
    }

    public void setWindowTitle(String windowTitle) {
        this.windowTitle = windowTitle;
    }

    public Instant getCollectedAt() {
        return collectedAt;
    }

    public void setCollectedAt(Instant collectedAt) {
        this.collectedAt = collectedAt;
    }
}
