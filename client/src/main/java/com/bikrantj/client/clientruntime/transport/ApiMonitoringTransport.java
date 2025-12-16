package com.bikrantj.client.clientruntime.transport;

import com.bikrantj.client.AppContext;
import com.bikrantj.client.api.ApiClient;
import com.bikrantj.client.clientruntime.data.MonitoringData;
import com.bikrantj.client.clientruntime.data.ProcessMonitoringData;
import com.bikrantj.client.clientruntime.data.ScreenshotMonitoringData;
import com.bikrantj.client.clientruntime.monitoring.MonitoringContext;
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
        MonitoringPayload payload = buildPayload(screenshot, processData);

        // 3️⃣ Send to server
        try {
            apiClient.sendMonitoringSnapshot(payload);
        } catch (Exception e) {
            System.err.println("Failed to send monitoring snapshot: " + e.getMessage());
        }
    }

    private MonitoringPayload buildPayload(
            ScreenshotMonitoringData screenshot,
            ProcessMonitoringData processData
    ) {
        MonitoringPayload payload = new MonitoringPayload();

        payload.setClientId(context.getClientId());
        payload.setWorkspaceId(context.getWorkspaceId());

        // Fake screenshot URL for now
        ScreenshotPayload screenshotPayload = new ScreenshotPayload();
        screenshotPayload.setFilePath(
                "https://fake-fileserver/screenshots/" + screenshot.timestamp + ".png"
        );
        screenshotPayload.setFileSize(screenshot.getImageBytes().length);

        payload.setScreenshot(screenshotPayload);

        List<ProcessPayload> processPayloads = new ArrayList<>();
        processData.getProcesses().forEach(p -> {
            ProcessPayload pp = new ProcessPayload();
            pp.setProcessName(p.processName);
            pp.setProcessId(p.processId);
            pp.setMemoryUsage(p.memoryUsage);
            pp.setCpuUsage(p.cpuUsage);
            pp.setWindowTitle(p.windowTitle);
            processPayloads.add(pp);
        });

        payload.setProcesses(processPayloads);

        return payload;
    }
}
