package com.resharthy.study_scheduling_engine.service;

import com.resharthy.study_scheduling_engine.dto.AnalyticsSummaryResponse;
import com.resharthy.study_scheduling_engine.dto.SubjectAnalyticsResponse;
import com.resharthy.study_scheduling_engine.model.StudyTask;
import com.resharthy.study_scheduling_engine.model.TaskStatus;
import com.resharthy.study_scheduling_engine.repository.StudyTaskRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AnalyticsService {

    private final StudyTaskRepository studyTaskRepository;
    private final StudyTaskService studyTaskService;

    public AnalyticsService(StudyTaskRepository studyTaskRepository, StudyTaskService studyTaskService) {
        this.studyTaskRepository = studyTaskRepository;
        this.studyTaskService = studyTaskService;
    }

    public AnalyticsSummaryResponse getSummary() {
        studyTaskService.refreshOverdueStatuses();

        List<StudyTask> tasks = studyTaskRepository.findAll();

        AnalyticsSummaryResponse response = new AnalyticsSummaryResponse();

        int totalTasks = tasks.size();
        int completedTasks = 0;
        int overdueTasks = 0;
        int totalPendingHours = 0;

        for (StudyTask task : tasks) {
            if (task.getStatus() == TaskStatus.COMPLETED) {
                completedTasks = completedTasks + 1;
            }

            if (task.getStatus() == TaskStatus.OVERDUE) {
                overdueTasks = overdueTasks + 1;
            }

            if (task.getStatus() != TaskStatus.COMPLETED) {
                totalPendingHours = totalPendingHours + task.getRemainingHours();
            }
        }

        response.setTotalTasks(totalTasks);
        response.setCompletedTasks(completedTasks);
        response.setOverdueTasks(overdueTasks);
        response.setTotalPendingHours(totalPendingHours);

        return response;
    }

    public List<SubjectAnalyticsResponse> getSubjectWiseAnalytics() {
        studyTaskService.refreshOverdueStatuses();

        List<StudyTask> tasks = studyTaskRepository.findAll();
        Map<String, SubjectAnalyticsResponse> subjectMap = new LinkedHashMap<>();

        for (StudyTask task : tasks) {
            String subject = task.getSubject();

            if (subject == null || subject.isBlank()) {
                subject = "Unknown";
            }

            if (!subjectMap.containsKey(subject)) {
                SubjectAnalyticsResponse response = new SubjectAnalyticsResponse();
                response.setSubject(subject);
                response.setTotalTasks(0);
                response.setTotalRemainingHours(0);
                subjectMap.put(subject, response);
            }

            SubjectAnalyticsResponse current = subjectMap.get(subject);
            current.setTotalTasks(current.getTotalTasks() + 1);

            if (task.getStatus() != TaskStatus.COMPLETED) {
                current.setTotalRemainingHours(current.getTotalRemainingHours() + task.getRemainingHours());
            }
        }

        return new ArrayList<>(subjectMap.values());
    }
}