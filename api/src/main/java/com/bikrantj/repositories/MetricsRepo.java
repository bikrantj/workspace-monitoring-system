package com.bikrantj.repositories;

import com.bikrantj.shared.model.ProcessInfo;
import com.bikrantj.shared.model.Screenshot;
import com.bikrantj.shared.responses.ActivityPoint;
import com.bikrantj.shared.responses.HighRamProcessUsage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MetricsRepo {

    private final Connection con;

    public MetricsRepo(Connection con) {
        this.con = con;
    }


    public List<ActivityPoint> getWorkspaceActivityLast24Hours(
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

        List<ActivityPoint> result = new ArrayList<>();

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, workspaceId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(
                            new ActivityPoint(
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

    public List<ProcessInfo> getLatestProcessesForClient(String workspaceId, String clientId) {
        String sql = """
                SELECT 
                    p.id,
                    p.snapshot_id AS snapshotId,
                    p.client_id AS clientId,
                    p.workspace_id AS workspaceId,
                    p.process_name AS processName,
                    p.process_id AS processId,
                    p.memory_usage AS memoryUsage,
                    p.cpu_usage AS cpuUsage,
                    p.window_title AS windowTitle,
                    p.collected_at AS collectedAt
                FROM processes p
                JOIN monitoring_snapshots ms ON p.snapshot_id = ms.id
                WHERE ms.workspace_id = ?
                  AND ms.client_id = ?
                  AND ms.id = (
                      SELECT id 
                      FROM monitoring_snapshots 
                      WHERE workspace_id = ? 
                        AND client_id = ?
                      ORDER BY collected_at DESC 
                      LIMIT 1
                  )
                ORDER BY p.memory_usage DESC;
                """;

        List<ProcessInfo> result = new ArrayList<>();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, workspaceId);
            ps.setString(2, clientId);
            ps.setString(3, workspaceId);
            ps.setString(4, clientId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ProcessInfo info = new ProcessInfo();
                    info.setId(rs.getString("id"));
                    info.setSnapshotId(rs.getString("snapshotId"));
                    info.setClientId(rs.getString("clientId"));
                    info.setWorkspaceId(rs.getString("workspaceId"));
                    info.setProcessName(rs.getString("processName"));
                    info.setProcessId(rs.getInt("processId"));
                    System.out.println("IN REPOSITORY: MEMORY USAGE = " + rs.getLong("memoryUsage"));
                    info.setMemoryUsage(rs.getLong("memoryUsage"));
                    info.setCpuUsage(rs.getDouble("cpuUsage"));
                    info.setWindowTitle(rs.getString("windowTitle"));
                    // Handle timestamp if needed
                    Timestamp ts = rs.getTimestamp("collectedAt");
                    info.setCollectedAt(ts);

                    result.add(info);
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

    public List<ActivityPoint> getClientActivityLast24Hours(
            String workspaceId,
            String clientId
    ) {

        String sql = """
                    SELECT
                        DATE_FORMAT(collected_at, '%H:%i') AS time_bucket,
                        COUNT(*) AS activity_count
                    FROM monitoring_snapshots
                    WHERE workspace_id = ?
                      AND client_id = ?
                      AND collected_at >= NOW() - INTERVAL 24 HOUR
                    GROUP BY time_bucket
                    ORDER BY time_bucket;
                """;

        List<ActivityPoint> result = new ArrayList<>();

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, workspaceId);
            ps.setString(2, clientId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(
                            new ActivityPoint(
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

    public Screenshot getLatestScreenshotForClient(String workspaceId, String clientId) {
        String sql = """
                SELECT 
                    id,
                    client_id AS clientId,
                    workspace_id AS workspaceId,
                    file_path AS filePath,
                    file_size AS fileSize
                FROM screenshots
                WHERE workspace_id = ?
                  AND client_id = ?
                ORDER BY capture_time DESC
                LIMIT 1;
                """;

        Screenshot screenshot = null;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, workspaceId);
            ps.setString(2, clientId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    screenshot = new Screenshot();
                    screenshot.setId(rs.getString("id"));
                    screenshot.setClientId(rs.getString("clientId"));
                    screenshot.setWorkspaceId(rs.getString("workspaceId"));
                    screenshot.setFilePath(rs.getString("filePath"));
                    screenshot.setFileSize(rs.getLong("fileSize"));
                }
            }
            return screenshot;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}