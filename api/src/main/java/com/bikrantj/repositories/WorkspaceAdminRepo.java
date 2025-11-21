package com.bikrantj.repositories;

import com.bikrantj.db.DBConnection;
import com.bikrantj.models.WorkspaceAdmin;
import com.bikrantj.repositories.interfaces.IWorkspaceAdminRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class WorkspaceAdminRepo implements IWorkspaceAdminRepository {

    @Override
    public boolean save(WorkspaceAdmin admin) {
        String sql = "INSERT INTO workspace_admins (username, password_hash, email) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, admin.getUsername());
            stmt.setString(2, admin.getPasswordHash());
            stmt.setString(3, admin.getEmail());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Optional<WorkspaceAdmin> findByEmail(String email) {
        String sql = "SELECT * FROM workspace_admins WHERE email = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                WorkspaceAdmin admin = new WorkspaceAdmin();
                admin.setId(rs.getString("id"));
                admin.setUsername(rs.getString("username"));
                admin.setPasswordHash(rs.getString("password_hash"));
                admin.setEmail(rs.getString("email"));
                return Optional.of(admin);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }
}