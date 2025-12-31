package com.bikrantj.controllers;

import com.bikrantj.services.WorkspaceAdminService;
import com.bikrantj.shared.dto.User;
import com.bikrantj.shared.model.WorkspaceAdmin;
import com.bikrantj.shared.requests.LoginRequest;
import com.bikrantj.shared.requests.RegisterRequest;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.util.Map;

public class AuthController {

    private final WorkspaceAdminService workspaceAdminService;

    public AuthController(WorkspaceAdminService adminService) {
        this.workspaceAdminService = adminService;
    }


    // REGISTER

    private static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }

        // RFC 5322 compliant pattern (practical subset - very good balance)
        String emailRegex =
                "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?" +
                        "(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$";

        return email.matches(emailRegex);
    }

    public void register(Context ctx) {
        RegisterRequest req = ctx.bodyAsClass(RegisterRequest.class);
        if (req.getUsername() == null || req.getUsername().trim().isEmpty() ||
                req.getPassword() == null || req.getPassword().trim().isEmpty() ||
                req.getEmail() == null || req.getEmail().trim().isEmpty()) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Username, password, and email are required");
            return;
        }

        if (!isValidEmail(req.getEmail().trim())) {
            System.out.println("Invalid email format: " + req.getEmail().trim());
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Invalid email format");
            return;
        }

        if (req.getPassword().length() < 8) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Password must be at least 8 characters long");
            return;
        }

        System.out.println("Valid email format: " + req.getEmail().trim());

        boolean success = workspaceAdminService.register(
                req.getUsername().trim(),
                req.getPassword(),
                req.getEmail().trim()
        );

        if (success) {
            ctx.status(HttpStatus.CREATED)
                    .json(Map.of("message", "Admin registered successfully"));
        } else {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("Email already in use");
        }
    }


    // LOGIN – now returns JWT token!

    public void login(Context ctx) {
        LoginRequest req = ctx.bodyValidator(LoginRequest.class)
                .check(r -> r.getEmail() != null && !r.getEmail().trim().isEmpty(), "Email is required")
                .check(r -> r.getPassword() != null && !r.getPassword().trim().isEmpty(), "Password is required")
                .get();

        var result = workspaceAdminService.login(req.getEmail().trim(), req.getPassword());

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
        User user = workspaceAdminService.getCurrentAdmin(context);
        if (user != null) {
            context.status(HttpStatus.OK).json(user);
        } else {
            context.status(HttpStatus.UNAUTHORIZED)
                    .json(Map.of("error", "Unauthorized"));
        }
    }

}