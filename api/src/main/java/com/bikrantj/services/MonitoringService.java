package com.bikrantj.services;


import com.bikrantj.repositories.ProcessRepo;
import com.bikrantj.repositories.ScreenshotRepo;
import com.bikrantj.repositories.SnapshotRepo;
import com.bikrantj.shared.model.MonitoringSnapshot;
import com.bikrantj.shared.model.ProcessInfo;
import com.bikrantj.shared.requests.MonitoringPayload;
import com.bikrantj.shared.requests.ProcessPayload;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class MonitoringService {

    private final SnapshotRepo snapshotRepo;
    private final ScreenshotRepo screenshotRepo;
    private final ProcessRepo processRepo;

    public MonitoringService(
            SnapshotRepo snapshotRepo,
            ScreenshotRepo screenshotRepo,
            ProcessRepo processRepo
    ) {
        this.snapshotRepo = snapshotRepo;
        this.screenshotRepo = screenshotRepo;
        this.processRepo = processRepo;
    }

    public void ingest(MonitoringPayload payload) {

        //  Create snapshot
        MonitoringSnapshot snapshot =
                snapshotRepo.create(
                        payload.getClientId(),
                        payload.getWorkspaceId()
                );

        //  Save screenshot (fake URL for now)
        screenshotRepo.create(
                snapshot.getId(),
                payload.getClientId(),
                payload.getWorkspaceId(),
                payload.getScreenshot().getFilePath(),
                payload.getScreenshot().getFileSize()
        );

        // Convert & save processes
        List<ProcessInfo> processes = new ArrayList<>();

        for (ProcessPayload p : payload.getProcesses()) {
            processes.add(
                    new ProcessInfo(
                            null,
                            snapshot.getId(),
                            payload.getClientId(),
                            payload.getWorkspaceId(),
                            p.getProcessName(),
                            p.getProcessId(),
                            p.getMemoryUsage(),
                            p.getCpuUsage(),
                            p.getWindowTitle(),
                            Instant.now().toString()
                    )
            );
        }

        processRepo.batchInsert(processes);
    }
}
