package com.bikrantj.shared.responses;


public class ActivityPoint {

    private String time;
    private int count;

    public ActivityPoint() {
    }

    public ActivityPoint(String time, int count) {
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
