package com.bikrantj.repositories;

import com.bikrantj.shared.model.ProcessInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.UUID;

public class ProcessRepo {

    private final Connection con;

    public ProcessRepo(Connection con) {
        this.con = con;
    }

    public void batchInsert(List<ProcessInfo> processes) {

        String sql = """
                    INSERT INTO processes (
                        id, snapshot_id, client_id, workspace_id,
                        process_name, process_id,
                        memory_usage, cpu_usage, window_title
                    ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            for (ProcessInfo p : processes) {
                ps.setString(1, UUID.randomUUID().toString());
                ps.setString(2, p.getSnapshotId());
                ps.setString(3, p.getClientId());
                ps.setString(4, p.getWorkspaceId());
                ps.setString(5, p.getProcessName());
                ps.setInt(6, p.getProcessId());
                ps.setLong(7, p.getMemoryUsage());
                ps.setDouble(8, p.getCpuUsage());
                ps.setString(9, p.getWindowTitle());
                ps.addBatch();
            }

            ps.executeBatch();

        } catch (Exception e) {
            throw new RuntimeException("Failed to insert processes", e);
        }
    }


}
