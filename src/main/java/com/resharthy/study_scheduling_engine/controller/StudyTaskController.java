package com.resharthy.study_scheduling_engine.controller;

import com.resharthy.study_scheduling_engine.model.StudyTask;
import com.resharthy.study_scheduling_engine.service.StudyTaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class StudyTaskController {

    private final StudyTaskService studyTaskService;

    public StudyTaskController(StudyTaskService studyTaskService) {
        this.studyTaskService = studyTaskService;
    }

    @PostMapping
    public StudyTask createTask(@RequestBody StudyTask studyTask) {
        return studyTaskService.createTask(studyTask);
    }

    @GetMapping
    public List<StudyTask> getAllTasks() {
        return studyTaskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudyTask> getTaskById(@PathVariable Long id) {
        return studyTaskService.getTaskById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudyTask> updateTask(@PathVariable Long id, @RequestBody StudyTask updatedTask) {
        StudyTask task = studyTaskService.updateTask(id, updatedTask);

        if (task == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        studyTaskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}