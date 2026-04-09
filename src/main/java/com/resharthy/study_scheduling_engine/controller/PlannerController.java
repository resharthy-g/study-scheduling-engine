package com.resharthy.study_scheduling_engine.controller;

import com.resharthy.study_scheduling_engine.dto.ScheduleRequest;
import com.resharthy.study_scheduling_engine.dto.ScheduleResponse;
import com.resharthy.study_scheduling_engine.service.PlannerService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/planner")
public class PlannerController {

    private final PlannerService plannerService;

    public PlannerController(PlannerService plannerService) {
        this.plannerService = plannerService;
    }

    @PostMapping("/generate")
    public ScheduleResponse generatePlan(@RequestBody ScheduleRequest request) {
        return plannerService.generatePlan(request);
    }

    @PostMapping("/regenerate")
    public ScheduleResponse regeneratePlan(@RequestBody ScheduleRequest request) {
        return plannerService.regeneratePlan(request);
    }
}