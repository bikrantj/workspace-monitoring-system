package com.bikrantj.config;

import com.bikrantj.controllers.AuthController;
import com.bikrantj.repositories.WorkspaceAdminRepo;
import com.bikrantj.services.WorkspaceAdminService;
import io.javalin.Javalin;

import java.util.Map;

public class Routes {
    public static void configure(Javalin app) {
        // Initialize dependencies
        WorkspaceAdminRepo adminRepo = new WorkspaceAdminRepo();
        WorkspaceAdminService adminService = new WorkspaceAdminService(adminRepo);
        AuthController authController = new AuthController(adminService);

        // Auth routes
        app.post("/auth/register", authController::register);
        app.post("/auth/login", authController::login);
        app.get("/auth/me", authController::getCurrentAdmin);

        // Health check
        app.get("/health", ctx -> ctx.json(Map.of("status", "healthy")));
    }
}