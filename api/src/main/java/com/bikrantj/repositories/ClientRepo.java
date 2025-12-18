package com.bikrantj.repositories;

import com.bikrantj.shared.model.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientRepo {
    private final Connection con;

    public ClientRepo(Connection con) {
        this.con = con;
    }

    public Client create(Client client) {

        String sql = """
                    INSERT INTO clients (
                        workspace_id,
                        client_name,
                        client_identifier,
                        os_info,
                        last_ip_address,
                        status
                    ) VALUES (?, ?, ?, ?, ?, ?)
                    ON DUPLICATE KEY UPDATE
                                    workspace_id     = VALUES(workspace_id),
                                    client_name      = VALUES(client_name),
                                    os_info           = VALUES(os_info),
                                    last_ip_address  = VALUES(last_ip_address),
                                    status            = VALUES(status)
                """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, client.getWorkspaceId());
            ps.setString(2, client.getClientName());
            ps.setString(3, client.getClientIdentifier());
            ps.setString(4, client.getOsInfo());
            ps.setString(5, client.getLastIpAddress());
            ps.setString(6, client.getStatus().name().toLowerCase());

            int affectedRows = ps.executeUpdate();
            System.out.println("Everything ok till here. Affected rows: " + affectedRows);
            return findByIdentifierAndWorkspace(client.getClientIdentifier(), client.getWorkspaceId());

        } catch (SQLException e) {
            System.out.println("Error SQL State: " + e.getSQLState());
            // Duplicate client (same MAC / identifier)
            if (e.getSQLState() != null && e.getSQLState().startsWith("23")) {
                System.out.println(e.getMessage());
                System.err.println("Client already registered with this identifier.");
                return null;
            }

            e.printStackTrace();
            return null;
        }
    }

    /**
     * Checks if a client already exists by its unique identifier.
     */
    public Client findByIdentifierAndWorkspace(String clientIdentifier, String workspaceId) {

        String sql = """
                SELECT
                    *
                FROM clients
                WHERE client_identifier = ? AND workspace_id = ?
                """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, clientIdentifier);
            ps.setString(2, workspaceId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Client client = new Client(

                    );
                    client.setWorkspaceId(rs.getString("workspace_id"));
                    client.setId(rs.getString("id"));
                    client.setClientName(
                            rs.getString("client_name"));
                    client.setClientIdentifier(
                            rs.getString("client_identifier"));
                    client.setOsInfo(
                            rs.getString("os_info"));
                    client.setLastIpAddress(
                            rs.getString("last_ip_address"));

                    return (client);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Client> findAllByWorkspace(String workspaceId) {
        String sql = """
                SELECT
                    *
                FROM clients
                WHERE workspace_id = ?
                """;

        try (PreparedStatement ps = con.prepareStatement(sql);) {

            ps.setString(1, workspaceId);
            List<Client> clients = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Client client = new Client();
                    client.setWorkspaceId(rs.getString("workspace_id"));
                    client.setId(rs.getString("id"));
                    client.setClientName(
                            rs.getString("client_name"));
                    client.setClientIdentifier(
                            rs.getString("client_identifier"));
                    client.setOsInfo(
                            rs.getString("os_info"));
                    client.setLastIpAddress(
                            rs.getString("last_ip_address"));
                    clients.add(client);
                }
//                return clients;
                // Implementation goes here
            }
            return clients;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Implementation goes here
        return null;
    }
}
