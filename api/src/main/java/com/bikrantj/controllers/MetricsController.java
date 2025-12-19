package com.bikrantj.controllers;

import com.bikrantj.services.MetricsService;
import com.bikrantj.shared.model.ProcessInfo;
import com.bikrantj.shared.responses.ActivityPoint;
import com.bikrantj.shared.responses.HighRamProcessUsage;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.util.List;

public class MetricsController {

    private final MetricsService metricsService;

    public MetricsController(MetricsService metricsService) {
        this.metricsService = metricsService;
    }

    public void getHighRamUsageProcesses(Context ctx) {

        String workspaceId = ctx.pathParam("workspaceId");
        String clientId = ctx.pathParam("clientId");

        if (workspaceId.isBlank() || clientId.isBlank()) {
            ctx.status(400).json("workspaceId and clientId are required");
            return;
        }

        List<HighRamProcessUsage> data = metricsService.getHighRamUsageProcesses(
                workspaceId,
                clientId
        );

        System.out.println("High RAM Usage Processes for clientId: " + clientId + " in workspaceId: " + workspaceId);
        for (HighRamProcessUsage process : data) {
            System.out.println("Process Name: " + process.getProcessName() +
                    ", PID: " + process.getProcessName() +
                    ", RAM Usage (MB): " + process.getAvgMemoryUsage());
        }

        ctx.json(
                data
        );
    }

    public void getLatestProcessesForClient(Context ctx) {

        String workspaceId = ctx.pathParam("workspaceId");
        String clientId = ctx.pathParam("clientId");

        if (workspaceId.isBlank() || clientId.isBlank()) {
            ctx.status(400).json("workspaceId and clientId are required");
            return;
        }

        List<ProcessInfo> data = metricsService.getLatestProcessesForClient(
                workspaceId,
                clientId
        );

        System.out.println("Getting Latest Processes: " + clientId + " in workspaceId: " + workspaceId);
        for (ProcessInfo process : data) {
            System.out.println("Process Name: " + process.getProcessName() +
                    ", PID: " + process.getProcessName());
        }

        ctx.json(
                data
        );
    }

    public void getClientActivity(Context ctx) {

        String workspaceId = ctx.pathParam("workspaceId");
        String clientId = ctx.pathParam("clientId");

        if (workspaceId.isBlank() || clientId.isBlank()) {
            ctx.status(400).json("workspaceId and clientId are required");
            return;
        }

        List<ActivityPoint> points =
                metricsService.getClientActivityMetrics(workspaceId, clientId);

        ctx.json(points);
    }

    public void getWorkspaceActivity(Context ctx) {
        String workspaceId = ctx.pathParam("workspaceId");
        System.out.println("Geting activity metrics for workspaceId: " + workspaceId);

        if (workspaceId.isBlank()) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("workspaceId is required");
            return;
        }
        List<ActivityPoint> points = metricsService.getWorkspaceActivityMetrics(workspaceId);

        for (ActivityPoint point : points) {
            System.out.println("Timestamp: " + point.getTime() + ", Active Clients: " + point.getCount());
        }
        ctx.json(
                points
        );
    }
}
