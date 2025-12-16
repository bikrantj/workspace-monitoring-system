package com.bikrantj.services;


import com.bikrantj.repositories.WorkspaceRepo;
import com.bikrantj.shared.model.Workspace;

public class WorkspaceService {

    private final WorkspaceRepo workspaceRepo;

    public WorkspaceService(WorkspaceRepo workspaceRepo) {
        this.workspaceRepo = workspaceRepo;
    }

    /**
     * Creates a workspace after validating input.
     */
    public boolean createWorkspace(Workspace workspace) {

        if (workspace.getName() == null || workspace.getName().isBlank()) {
            return false;
        }

        if (workspace.getAdminId() == null || workspace.getAdminId().isBlank()) {
            return false;
        }

        return workspaceRepo.create(workspace);
    }

    /**
     * Validates workspace before client joins.
     */
    public Workspace validateWorkspace(String workspaceId) {

        if (workspaceId == null || workspaceId.isBlank()) {
            return null;
        }

        return workspaceRepo.findActiveById(workspaceId.trim());
    }
}
