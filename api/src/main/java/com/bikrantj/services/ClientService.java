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
            return false;
        }

        clientRepo.create(client);
//
//        Check if the workspace is valid before registering the client
        // Registration logic here
        return true;
    }

    public boolean isClientRegistered(String clientIdentifier) {
//        TODO: Check whether the client has been removed from workspace( Chceck relation).
        return clientRepo.existsByIdentifier(clientIdentifier);
    }
}
