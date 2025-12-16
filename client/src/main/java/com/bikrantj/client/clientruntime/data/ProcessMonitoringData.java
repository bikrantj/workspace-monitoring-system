package com.bikrantj.client.clientruntime.data;

import java.time.Instant;
import java.util.List;

public class ProcessMonitoringData extends MonitoringData {

    private final List<ProcessInfo> processes;

    public ProcessMonitoringData(
            String workspaceId,
            String clientIdentifier,
            Instant timestamp,
            List<ProcessInfo> processes
    ) {
        super(workspaceId, clientIdentifier, timestamp);
        this.processes = processes;
    }

    public List<ProcessInfo> getProcesses() {
        return processes;
    }

    public static class ProcessInfo {
        public final int processId;
        public final String processName;
        public final long memoryUsage;
        public final double cpuUsage;
        public final String windowTitle;

        public ProcessInfo(
                int processId,
                String processName,
                long memoryUsage,
                double cpuUsage,
                String windowTitle
        ) {
            this.processId = processId;
            this.processName = processName;
            this.memoryUsage = memoryUsage;
            this.cpuUsage = cpuUsage;
            this.windowTitle = windowTitle;
        }
    }
}