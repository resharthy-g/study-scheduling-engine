package com.resharthy.study_scheduling_engine.dto;

public class SubjectAnalyticsResponse {

    private String subject;
    private int totalTasks;
    private int totalRemainingHours;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getTotalTasks() {
        return totalTasks;
    }

    public void setTotalTasks(int totalTasks) {
        this.totalTasks = totalTasks;
    }

    public int getTotalRemainingHours() {
        return totalRemainingHours;
    }

    public void setTotalRemainingHours(int totalRemainingHours) {
        this.totalRemainingHours = totalRemainingHours;
    }
}