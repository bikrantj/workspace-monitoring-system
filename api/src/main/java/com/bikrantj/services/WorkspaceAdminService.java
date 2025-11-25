package com.bikrantj.services;

import com.bikrantj.models.WorkspaceAdmin;
import com.bikrantj.repositories.interfaces.IWorkspaceAdminRepository;
import com.bikrantj.utils.PasswordUtil;

import java.util.Optional;

public class WorkspaceAdminService {
    private final IWorkspaceAdminRepository adminRepository;

    public WorkspaceAdminService(IWorkspaceAdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public boolean register(String username, String password, String email) {
        if (adminRepository.existsByEmail(email)) {
            return false;
        }

        String salt = PasswordUtil.generateSalt();

        String passwordHash = PasswordUtil.hashPassword(password, salt);


        // Simple hash - just for demo

        WorkspaceAdmin admin = new WorkspaceAdmin(username, passwordHash, email);
        admin.setSalt(salt);
        System.out.println("Registering admin: " + admin.getUsername() + ", " + admin.getEmail());
        return adminRepository.save(admin);
    }

    public Optional<LoginResult> login(String email, String password) {
        Optional<WorkspaceAdmin> adminOpt = adminRepository.findByEmail(email);

        if (adminOpt.isEmpty()) {
            return Optional.empty();
        }


        WorkspaceAdmin admin = adminOpt.get();

        if (!PasswordUtil.verifyPassword(password, admin.getSalt(), admin.getPasswordHash())) {
            return Optional.empty();
        }

        String token = JwtService.generateToken(
                admin.getId(),
                admin.getUsername(),
                admin.getEmail()
        );
        return Optional.of(new LoginResult(token, admin));
    }

    public Optional<WorkspaceAdmin> validateToken(String token) {
        if (token == null || token.isBlank()) {
            return Optional.empty();
        }

        try {
            // 1. Parse and verify the JWT signature + expiration
            String email = JwtService.getEmail(token);
            System.out.println("Validating token for email: " + email);
            if (email == null || !JwtService.isValid(token)) {
                return Optional.empty(); // invalid or expired
            }

            // 2. Fetch the admin from DB (to make sure user still exists and is active)
            Optional<WorkspaceAdmin> adminOpt = adminRepository.findByEmail(email);

            System.out.println("Admin found: " + adminOpt.isPresent() + " for email: " + email);
            if (adminOpt.isEmpty()) {
                return Optional.empty(); // user deleted
            }

            WorkspaceAdmin admin = adminOpt.get();

            return Optional.of(admin);

        } catch (Exception e) {
            // Any parsing error = invalid token
            System.err.println("JWT validation failed: " + e.getMessage());
            return Optional.empty();
        }
    }

    public record LoginResult(String token, WorkspaceAdmin admin) {
    }
}