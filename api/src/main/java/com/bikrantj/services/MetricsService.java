package com.bikrantj.services;

import com.bikrantj.repositories.MetricsRepo;
import com.bikrantj.shared.responses.WorkspaceActivityPoint;

import java.util.List;

public class MetricsService {
    private final MetricsRepo metricsRepo;

    public MetricsService(MetricsRepo metricsRepo) {
        this.metricsRepo = metricsRepo;
    }

    public List<WorkspaceActivityPoint> getWorkspaceActivityMetrics(String workspaceId) {
        // Implementation goes here
        return metricsRepo.getWorkspaceActivityLast24Hours(workspaceId);
    }
}
