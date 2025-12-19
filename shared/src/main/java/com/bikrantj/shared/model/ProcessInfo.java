package com.bikrantj.shared.model;


import java.sql.Timestamp;

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
    private Timestamp collectedAt;
    private double memoryUsageMB;

    public ProcessInfo() {
    }

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
            Timestamp collectedAt
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

    public ProcessInfo(
            int processId,
            String processName,
            long memoryUsage,
            double cpuUsage,
            String windowTitle
    ) {
        this.processId = processId;
        this.processName = processName;
        this.memoryUsage = memoryUsage;
        this.cpuUsage = cpuUsage;
        this.windowTitle = windowTitle;
    }

    public long getMemoryUsage() {
        return memoryUsage;
    }

    public void setMemoryUsage(long memoryUsage) {
        this.memoryUsage = memoryUsage;
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
        return processName != null ? processName : "";
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

    public double getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    //    Getter function for table display.
    public double getMemoryUsageMB() {
        if (memoryUsage <= 0) {
            return 0.0;
        }
        return Math.round((memoryUsage / (1024.0 * 1024.0)) * 100.0) / 100.0;
    }

    public void setMemoryUsageMB(double memoryUsageMB) {
        this.memoryUsageMB = memoryUsageMB;
    }

    public String getWindowTitle() {
        return windowTitle != null ? windowTitle : "";
    }

    public void setWindowTitle(String windowTitle) {
        this.windowTitle = windowTitle;
    }

    public Timestamp getCollectedAt() {
        return collectedAt;
    }

    public void setCollectedAt(Timestamp collectedAt) {
        this.collectedAt = collectedAt;
    }


}
