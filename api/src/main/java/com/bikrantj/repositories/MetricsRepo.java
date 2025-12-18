package com.bikrantj.repositories;

import com.bikrantj.shared.responses.HighRamProcessUsage;
import com.bikrantj.shared.responses.WorkspaceActivityPoint;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MetricsRepo {

    private final Connection con;

    public MetricsRepo(Connection con) {
        this.con = con;
    }

    public List<WorkspaceActivityPoint> getWorkspaceActivityLast24Hours(
            String workspaceId
    ) {

        String sql = """
                    SELECT
                        DATE_FORMAT(collected_at, '%H:%i') AS time_bucket,
                        COUNT(DISTINCT snapshot_id) AS activity_count
                    FROM processes
                    WHERE workspace_id = ?
                      AND collected_at >= NOW() - INTERVAL 24 HOUR
                    GROUP BY time_bucket
                    ORDER BY time_bucket;
                """;

        List<WorkspaceActivityPoint> result = new ArrayList<>();

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, workspaceId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(
                            new WorkspaceActivityPoint(
                                    rs.getString("time_bucket"),
                                    rs.getInt("activity_count")
                            )
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public List<HighRamProcessUsage> getTopRamUsageProcessesForClient(
            String workspaceId,
            String clientId,
            int snapshotLimit,
            int topN
    ) {

        String sql = """
                  SELECT
                                        p.process_name              AS process_name,
                                        AVG(p.memory_usage)         AS avg_memory,
                                        AVG(p.cpu_usage)            AS avg_cpu
                                    FROM (
                                        SELECT id
                                        FROM monitoring_snapshots
                                        WHERE workspace_id = ?
                                          AND client_id = ?
                                        ORDER BY collected_at DESC
                                        LIMIT ?
                                    ) AS recent_snapshots
                                    JOIN processes p
                                         ON p.snapshot_id = recent_snapshots.id
                                    GROUP BY p.process_name
                                    ORDER BY avg_memory DESC
                                    LIMIT ?;
                
                
                """;

        List<HighRamProcessUsage> result = new ArrayList<>();

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, workspaceId);
            ps.setString(2, clientId);
            ps.setInt(3, snapshotLimit);
            ps.setInt(4, topN);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(
                            new HighRamProcessUsage(
                                    rs.getString("process_name"),
                                    rs.getLong("avg_memory"),
                                    rs.getDouble("avg_cpu")
                            )
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}