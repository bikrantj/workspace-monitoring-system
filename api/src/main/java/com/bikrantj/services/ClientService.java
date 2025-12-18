package com.bikrantj.services;

import com.bikrantj.repositories.ClientRepo;
import com.bikrantj.shared.model.Client;
import com.bikrantj.shared.model.Workspace;

import java.util.List;

public class ClientService {
    private final WorkspaceService workspaceService;
    private final ClientRepo clientRepo;

    public ClientService(WorkspaceService workspaceService, ClientRepo clientRepo) {
        this.workspaceService = workspaceService;
        this.clientRepo = clientRepo;
    }


    public Client register(Client c) {
//        We don't need to check if client is already registered here, because the controller will handle that.
        Workspace workspace = workspaceService.validateWorkspace(c.getWorkspaceId());

        if (workspace == null) {
            System.out.println("Registering Client. Invalid workspace ID: " + c.getWorkspaceId());
            return null;
        }

        // change client's workspaceId to db workspace->id
//        c.setWorkspaceId(workspace.getId());
        System.out.println("Creating client in workspace: " + workspace.getName() + " (" + workspace.getId() + ")");
        //
//        Check if the workspace is valid before registering the client
        // Registration logic here
        return clientRepo.create(c);
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

    public List<Client> getAllClientsByWorkspace(String workspaceId) {
        Workspace workspace = workspaceService.validateWorkspace(workspaceId);

        if (workspace == null) {
            System.out.println("Fetching Clients. Invalid workspace ID: " + workspaceId);
            return null;
        }

        return clientRepo.findAllByWorkspace(workspaceId);
    }
}
