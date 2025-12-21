package com.bikrantj.client.clientruntime.transport;

import com.bikrantj.client.AppContext;
import com.bikrantj.client.api.ApiClient;
import com.bikrantj.client.clientruntime.data.MonitoringData;
import com.bikrantj.client.clientruntime.data.ProcessMonitoringData;
import com.bikrantj.client.clientruntime.data.ScreenshotMonitoringData;
import com.bikrantj.client.clientruntime.monitoring.MonitoringContext;
import com.bikrantj.client.clientruntime.storage.StorageService;
import com.bikrantj.shared.requests.MonitoringPayload;
import com.bikrantj.shared.requests.ProcessPayload;
import com.bikrantj.shared.requests.ScreenshotPayload;

import java.util.ArrayList;
import java.util.List;

public class ApiMonitoringTransport implements MonitoringTransport {

    private final ApiClient apiClient;
    private final MonitoringContext context;

    public ApiMonitoringTransport(MonitoringContext context) {
        this.apiClient = AppContext.getApiClient();
        this.context = context;
    }

    @Override
    public void send(List<MonitoringData> dataList) {

        ScreenshotMonitoringData screenshot = null;
        ProcessMonitoringData processData = null;

        // 1️⃣ Separate collected data
        for (MonitoringData data : dataList) {
            if (data instanceof ScreenshotMonitoringData sm) {
                screenshot = sm;
            } else if (data instanceof ProcessMonitoringData pm) {
                processData = pm;
            }
        }

        // Snapshot must have both
        if (screenshot == null || processData == null) {
            System.err.println("Incomplete monitoring snapshot, skipping send.");
            return;
        }

        // 2️⃣ Build payload

        try {
            // 1️⃣ Save screenshot LOCALLY on client
            String savedPath =
                    StorageService.saveScreenshot(
                            context.getWorkspaceId(),
                            context.getClientId(),
                            screenshot.timestamp,
                            screenshot.getImageBytes()
                    );

            // 2️⃣ Build payload using local path
            MonitoringPayload payload =
                    buildPayload(screenshot, processData, savedPath);

            // 3️⃣ Send metadata only
            apiClient.sendMonitoringSnapshot(payload);

        } catch (Exception e) {
            System.err.println("Failed to send monitoring snapshot: " + e.getMessage());
        }
    }

    private MonitoringPayload buildPayload(
            ScreenshotMonitoringData screenshot,
            ProcessMonitoringData processData, String savedPath
    ) {
        MonitoringPayload payload = new MonitoringPayload();

        payload.setClientId(context.getClientId());
        payload.setWorkspaceId(context.getWorkspaceId());

        // Fake screenshot URL for now
        ScreenshotPayload screenshotPayload = new ScreenshotPayload();
        screenshotPayload.setFilePath(
                savedPath
        );
        screenshotPayload.setFileSize(screenshot.getImageBytes().length);

        payload.setScreenshot(screenshotPayload);

        List<ProcessPayload> processPayloads = new ArrayList<>();
        processData.getProcesses().forEach(p -> {
            ProcessPayload pp = new ProcessPayload();
            pp.setProcessName(p.getProcessName());
            pp.setProcessId(p.getProcessId());
            pp.setMemoryUsage(p.getMemoryUsage());
            pp.setCpuUsage(p.getCpuUsage());
            pp.setWindowTitle(p.getWindowTitle());
            processPayloads.add(pp);
        });

        payload.setProcesses(processPayloads);

        return payload;
    }
}
