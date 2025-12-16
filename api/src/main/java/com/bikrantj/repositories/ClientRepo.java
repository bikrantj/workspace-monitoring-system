package com.bikrantj.repositories;

import com.bikrantj.shared.model.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientRepo {
    private final Connection con;

    public ClientRepo(Connection con) {
        this.con = con;
    }

    public boolean create(Client client) {

        String sql = """
                    INSERT INTO clients (
                        workspace_id,
                        client_name,
                        client_identifier,
                        os_info,
                        last_ip_address,
                        status
                    ) VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, client.getWorkspaceId());
            ps.setString(2, client.getClientName());
            ps.setString(3, client.getClientIdentifier());
            ps.setString(4, client.getOsInfo());
            ps.setString(5, client.getLastIpAddress());
            ps.setString(6, client.getStatus().name().toLowerCase());

            int affectedRows = ps.executeUpdate();
            return affectedRows == 1;

        } catch (SQLException e) {

            // Duplicate client (same MAC / identifier)
            if (e.getSQLState() != null && e.getSQLState().startsWith("23")) {
                System.err.println("Client already registered with this identifier.");
                return false;
            }

            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks if a client already exists by its unique identifier.
     */
    public boolean existsByIdentifier(String clientIdentifier) {

        String sql = """
                    SELECT 1 FROM clients WHERE client_identifier = ?
                """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, clientIdentifier);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
