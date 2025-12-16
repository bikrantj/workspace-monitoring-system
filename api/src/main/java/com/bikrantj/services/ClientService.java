package com.bikrantj.services;

import com.bikrantj.repositories.ClientRepo;
import com.bikrantj.shared.model.Client;
import com.bikrantj.shared.model.Workspace;

public class ClientService {
    private final WorkspaceService workspaceService;
    private final ClientRepo clientRepo;

    public ClientService(WorkspaceService workspaceService, ClientRepo clientRepo) {
        this.workspaceService = workspaceService;
        this.clientRepo = clientRepo;
    }


    public boolean register(Client client) {
//        We don't need to check if client is already registered here, because the controller will handle that.
        Workspace workspace = workspaceService.validateWorkspace(client.getWorkspaceId());

        if (workspace == null) {
            System.out.println("Registering Client. Invalid workspace ID: " + client.getWorkspaceId());
            return false;
        }

        // change client's workspaceId to db workspace->id
        client.setWorkspaceId(workspace.getId());
        System.out.println("Creating client in workspace: " + workspace.getName() + " (" + workspace.getId() + ")");
        clientRepo.create(client);
//
//        Check if the workspace is valid before registering the client
        // Registration logic here
        return true;
    }

    public Client isClientInWorkspace(String clientIdentifier, String workspaceId) {
//        TODO: Check whether the client has been removed from workspace( Chceck relation).
        Workspace workspace = workspaceService.validateWorkspace(workspaceId);

        if (workspace == null) {
            System.out.println("Registering Client. Invalid workspace ID: " + workspaceId);
            return null;
        }

        Client client = clientRepo.findByIdentifierAndWorkspace(clientIdentifier, workspaceId);
        if (client == null) {
            System.out.println("Client with identifier " + clientIdentifier + " not found in workspace " + workspaceId);
            return null;
        }
        return client;
    }
}
