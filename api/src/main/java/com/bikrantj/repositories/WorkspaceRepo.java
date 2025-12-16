package com.bikrantj.repositories;


import com.bikrantj.shared.model.Workspace;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WorkspaceRepo {

    private final Connection con;

    public WorkspaceRepo(Connection con) {
        this.con = con;
    }

    public boolean create(Workspace workspace) {

        String sql = """
                                    INSERT INTO workspaces (
                                        name,
                                        description,
                                        admin_id,
                                        uniqueId,
                                        is_active
                                    ) VALUES (?, ?, ?, ?, ?)
                """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, workspace.getName());
            ps.setString(2, workspace.getDescription());
            ps.setString(3, workspace.getAdminId());
            ps.setString(4, workspace.getUniqueId());
            ps.setBoolean(5, workspace.isActive());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {

            // Duplicate workspace name for same admin
            if (e.getSQLState() != null && e.getSQLState().startsWith("23")) {
                System.err.println("Workspace name already exists for this admin.");
                return false;
            }

            e.printStackTrace();
            return false;
        }
    }

    public List<Workspace> findAllByAdminId(String adminId) {
        String sql = """
                SELECT * FROM workspaces
                WHERE admin_id = ? AND is_active = TRUE
                """;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, adminId);
            List<Workspace> workspaces = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Workspace workspace = new Workspace();
                    workspace.setId(rs.getString("id"));
                    workspace.setName(rs.getString("name"));
                    workspace.setDescription(rs.getString("description"));
                    workspace.setAdminId(rs.getString("admin_id"));
                    workspace.setActive(rs.getBoolean("is_active"));
                    workspace.setUniqueId(rs.getString("uniqueId"));
                    workspaces.add(workspace);
                }

                return workspaces;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Fetches an active workspace by its ID.
     */
    public Workspace findById(String workspaceId) {

        String sql = """
                    SELECT id, name, description, admin_id, is_active, uniqueId, created_at
                    FROM workspaces
                    WHERE uniqueId = ? AND is_active = TRUE
                """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, workspaceId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                Workspace workspace = new Workspace();
                workspace.setId(rs.getString("id"));
                workspace.setUniqueId(rs.getString("uniqueId"));
                workspace.setName(rs.getString("name"));
                workspace.setDescription(rs.getString("description"));
                workspace.setAdminId(rs.getString("admin_id"));
                workspace.setActive(rs.getBoolean("is_active"));

                return workspace;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
