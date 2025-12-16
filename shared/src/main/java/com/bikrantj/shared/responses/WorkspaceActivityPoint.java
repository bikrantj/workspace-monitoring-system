package com.bikrantj.shared.responses;


public class WorkspaceActivityPoint {

    private String time;
    private int count;

    public WorkspaceActivityPoint() {
    }

    public WorkspaceActivityPoint(String time, int count) {
        this.time = time;
        this.count = count;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
