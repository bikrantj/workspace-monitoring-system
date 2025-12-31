package com.bikrantj.shared.model;

public class Screenshot {

    private String id;
    private String snapshotId;
    private String clientId;
    private String workspaceId;
    private String filePath;
    private long fileSize;
    private String captureTime;       // ISO-8601 string or simple date string - your choice

    public Screenshot() {
    }

    public Screenshot(
            String id,
            String snapshotId,
            String clientId,
            String workspaceId,
            String filePath,
            long fileSize,
            String captureTime
    ) {
        this.id = id;
        this.snapshotId = snapshotId;
        this.clientId = clientId;
        this.workspaceId = workspaceId;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.captureTime = captureTime;
    }

    // Getters and setters

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

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getCaptureTime() {
        return captureTime != null ? captureTime : "";
    }

    public void setCaptureTime(String captureTime) {
        this.captureTime = captureTime;
    }
}