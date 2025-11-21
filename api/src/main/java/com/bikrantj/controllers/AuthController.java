package com.bikrantj.controllers;


import com.bikrantj.models.WorkspaceAdmin;
import com.bikrantj.services.WorkspaceAdminService;
import io.javalin.http.Context;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AuthController {
    private final WorkspaceAdminService adminService;

    public AuthController(WorkspaceAdminService adminService) {
        this.adminService = adminService;
    }

    public void register(Context ctx) {
        try {
            Map<String, String> body = ctx.bodyAsClass(Map.class);
            String username = body.get("username");
            String password = body.get("password");
            String email = body.get("email");

            if (username == null || password == null || email == null) {
                ctx.status(400).json(Map.of("error", "Username, password and email are required"));
                return;
            }

            boolean success = adminService.register(username, password, email);

            if (success) {
                ctx.status(201).json(Map.of("message", "Admin registered successfully"));
            } else {
                ctx.status(400).json(Map.of("error", "Email already exists"));
            }

        } catch (Exception e) {
            ctx.status(500).json(Map.of("error", "Registration failed"));
        }
    }

    public void login(Context ctx) {
        try {
            Map<String, String> body = ctx.bodyAsClass(Map.class);
            String email = body.get("email");
            String password = body.get("password");

            if (email == null || password == null) {
                ctx.status(400).json(Map.of("error", "Email and password are required"));
                return;
            }

            Optional<WorkspaceAdmin> adminOpt = adminService.login(email, password);

            if (adminOpt.isPresent()) {
                WorkspaceAdmin admin = adminOpt.get();
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Login successful");
                response.put("admin", Map.of(
                        "id", admin.getId(),
                        "username", admin.getUsername(),
                        "email", admin.getEmail()
                ));
                ctx.json(response);
            } else {
                ctx.status(401).json(Map.of("error", "Invalid email or password"));
            }

        } catch (Exception e) {
            ctx.status(500).json(Map.of("error", "Login failed"));
        }
    }
}