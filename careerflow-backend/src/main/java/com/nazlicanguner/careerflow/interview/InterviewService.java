package com.nazlicanguner.careerflow.interview;

import com.nazlicanguner.careerflow.jobapplication.JobApplication;
import com.nazlicanguner.careerflow.jobapplication.JobApplicationNotFoundException;
import com.nazlicanguner.careerflow.jobapplication.JobApplicationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InterviewService {

    private final InterviewRepository interviewRepository;
    private final JobApplicationRepository jobApplicationRepository;

    public InterviewService(
            InterviewRepository interviewRepository,
            JobApplicationRepository jobApplicationRepository
    ) {
        this.interviewRepository = interviewRepository;
        this.jobApplicationRepository = jobApplicationRepository;
    }

    public List<Interview> getAllInterviews() {
        return interviewRepository.findAllWithJobApplication();
    }

    public Interview getInterviewById(Long id) {
        return interviewRepository.findByIdWithJobApplication(id)
                .orElseThrow(() -> new InterviewNotFoundException(id));
    }

    public Interview createInterview(
            Long jobApplicationId,
            Interview interview
    ) {
        if (interviewRepository
                .existsByJobApplicationIdAndStageNumber(
                        jobApplicationId,
                        interview.getStageNumber()
                )) {
            throw new InterviewStageAlreadyExistsException(
                    jobApplicationId,
                    interview.getStageNumber()
            );
        }

        JobApplication jobApplication = jobApplicationRepository
                .findByIdWithCompany(jobApplicationId)
                .orElseThrow(
                        () -> new JobApplicationNotFoundException(
                                jobApplicationId
                        )
                );

        interview.setJobApplication(jobApplication);

        Interview savedInterview = interviewRepository.save(interview);

        return getInterviewById(savedInterview.getId());
    }
}