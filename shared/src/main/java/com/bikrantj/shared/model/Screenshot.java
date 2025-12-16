package com.bikrantj.shared.model;


import java.time.Instant;

public class Screenshot {

    private String id;
    private String snapshotId;
    private String clientId;
    private String workspaceId;
    private String filePath;
    private long fileSize;
    private Instant captureTime;

    public Screenshot(
            String id,
            String snapshotId,
            String clientId,
            String workspaceId,
            String filePath,
            long fileSize,
            Instant captureTime
    ) {
        this.id = id;
        this.snapshotId = snapshotId;
        this.clientId = clientId;
        this.workspaceId = workspaceId;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.captureTime = captureTime;
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

    public Instant getCaptureTime() {
        return captureTime;
    }

    public void setCaptureTime(Instant captureTime) {
        this.captureTime = captureTime;
    }
    // getters only (immutability is fine here)
}
