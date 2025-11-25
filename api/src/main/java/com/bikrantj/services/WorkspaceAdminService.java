package com.bikrantj.services;

import com.bikrantj.models.WorkspaceAdmin;
import com.bikrantj.repositories.interfaces.IWorkspaceAdminRepository;

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

        // Simple hash - just for demo
        String passwordHash = String.valueOf(password.hashCode());

        WorkspaceAdmin admin = new WorkspaceAdmin(username, passwordHash, email);
        System.out.println("Registering admin: " + admin.getUsername() + ", " + admin.getEmail());
        return adminRepository.save(admin);
    }

    public Optional<WorkspaceAdmin> login(String email, String password) {
        Optional<WorkspaceAdmin> adminOpt = adminRepository.findByEmail(email);

        if (adminOpt.isPresent()) {
            WorkspaceAdmin admin = adminOpt.get();
            String inputHash = String.valueOf(password.hashCode());
            if (inputHash.equals(admin.getPasswordHash())) {
                return adminOpt;
            }
        }
        return Optional.empty();
    }
}