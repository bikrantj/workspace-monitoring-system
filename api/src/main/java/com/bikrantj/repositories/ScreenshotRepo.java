package com.bikrantj.repositories;

import com.bikrantj.shared.model.Screenshot;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.Instant;
import java.util.UUID;

public class ScreenshotRepo {

    private final Connection con;

    public ScreenshotRepo(Connection con) {
        this.con = con;
    }

    public Screenshot create(
            String snapshotId,
            String clientId,
            String workspaceId,
            String filePath,
            long fileSize
    ) {
        String id = UUID.randomUUID().toString();

        String sql = """
                    INSERT INTO screenshots (
                        id, snapshot_id, client_id, workspace_id,
                        file_path, file_size
                    ) VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.setString(2, snapshotId);
            ps.setString(3, clientId);
            ps.setString(4, workspaceId);
            ps.setString(5, filePath);
            ps.setLong(6, fileSize);
            ps.executeUpdate();

            return new Screenshot(
                    id,
                    snapshotId,
                    clientId,
                    workspaceId,
                    filePath,
                    fileSize,
                    Instant.now().toString()
            );

        } catch (Exception e) {
            throw new RuntimeException("Failed to save screenshot", e);
        }
    }
}