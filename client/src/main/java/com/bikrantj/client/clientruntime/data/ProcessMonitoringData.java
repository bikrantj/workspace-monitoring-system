package com.bikrantj.client.clientruntime.data;

import com.bikrantj.shared.model.ProcessInfo;

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


}