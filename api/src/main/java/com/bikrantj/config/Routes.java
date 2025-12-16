package com.bikrantj.config;

import com.bikrantj.controllers.AdminController;
import com.bikrantj.controllers.AuthController;
import com.bikrantj.controllers.ClientAuthController;
import com.bikrantj.db.DBConnection;
import com.bikrantj.repositories.ClientRepo;
import com.bikrantj.repositories.WorkspaceAdminRepo;
import com.bikrantj.repositories.WorkspaceRepo;
import com.bikrantj.services.ClientService;
import com.bikrantj.services.WorkspaceAdminService;
import com.bikrantj.services.WorkspaceService;
import io.javalin.Javalin;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class Routes {
    public static void configure(Javalin app) throws SQLException {
        // Initialize dependencies
        Connection con = DBConnection.getConnection();

        WorkspaceAdminRepo adminRepo = new WorkspaceAdminRepo(con);
        WorkspaceRepo workspaceRepo = new WorkspaceRepo(con);
        ClientRepo clientRepo = new ClientRepo(con);

        WorkspaceService workspaceService = new WorkspaceService(workspaceRepo);
        WorkspaceAdminService workspaceAdminService = new WorkspaceAdminService(adminRepo, workspaceService);
        ClientService clientService = new ClientService(workspaceService, clientRepo);

        AuthController authController = new AuthController(workspaceAdminService);
        AdminController adminController = new AdminController(workspaceAdminService);
        ClientAuthController clientAuthController = new ClientAuthController(clientService);

        // Auth routes
        app.post("/auth/register", authController::register);
        app.post("/auth/login", authController::login);
        app.get("/auth/me", authController::getCurrentAdmin);

//        Client routes
        app.post("/client/login", clientAuthController::login);

//        Workspace routes
        app.post("/workspace/create", adminController::createWorkspace);

        // Health check
        app.get("/health", ctx -> ctx.json(Map.of("status", "healthy")));
    }
}