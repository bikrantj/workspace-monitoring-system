package com.bikrantj.client.clientruntime.transport;

import com.bikrantj.client.clientruntime.data.MonitoringData;
import com.bikrantj.client.clientruntime.data.ProcessMonitoringData;
import com.bikrantj.client.clientruntime.data.ScreenshotMonitoringData;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ConsoleMonitoringTransport implements MonitoringTransport {

    private static final Path SCREENSHOT_DIR =
            Path.of(System.getProperty("user.home"),
                    "workspace-monitor", "screenshots");

    public ConsoleMonitoringTransport() {
        try {
            Files.createDirectories(SCREENSHOT_DIR);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void send(List<MonitoringData> dataList) {

        for (MonitoringData data : dataList) {

            if (data instanceof ProcessMonitoringData pm) {
                logProcesses(pm);
            }

            if (data instanceof ScreenshotMonitoringData sm) {
                saveScreenshot(sm);
            }
        }
    }

    private void logProcesses(ProcessMonitoringData pm) {
        System.out.println("=== Process Snapshot @ " + pm.timestamp + " ===");

        pm.getProcesses().forEach(p ->
                System.out.printf(
                        "PID=%d | NAME=%s | CPU=%.2f | MEM=%d%n",
                        p.processId,
                        p.processName,
                        p.cpuUsage,
                        p.memoryUsage
                )
        );
    }

    private void saveScreenshot(ScreenshotMonitoringData sm) {
        try {
            String fileName =
                    "screenshot_" +
                            sm.timestamp.toString().replace(":", "-") +
                            ".png";

            Path file = SCREENSHOT_DIR.resolve(fileName);
            Files.write(file, sm.getImageBytes());

            System.out.println("Screenshot saved: " + file);

        } catch (IOException e) {
            System.err.println("Failed to save screenshot: " + e.getMessage());
        }
    }
}