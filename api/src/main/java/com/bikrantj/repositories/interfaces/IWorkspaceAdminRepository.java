package com.bikrantj.repositories.interfaces;

import com.bikrantj.models.WorkspaceAdmin;

import java.util.Optional;

public interface IWorkspaceAdminRepository {

    // Create admin
    boolean save(WorkspaceAdmin admin);

    Optional<WorkspaceAdmin> findByEmail(String email);

    boolean existsByEmail(String email);


}