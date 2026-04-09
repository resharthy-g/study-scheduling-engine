package com.resharthy.study_scheduling_engine.dto;

public class AnalyticsSummaryResponse {

    private int totalTasks;
    private int completedTasks;
    private int overdueTasks;
    private int totalPendingHours;

    public int getTotalTasks() {
        return totalTasks;
    }

    public void setTotalTasks(int totalTasks) {
        this.totalTasks = totalTasks;
    }

    public int getCompletedTasks() {
        return completedTasks;
    }

    public void setCompletedTasks(int completedTasks) {
        this.completedTasks = completedTasks;
    }

    public int getOverdueTasks() {
        return overdueTasks;
    }

    public void setOverdueTasks(int overdueTasks) {
        this.overdueTasks = overdueTasks;
    }

    public int getTotalPendingHours() {
        return totalPendingHours;
    }

    public void setTotalPendingHours(int totalPendingHours) {
        this.totalPendingHours = totalPendingHours;
    }
}