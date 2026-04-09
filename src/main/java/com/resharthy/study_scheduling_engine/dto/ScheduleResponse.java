package com.resharthy.study_scheduling_engine.dto;

import java.util.List;

public class ScheduleResponse {

    private List<StudyPlanItemResponse> plan;
    private boolean overloadDetected;
    private String warningMessage;
    private int totalPlannedHours;
    private int unallocatedHours;

    public List<StudyPlanItemResponse> getPlan() {
        return plan;
    }

    public void setPlan(List<StudyPlanItemResponse> plan) {
        this.plan = plan;
    }

    public boolean isOverloadDetected() {
        return overloadDetected;
    }

    public void setOverloadDetected(boolean overloadDetected) {
        this.overloadDetected = overloadDetected;
    }

    public String getWarningMessage() {
        return warningMessage;
    }

    public void setWarningMessage(String warningMessage) {
        this.warningMessage = warningMessage;
    }

    public int getTotalPlannedHours() {
        return totalPlannedHours;
    }

    public void setTotalPlannedHours(int totalPlannedHours) {
        this.totalPlannedHours = totalPlannedHours;
    }

    public int getUnallocatedHours() {
        return unallocatedHours;
    }

    public void setUnallocatedHours(int unallocatedHours) {
        this.unallocatedHours = unallocatedHours;
    }
}