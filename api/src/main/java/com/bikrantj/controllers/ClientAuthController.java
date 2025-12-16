package com.bikrantj.controllers;

import com.bikrantj.services.ClientService;
import com.bikrantj.shared.model.Client;
import com.bikrantj.shared.requests.CreateClientRequest;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.util.Map;

public class ClientAuthController {

    private final ClientService clientService;

    public ClientAuthController(ClientService clientService) {
        this.clientService = clientService;
    }

    public void login(Context ctx) {
        CreateClientRequest req = ctx.bodyValidator(CreateClientRequest.class)
                .check(r -> r.getClientIdentifier() != null && !r.getClientIdentifier().trim().isEmpty(), "clientIdentifier is required")
                .check(r -> r.getClientName() != null && !r.getClientName().trim().isEmpty(), "clientName is required")
                .check(r -> r.getWorkspaceId() != null && !r.getWorkspaceId().trim().isEmpty(), "workspaceId is required")
                .check(r -> r.getLastIpAddress() != null && !r.getLastIpAddress().trim().isEmpty(), "lastIpAddress is required")
                .check(r -> r.getOsInfo() != null && !r.getOsInfo().trim().isEmpty(), "osInfo is required")
                .get();


        Client _client = new Client();

        _client.setClientIdentifier(req.getClientIdentifier().trim());
        _client.setClientName(req.getClientName().trim());
        _client.setWorkspaceId(req.getWorkspaceId().trim());
        _client.setLastIpAddress(req.getLastIpAddress().trim());
        _client.setOsInfo(req.getOsInfo().trim());

//        Check if client is already registered
        Client client = clientService.isClientInWorkspace(_client.getClientIdentifier(), _client.getWorkspaceId());

        if (client != null) {
            // Client already registered -> Login successful
            ctx.status(HttpStatus.OK).json(client);
        } else {
            boolean success = clientService.register(_client);
            if (success) {
                ctx.status(HttpStatus.CREATED)
                        .json(_client);
            } else {
                System.out.println("NOt success in client registration");
                ctx.status(HttpStatus.BAD_REQUEST)
                        .json(Map.of("error", "Failed to register client."));
            }

        }


    }
}
