package com.bikrantj.controllers;

import com.bikrantj.services.ClientService;
import com.bikrantj.shared.model.Client;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
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
