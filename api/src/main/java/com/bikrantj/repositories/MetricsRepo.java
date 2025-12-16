package com.bikrantj.repositories;

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
}