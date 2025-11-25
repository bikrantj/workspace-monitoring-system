package com.bikrantj.controllers;

import com.bikrantj.models.WorkspaceAdmin;
import com.bikrantj.services.WorkspaceAdminService;
import com.bikrantj.shared.dto.User;
import com.bikrantj.shared.requests.LoginRequest;
import com.bikrantj.shared.requests.RegisterRequest;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.util.Map;
import java.util.Optional;

public class AuthController {

    private final WorkspaceAdminService adminService;

    public AuthController(WorkspaceAdminService adminService) {
        this.adminService = adminService;
    }


    // REGISTER

    public void register(Context ctx) {
        RegisterRequest req = ctx.bodyValidator(RegisterRequest.class)
                .check(r -> r.getUsername() != null && !r.getUsername().trim().isEmpty(), "Username is required")
                .check(r -> r.getPassword() != null && !r.getPassword().trim().isEmpty(), "Password is required")
                .check(r -> r.getEmail() != null && !r.getEmail().trim().isEmpty(), "Email is required")
                .get();

        boolean success = adminService.register(
                req.getUsername().trim(),
                req.getPassword(),
                req.getEmail().trim()
        );

        if (success) {
            ctx.status(HttpStatus.CREATED)
                    .json(Map.of("message", "Admin registered successfully"));
        } else {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json(Map.of("error", "Email already exists"));
        }
    }


    // LOGIN – now returns JWT token!

    public void login(Context ctx) {
        LoginRequest req = ctx.bodyValidator(LoginRequest.class)
                .check(r -> r.getEmail() != null && !r.getEmail().trim().isEmpty(), "Email is required")
                .check(r -> r.getPassword() != null && !r.getPassword().trim().isEmpty(), "Password is required")
                .get();

        var result = adminService.login(req.getEmail().trim(), req.getPassword());

        if (result.isEmpty()) {
            ctx.status(HttpStatus.UNAUTHORIZED)
                    .json(Map.of("error", "Invalid email or password"));
            return;
        }

        WorkspaceAdminService.LoginResult login = result.get();
        WorkspaceAdmin admin = login.admin();

        // This is what your JavaFX client needs!
        Map<String, Object> response = Map.of(
                "token", login.token(),
                "user", Map.of(
                        "id", admin.getId(),
                        "username", admin.getUsername(),
                        "email", admin.getEmail()
                )
        );

        ctx.status(HttpStatus.OK).json(response);
    }

    public void getCurrentAdmin(Context context) {
        String authHeader = context.header("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            context.status(HttpStatus.UNAUTHORIZED).json(Map.of("error", "Unauthorized"));
            return;
        }
        String token = authHeader.substring(7);

        Optional<WorkspaceAdmin> adminOpt = adminService.validateToken(token);
        if (adminOpt.isEmpty()) {
            context.status(HttpStatus.UNAUTHORIZED).json(Map.of("error", "Invalid or expired token"));
            return;
        }

        WorkspaceAdmin admin = adminOpt.get();
        context.json(new User(admin.getId(), admin.getUsername(), admin.getEmail()));
    }

}