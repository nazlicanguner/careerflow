package com.nazlicanguner.careerflow.dashboard;

import com.nazlicanguner.careerflow.jobapplication.ApplicationStatus;

public record ApplicationStatusSummary(
        ApplicationStatus status,
        long count
) {
}