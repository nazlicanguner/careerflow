package com.nazlicanguner.careerflow.dashboard;

import com.nazlicanguner.careerflow.followuptask.FollowUpTaskRepository;
import com.nazlicanguner.careerflow.followuptask.TaskStatus;
import com.nazlicanguner.careerflow.interview.InterviewRepository;
import com.nazlicanguner.careerflow.jobapplication.JobApplicationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class DashboardService {

    private final JobApplicationRepository jobApplicationRepository;
    private final InterviewRepository interviewRepository;
    private final FollowUpTaskRepository followUpTaskRepository;

    public DashboardService(
            JobApplicationRepository jobApplicationRepository,
            InterviewRepository interviewRepository,
            FollowUpTaskRepository followUpTaskRepository
    ) {
        this.jobApplicationRepository = jobApplicationRepository;
        this.interviewRepository = interviewRepository;
        this.followUpTaskRepository = followUpTaskRepository;
    }

    public DashboardSummary getDashboardSummary() {
        return new DashboardSummary(
                jobApplicationRepository.countApplicationsByStatus(),
                interviewRepository.countUpcomingInterviews(LocalDateTime.now()),
                followUpTaskRepository.countTasksByStatus(TaskStatus.PENDING),
                followUpTaskRepository.countOverdueTasks(
                        LocalDate.now(),
                        TaskStatus.COMPLETED
                )
        );
    }
}