package com.bikrantj.config;

import com.bikrantj.controllers.AuthController;
import com.bikrantj.controllers.ClientAuthController;
import com.bikrantj.controllers.MonitoringController;
import com.bikrantj.controllers.WorkspaceController;
import com.bikrantj.db.DBConnection;
import com.bikrantj.repositories.*;
import com.bikrantj.services.ClientService;
import com.bikrantj.services.MonitoringService;
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
        WorkspaceController workspaceController = new WorkspaceController(workspaceAdminService, workspaceService);
        ClientAuthController clientAuthController = new ClientAuthController(clientService);

        ScreenshotRepo screenshotRepo = new ScreenshotRepo(con);
        SnapshotRepo snapshotRepo = new SnapshotRepo(con);
        ProcessRepo processRepo = new ProcessRepo(con);
        MonitoringService monitoringService = new MonitoringService(snapshotRepo, screenshotRepo, processRepo);
        MonitoringController monitoringController = new MonitoringController(monitoringService, clientService);
        // Auth routes
        app.post("/auth/register", authController::register);
        app.post("/auth/login", authController::login);
        app.get("/auth/me", authController::getCurrentAdmin);

//        Client routes
        app.post("/client/login", clientAuthController::login);

//        Workspace routes
        app.post("/workspace/create", workspaceController::createWorkspace);
        app.get("/workspace", workspaceController::getWorkspaces);
// Client Monitoring:
        app.post("/monitoring/ingest", monitoringController::ingest);

        // Health check
        app.get("/health", ctx -> ctx.json(Map.of("status", "healthy")));
    }
}