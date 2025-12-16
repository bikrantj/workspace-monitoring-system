package com.bikrantj.client.clientruntime.collectors;

import com.bikrantj.client.clientruntime.data.ProcessMonitoringData;
import com.bikrantj.client.clientruntime.monitoring.MonitoringContext;
import oshi.SystemInfo;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProcessCollector implements DataCollector<ProcessMonitoringData> {

    private final OperatingSystem os =
            new SystemInfo().getOperatingSystem();

    @Override
    public ProcessMonitoringData collect(
            MonitoringContext context,
            Instant timestamp
    ) {
        List<ProcessMonitoringData.ProcessInfo> processes =
                os.getProcesses().stream()
                        .filter(p -> p.getUser() != null)
                        .filter(this::isUserApplication)
                        .map(this::mapProcess)
                        .collect(Collectors.toList());

        processes = removeDuplicatesByName(processes);

        return new ProcessMonitoringData(
                context.getWorkspaceId(),
                context.getClientIdentifier(),
                timestamp,
                processes
        );
    }

    private ProcessMonitoringData.ProcessInfo mapProcess(OSProcess p) {
        return new ProcessMonitoringData.ProcessInfo(
                p.getProcessID(),
                p.getName(),
                p.getResidentSetSize() / 1024, // in KB
                p.getProcessCpuLoadCumulative() * 100,
                p.getCommandLine()
        );
    }

    private List<ProcessMonitoringData.ProcessInfo> removeDuplicatesByName(List<ProcessMonitoringData.ProcessInfo> processes) {

        Map<String, ProcessMonitoringData.ProcessInfo> uniqueByName = new LinkedHashMap<>();

        for (ProcessMonitoringData.ProcessInfo process : processes) {
            uniqueByName.putIfAbsent(
                    process.processName.toLowerCase(),
                    process
            );
        }

        return new ArrayList<>(uniqueByName.values());
    }

    private boolean isUserApplication(OSProcess p) {

        String user = p.getUser();
        if (user == null) return false;

        String currentUser = System.getProperty("user.name");
        if (!user.equalsIgnoreCase(currentUser)) return false;

        String path = p.getPath();
        if (path == null || path.isBlank()) return false;

        String name = p.getName() != null
                ? p.getName().toLowerCase()
                : "";

        path = path.toLowerCase();

        // ---- Common system process names ----
        String[] blockedNames = {
                "systemd", "init", "svchost.exe", "csrss.exe",
                "wininit.exe", "services.exe", "lsass.exe",
                "bash", "zsh", "sh", "cmd.exe", "powershell.exe",
                "java", "python"
        };

        for (String blocked : blockedNames) {
            if (name.equals(blocked)) return false;
        }

        String osName = System.getProperty("os.name").toLowerCase();

        // ---- OS-specific path checks ----
        if (osName.contains("linux")) {
            return path.contains("/home/")
                    || path.contains("/opt/")
                    || path.contains("/snap/")
                    || path.contains("/flatpak/");
        }

        if (osName.contains("windows")) {
            return path.contains("program files")
                    || path.contains("appdata\\local");
        }

        if (osName.contains("mac")) {
            return path.contains("/applications")
                    || path.contains("/users/");
        }

        return false;
    }

}