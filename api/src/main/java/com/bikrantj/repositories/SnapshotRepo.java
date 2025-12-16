package com.bikrantj.repositories;

import com.bikrantj.shared.model.MonitoringSnapshot;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.Instant;
import java.util.UUID;

public class SnapshotRepo {

    private final Connection con;

    public SnapshotRepo(Connection con) {
        this.con = con;
    }

    public MonitoringSnapshot create(String clientId, String workspaceId) {
        String id = UUID.randomUUID().toString();

        String sql = """
                    INSERT INTO monitoring_snapshots (id, client_id, workspace_id)
                    VALUES (?, ?, ?)
                """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.setString(2, clientId);
            ps.setString(3, workspaceId);
            ps.executeUpdate();

            return new MonitoringSnapshot(
                    id,
                    clientId,
                    workspaceId,
                    Instant.now()
            );

        } catch (Exception e) {
            throw new RuntimeException("Failed to create snapshot", e);
        }
    }
}