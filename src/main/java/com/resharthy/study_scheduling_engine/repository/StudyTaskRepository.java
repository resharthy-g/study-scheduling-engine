package com.resharthy.study_scheduling_engine.repository;

import com.resharthy.study_scheduling_engine.model.StudyTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyTaskRepository extends JpaRepository<StudyTask, Long> {
}