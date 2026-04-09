package com.resharthy.study_scheduling_engine.service;

import com.resharthy.study_scheduling_engine.model.StudyTask;
import com.resharthy.study_scheduling_engine.model.TaskStatus;
import com.resharthy.study_scheduling_engine.repository.StudyTaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class StudyTaskService {

    private final StudyTaskRepository studyTaskRepository;

    public StudyTaskService(StudyTaskRepository studyTaskRepository) {
        this.studyTaskRepository = studyTaskRepository;
    }

    public StudyTask createTask(StudyTask studyTask) {
        prepareTaskForSave(studyTask);
        return studyTaskRepository.save(studyTask);
    }

    public List<StudyTask> getAllTasks() {
        refreshOverdueStatuses();
        return studyTaskRepository.findAll();
    }

    public Optional<StudyTask> getTaskById(Long id) {
        refreshOverdueStatuses();
        return studyTaskRepository.findById(id);
    }

    public StudyTask updateTask(Long id, StudyTask updatedTask) {
        return studyTaskRepository.findById(id)
                .map(task -> {
                    task.setSubject(updatedTask.getSubject());
                    task.setTopic(updatedTask.getTopic());
                    task.setDeadline(updatedTask.getDeadline());
                    task.setDifficulty(updatedTask.getDifficulty());
                    task.setEstimatedHours(updatedTask.getEstimatedHours());
                    task.setRemainingHours(updatedTask.getRemainingHours());
                    task.setStatus(updatedTask.getStatus());

                    prepareTaskForSave(task);
                    return studyTaskRepository.save(task);
                })
                .orElse(null);
    }

    public void deleteTask(Long id) {
        studyTaskRepository.deleteById(id);
    }

    public void refreshOverdueStatuses() {
        List<StudyTask> tasks = studyTaskRepository.findAll();
        boolean changed = false;

        for (StudyTask task : tasks) {
            TaskStatus oldStatus = task.getStatus();
            prepareTaskForSave(task);

            if (oldStatus != task.getStatus() || oldStatus == TaskStatus.OVERDUE) {
                changed = true;
            }
        }

        if (changed) {
            studyTaskRepository.saveAll(tasks);
        }
    }

    private void prepareTaskForSave(StudyTask task) {
        if (task.getRemainingHours() < 0) {
            task.setRemainingHours(0);
        }

        if (task.getEstimatedHours() < 0) {
            task.setEstimatedHours(0);
        }

        if (task.getRemainingHours() == 0) {
            task.setStatus(TaskStatus.COMPLETED);
            task.setPriorityScore(0.0);
            return;
        }

        if (task.getStatus() == null) {
            task.setStatus(TaskStatus.PENDING);
        }

        if (task.getDeadline() != null
                && task.getDeadline().isBefore(LocalDate.now())
                && task.getStatus() != TaskStatus.COMPLETED) {
            task.setStatus(TaskStatus.OVERDUE);
        }

        task.setPriorityScore(calculatePriorityScore(task));
    }

    private double calculatePriorityScore(StudyTask task) {
        if (task.getStatus() == TaskStatus.COMPLETED) {
            return 0.0;
        }

        if (task.getDeadline() == null) {
            return 0.0;
        }

        long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), task.getDeadline());

        if (daysLeft <= 0) {
            daysLeft = 1;
        }

        return (task.getDifficulty() * 2.0)
                + (task.getRemainingHours() * 1.5)
                + (10.0 / daysLeft);
    }
}