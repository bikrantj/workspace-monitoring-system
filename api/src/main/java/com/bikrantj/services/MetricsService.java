package com.bikrantj.services;

import com.bikrantj.repositories.MetricsRepo;
import com.bikrantj.shared.model.ProcessInfo;
import com.bikrantj.shared.responses.ActivityPoint;
import com.bikrantj.shared.responses.HighRamProcessUsage;

import java.util.List;

public class MetricsService {
    private final MetricsRepo metricsRepo;

    public MetricsService(MetricsRepo metricsRepo) {
        this.metricsRepo = metricsRepo;
    }

    public List<ActivityPoint> getClientActivityMetrics(
            String workspaceId,
            String clientId
    ) {
        return metricsRepo.getClientActivityLast24Hours(workspaceId, clientId);
    }

    public List<ProcessInfo> getLatestProcessesForClient(String workspaceId, String clientId) {
        return metricsRepo.getLatestProcessesForClient(workspaceId, clientId);
    }

    public List<ActivityPoint> getWorkspaceActivityMetrics(String workspaceId) {
        // Implementation goes here
        return metricsRepo.getWorkspaceActivityLast24Hours(workspaceId);
    }

    public List<HighRamProcessUsage> getHighRamUsageProcesses(
            String workspaceId,
            String clientId
    ) {
        int snapshotLimit = 5;
        int topN = 5;

        return metricsRepo.getTopRamUsageProcessesForClient(
                workspaceId,
                clientId,
                snapshotLimit,
                topN
        );
    }
}
