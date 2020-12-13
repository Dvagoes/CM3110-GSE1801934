package com.example.cm3110_gse1801934;

import java.time.*;
import java.util.Date;

public class PassTime {
    private LocalTime startTime;
    private long duration;

    public PassTime(LocalTime startTime, long duration) {
        this.startTime = startTime;
        this.duration = duration;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return "PassTime{" +
                "startTime=" + startTime +
                ", duration=" + duration +
                '}';
    }
}
