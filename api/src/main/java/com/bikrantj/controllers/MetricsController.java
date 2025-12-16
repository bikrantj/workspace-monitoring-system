package com.bikrantj.controllers;

import com.bikrantj.services.MetricsService;
import com.bikrantj.shared.responses.WorkspaceActivityPoint;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.util.List;

public class MetricsController {

    private final MetricsService metricsService;

    public MetricsController(MetricsService metricsService) {
        this.metricsService = metricsService;
    }

    public void getWorkspaceActivity(Context ctx) {
        String workspaceId = ctx.pathParam("workspaceId");
        System.out.println("Geting activity metrics for workspaceId: " + workspaceId);

        if (workspaceId.isBlank()) {
            ctx.status(HttpStatus.BAD_REQUEST)
                    .json("workspaceId is required");
            return;
        }
        List<WorkspaceActivityPoint> points = metricsService.getWorkspaceActivityMetrics(workspaceId);

        for (WorkspaceActivityPoint point : points) {
            System.out.println("Timestamp: " + point.getTime() + ", Active Clients: " + point.getCount());
        }
        ctx.json(
                points
        );
    }
}
