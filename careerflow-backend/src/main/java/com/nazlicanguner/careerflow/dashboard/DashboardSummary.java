package com.nazlicanguner.careerflow.dashboard;

import java.util.List;

public record DashboardSummary(
        List<ApplicationStatusSummary> applicationsByStatus,
        long upcomingInterviews,
        long openTasks,
        long overdueTasks
) {
}