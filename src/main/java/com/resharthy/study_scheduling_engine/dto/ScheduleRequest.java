package com.resharthy.study_scheduling_engine.dto;

public class ScheduleRequest {

    private int dailyAvailableHours;

    public int getDailyAvailableHours() {
        return dailyAvailableHours;
    }

    public void setDailyAvailableHours(int dailyAvailableHours) {
        this.dailyAvailableHours = dailyAvailableHours;
    }
}