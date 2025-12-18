package com.bikrantj.shared.responses;

public class HighRamProcessUsage {

    private String processName;
    private long avgMemoryUsage;
    private double avgCpuUsage;

    public HighRamProcessUsage() {
    }

    public HighRamProcessUsage(
            String processName,
            long avgMemoryUsage,
            double avgCpuUsage
    ) {
        this.processName = processName;
        this.avgMemoryUsage = avgMemoryUsage;
        this.avgCpuUsage = avgCpuUsage;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public long getAvgMemoryUsage() {
        return avgMemoryUsage;
    }

    public void setAvgMemoryUsage(long avgMemoryUsage) {
        this.avgMemoryUsage = avgMemoryUsage;
    }

    public double getAvgCpuUsage() {
        return avgCpuUsage;
    }

    public void setAvgCpuUsage(double avgCpuUsage) {
        this.avgCpuUsage = avgCpuUsage;
    }
}
