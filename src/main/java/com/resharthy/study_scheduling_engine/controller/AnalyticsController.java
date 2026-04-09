package com.resharthy.study_scheduling_engine.controller;

import com.resharthy.study_scheduling_engine.dto.AnalyticsSummaryResponse;
import com.resharthy.study_scheduling_engine.dto.SubjectAnalyticsResponse;
import com.resharthy.study_scheduling_engine.service.AnalyticsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/summary")
    public AnalyticsSummaryResponse getSummary() {
        return analyticsService.getSummary();
    }

    @GetMapping("/subjects")
    public List<SubjectAnalyticsResponse> getSubjectWiseAnalytics() {
        return analyticsService.getSubjectWiseAnalytics();
    }
}