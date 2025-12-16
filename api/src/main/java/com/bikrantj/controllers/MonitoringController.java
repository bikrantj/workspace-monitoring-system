package com.bikrantj.controllers;

import com.bikrantj.services.ClientService;
import com.bikrantj.services.MonitoringService;
import com.bikrantj.shared.requests.MonitoringPayload;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

public class MonitoringController {

    private final MonitoringService monitoringService;
    private final ClientService clientService;

    public MonitoringController(MonitoringService monitoringService, ClientService clientService) {
        this.monitoringService = monitoringService;
        this.clientService = clientService;
    }

    public void ingest(Context ctx) {

        MonitoringPayload payload = ctx.bodyValidator(MonitoringPayload.class)
                .check(p -> p.getClientId() != null && !p.getClientId().isBlank(), "clientId is required")
                .check(p -> p.getWorkspaceId() != null && !p.getWorkspaceId().isBlank(), "workspaceId is required")
                .check(p -> p.getScreenshot() != null, "screenshot is required")
                .check(p -> p.getProcesses() != null, "processes are required")
                .get();


        monitoringService.ingest(payload);

        ctx.status(HttpStatus.CREATED);
    }
}
