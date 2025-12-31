package com.bikrantj.controllers;

import com.bikrantj.services.ClientService;
import com.bikrantj.services.WorkspaceAdminService;
import com.bikrantj.shared.dto.User;
import com.bikrantj.shared.model.Client;
import com.bikrantj.shared.requests.DeleteClientRequest;
import com.bikrantj.shared.responses.ClientListView;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ClientController {

    private final ClientService clientService;
    private final WorkspaceAdminService adminService;

    public ClientController(ClientService clientService, WorkspaceAdminService adminService) {
        this.adminService = adminService;
        this.clientService = clientService;
    }

    public void getClientByIdentifier(Context ctx) {

        String workspaceId = ctx.pathParam("workspaceId");
        String clientIdentifier = ctx.pathParam("clientIdentifier");
        System.out.println("Server: Fetching client with identifier: " + clientIdentifier + " in workspace: " + workspaceId);

        if (workspaceId.isBlank() || clientIdentifier.isBlank()) {
            ctx.status(400).json("workspaceId is required");
        }
        System.out.println("Server: Fetching client with identifier: " + clientIdentifier + " in workspace: " + workspaceId);

        Client client = clientService.isClientInWorkspace(clientIdentifier, workspaceId);

        if (client != null) {
            ctx.status(200).json(client);
        } else {
            ctx.status(404).json("Client not found");
        }
        // Implementation goes here
    }

    public void getAllDevices(Context ctx) {

        User admin = adminService.getCurrentAdmin(ctx);

        if (admin == null) {
            ctx.status(401).json("Unauthorized");
            return;
        }

        List<ClientListView> devices = clientService.getAllDevicesForAdmin(admin.id());
        System.out.println("Devices for admin " + admin.id() + ": " + devices.size());
        for (ClientListView device : devices) {
            System.out.println("[Device] ID: " + device.getClientId() + ", Name: " + device.getClientName());
        }
        ctx.status(200).json(devices);
    }

    public void deleteDevice(Context ctx) {
        System.out.println("Server: Received request to delete device");
        User admin = adminService.getCurrentAdmin(ctx);

        if (admin == null) {
            ctx.status(401).json("Unauthorized");
            return;
        }

        DeleteClientRequest req = ctx.bodyAsClass(DeleteClientRequest.class);
        String clientId = req.getClientId();


        if (clientId == null || clientId.isBlank()) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Client ID is required");
            return;

        }

        boolean deleted =
                clientService.deleteClientForAdmin(clientId, admin.id());

        if (deleted) {
            ctx.status(204);
        } else {
            ctx.status(404).json("Device not found or not owned by admin");
        }
    }

    public void getAllClientsByWorkspace(@NotNull Context context) {
        String workspaceId = context.pathParam("workspaceId");
        System.out.println("Server: Fetching all clients in workspace: " + workspaceId);

        if (workspaceId.isBlank()) {
            context.status(400).json("workspaceId is required");
            return;
        }

        List<Client> clients = clientService.getAllClientsByWorkspace(workspaceId);
        System.out.println("Server: Found " + clients.size() + " clients in workspace: " + workspaceId);
        for (Client client : clients) {
            System.out.println("[Server] Client ID: " + client.getId() + ", Identifier: " + client.getClientName());
        }

        context.status(200).json(clients);
    }
}
