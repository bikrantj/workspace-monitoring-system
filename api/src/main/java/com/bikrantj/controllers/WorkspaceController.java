package com.bikrantj.controllers;

import com.bikrantj.services.WorkspaceAdminService;
import com.bikrantj.services.WorkspaceService;
import com.bikrantj.shared.dto.User;
import com.bikrantj.shared.model.Workspace;
import com.bikrantj.shared.requests.CreateWorkspaceRequest;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.util.Map;

public class WorkspaceController {
    private final WorkspaceAdminService adminService;
    private final WorkspaceService workspaceService;

    public WorkspaceController(WorkspaceAdminService adminService, WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
        this.adminService = adminService;
    }

    public void getWorkspaces(Context ctx) {
        User u = adminService.getCurrentAdmin(ctx);

        if (u == null) {
            ctx.status(HttpStatus.UNAUTHORIZED)
                    .json(Map.of("error", "Unauthorized"));
            return;
        }

        var workspaces = workspaceService.getWorkspacesByAdminId(u.id());

        ctx.status(HttpStatus.OK)
                .json(workspaces);
    }

    public void createWorkspace(Context ctx) {
        // Implementation for creating a workspace
//        TODO: Grab the adminId from the JWT token instead of the request body

        User u = adminService.getCurrentAdmin(ctx);

        if (u == null) {
            ctx.status(HttpStatus.UNAUTHORIZED)
                    .json(Map.of("error", "Unauthorized"));
            return;
        }

        System.out.println("Validating CreateWorkspaceRequest...");

        CreateWorkspaceRequest req = ctx.bodyValidator(CreateWorkspaceRequest.class)
                .check(r -> r.getName() != null && !r.getName().trim().isEmpty(), "Name is required.")
                .check(r -> r.getUniqueId() != null && !r.getUniqueId().trim().isEmpty(), "Workspace ID is required.")
                .check(r -> r.getDescription() != null && !r.getDescription().trim().isEmpty(), "Description is required.")
                .get();

        Workspace workspace = new Workspace();

        workspace.setName(req.getName().trim());
        workspace.setDescription(req.getDescription().trim());
        workspace.setUniqueId(req.getUniqueId().trim());
        workspace.setAdminId(u.id());
        workspace.setActive(true);


        boolean success = workspaceService.createWorkspace(
                workspace
        );
        if (success) {
            ctx.status(HttpStatus.CREATED)
                    .json(Map.of("message", "Workspace created successfully"));
        } else {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json(Map.of("error", "Workspace creation failed"));
        }
    }

}
