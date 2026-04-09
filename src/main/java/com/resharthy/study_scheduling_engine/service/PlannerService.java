package com.resharthy.study_scheduling_engine.service;

import com.resharthy.study_scheduling_engine.dto.ScheduleRequest;
import com.resharthy.study_scheduling_engine.dto.ScheduleResponse;
import com.resharthy.study_scheduling_engine.dto.StudyPlanItemResponse;
import com.resharthy.study_scheduling_engine.model.StudyTask;
import com.resharthy.study_scheduling_engine.model.TaskStatus;
import com.resharthy.study_scheduling_engine.repository.StudyTaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class PlannerService {

    private final StudyTaskRepository studyTaskRepository;
    private final StudyTaskService studyTaskService;

    public PlannerService(StudyTaskRepository studyTaskRepository, StudyTaskService studyTaskService) {
        this.studyTaskRepository = studyTaskRepository;
        this.studyTaskService = studyTaskService;
    }

    public ScheduleResponse generatePlan(ScheduleRequest request) {
        studyTaskService.refreshOverdueStatuses();

        int dailyAvailableHours = request.getDailyAvailableHours();

        if (dailyAvailableHours <= 0) {
            ScheduleResponse response = new ScheduleResponse();
            response.setPlan(new ArrayList<>());
            response.setOverloadDetected(true);
            response.setWarningMessage("Daily available study hours must be greater than 0.");
            response.setTotalPlannedHours(0);
            response.setUnallocatedHours(0);
            return response;
        }

        List<StudyTask> tasks = studyTaskRepository.findAll();

        List<StudyTask> tasksToPlan = tasks.stream()
                .filter(task -> task.getStatus() != TaskStatus.COMPLETED)
                .filter(task -> task.getRemainingHours() > 0)
                .sorted(Comparator
                        .comparingDouble(StudyTask::getPriorityScore).reversed()
                        .thenComparing(StudyTask::getDeadline))
                .toList();

        List<StudyPlanItemResponse> planItems = new ArrayList<>();
        Map<LocalDate, Integer> remainingCapacityByDate = new HashMap<>();
        LocalDate today = LocalDate.now();

        int totalPlannedHours = 0;
        int unallocatedHours = 0;

        for (StudyTask task : tasksToPlan) {
            int taskRemainingHours = task.getRemainingHours();

            if (task.getDeadline() == null) {
                unallocatedHours = unallocatedHours + taskRemainingHours;
                continue;
            }

            LocalDate currentDate = today;

            while (!currentDate.isAfter(task.getDeadline()) && taskRemainingHours > 0) {
                int remainingCapacity = remainingCapacityByDate.getOrDefault(currentDate, dailyAvailableHours);

                if (remainingCapacity > 0) {
                    int hoursToAllocate = Math.min(remainingCapacity, taskRemainingHours);

                    StudyPlanItemResponse item = new StudyPlanItemResponse();
                    item.setTaskId(task.getId());
                    item.setSubject(task.getSubject());
                    item.setTopic(task.getTopic());
                    item.setStudyDate(currentDate);
                    item.setPlannedHours(hoursToAllocate);

                    planItems.add(item);

                    remainingCapacityByDate.put(currentDate, remainingCapacity - hoursToAllocate);

                    taskRemainingHours = taskRemainingHours - hoursToAllocate;
                    totalPlannedHours = totalPlannedHours + hoursToAllocate;
                }

                currentDate = currentDate.plusDays(1);
            }

            if (taskRemainingHours > 0) {
                unallocatedHours = unallocatedHours + taskRemainingHours;
            }
        }

        ScheduleResponse response = new ScheduleResponse();
        response.setPlan(planItems);
        response.setTotalPlannedHours(totalPlannedHours);
        response.setUnallocatedHours(unallocatedHours);

        if (unallocatedHours > 0) {
            response.setOverloadDetected(true);
            response.setWarningMessage("Warning: all tasks cannot fit before their deadlines.");
        } else {
            response.setOverloadDetected(false);
            response.setWarningMessage("Study plan generated successfully.");
        }

        return response;
    }

    public ScheduleResponse regeneratePlan(ScheduleRequest request) {
        return generatePlan(request);
    }
}