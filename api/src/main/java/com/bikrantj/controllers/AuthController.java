package com.bikrantj.controllers;

import com.bikrantj.models.WorkspaceAdmin;
import com.bikrantj.services.WorkspaceAdminService;
import com.bikrantj.shared.requests.LoginRequest;
import com.bikrantj.shared.requests.RegisterRequest;
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
            // Check if body is empty
            String bodyString = ctx.body();
            if (bodyString.trim().isEmpty()) {
                ctx.status(400).json(Map.of("error", "Request body is required"));
                return;
            }

            RegisterRequest request = ctx.bodyAsClass(RegisterRequest.class);

            // Validate required fields
            if (request.getUsername().trim().isEmpty()) {
                ctx.status(400).json(Map.of("error", "Username is required"));
                return;
            }

            if (request.getPassword().trim().isEmpty()) {
                ctx.status(400).json(Map.of("error", "Password is required"));
                return;
            }

            if (request.getEmail().trim().isEmpty()) {
                ctx.status(400).json(Map.of("error", "Email is required"));
                return;
            }

            boolean success = adminService.register(
                    request.getUsername().trim(),
                    request.getPassword(),
                    request.getEmail().trim()
            );

            if (success) {
                ctx.status(201).json(Map.of("message", "Admin registered successfully"));
            } else {
                ctx.status(400).json(Map.of("error", "Email already exists"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).json(Map.of("error", "Registration failed: " + e.getMessage()));
        }
    }

    public void login(Context ctx) {
        try {
            // Check if body is empty
            String bodyString = ctx.body();
            if (bodyString.trim().isEmpty()) {
                ctx.status(400).json(Map.of("error", "Request body is required"));
                return;
            }

            LoginRequest request = ctx.bodyAsClass(LoginRequest.class);

            // Validate required fields
            if (request.getEmail().trim().isEmpty()) {
                ctx.status(400).json(Map.of("error", "Email is required"));
                return;
            }

            if (request.getPassword().trim().isEmpty()) {
                ctx.status(400).json(Map.of("error", "Password is required"));
                return;
            }

            Optional<WorkspaceAdmin> adminOpt = adminService.login(
                    request.getEmail().trim(),
                    request.getPassword()
            );

            if (adminOpt.isPresent()) {
                WorkspaceAdmin admin = adminOpt.get();
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Login successful");
                response.put("data", Map.of(
                        "id", admin.getId(),
                        "username", admin.getUsername(),
                        "email", admin.getEmail()
                ));
                ctx.json(response);
            } else {
                ctx.status(401).json(Map.of("error", "Invalid email or password"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).json(Map.of("error", "Login failed: " + e.getMessage()));
        }
    }
}