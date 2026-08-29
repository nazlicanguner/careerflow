package com.nazlicanguner.careerflow.interview;

import com.nazlicanguner.careerflow.jobapplication.JobApplication;
import com.nazlicanguner.careerflow.jobapplication.JobApplicationNotFoundException;
import com.nazlicanguner.careerflow.jobapplication.JobApplicationRepository;
import org.springframework.stereotype.Service;
import com.nazlicanguner.careerflow.activitylog.ActivityAction;
import com.nazlicanguner.careerflow.activitylog.ActivityEntityType;
import com.nazlicanguner.careerflow.activitylog.ActivityLogService;
import java.time.LocalDateTime;

import java.util.List;

@Service
public class InterviewService {

    private final InterviewRepository interviewRepository;
    private final JobApplicationRepository jobApplicationRepository;
    private final ActivityLogService activityLogService;

    public InterviewService(
            InterviewRepository interviewRepository,
            JobApplicationRepository jobApplicationRepository,
            ActivityLogService activityLogService
    ) {
        this.interviewRepository = interviewRepository;
        this.jobApplicationRepository = jobApplicationRepository;
        this.activityLogService = activityLogService;
    }

    public List<Interview> getAllInterviews() {
        return interviewRepository.findAllWithJobApplication();
    }

    public List<Interview> searchInterviews(
            Long jobApplicationId,
            boolean upcoming
    ) {
        return interviewRepository.findByFilters(
                jobApplicationId,
                upcoming,
                LocalDateTime.now()
        );
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

        activityLogService.log(
                ActivityEntityType.INTERVIEW,
                savedInterview.getId(),
                ActivityAction.CREATED,
                "Interview created: " + savedInterview.getStageName()
        );

        return getInterviewById(savedInterview.getId());
    }

    public Interview updateInterview(Long id, Interview updatedInterview) {
        Interview existingInterview = getInterviewById(id);

        boolean stageNumberChanged =
                !existingInterview.getStageNumber().equals(updatedInterview.getStageNumber());

        if (stageNumberChanged &&
                interviewRepository.existsByJobApplicationIdAndStageNumber(
                        existingInterview.getJobApplication().getId(),
                        updatedInterview.getStageNumber()
                )) {
            throw new InterviewStageAlreadyExistsException(
                    existingInterview.getJobApplication().getId(),
                    updatedInterview.getStageNumber()
            );
        }

        existingInterview.setStageNumber(updatedInterview.getStageNumber());
        existingInterview.setStageName(updatedInterview.getStageName());
        existingInterview.setScheduledAt(updatedInterview.getScheduledAt());
        existingInterview.setInterviewType(updatedInterview.getInterviewType());
        existingInterview.setOutcome(updatedInterview.getOutcome());
        existingInterview.setNotes(updatedInterview.getNotes());

        Interview savedInterview = interviewRepository.save(existingInterview);

        activityLogService.log(
                ActivityEntityType.INTERVIEW,
                savedInterview.getId(),
                ActivityAction.UPDATED,
                "Interview updated: " + savedInterview.getStageName()
        );

        return getInterviewById(id);
    }

    public void deleteInterview(Long id) {
        Interview interview = getInterviewById(id);
        interviewRepository.delete(interview);
        activityLogService.log(
                ActivityEntityType.INTERVIEW,
                interview.getId(),
                ActivityAction.DELETED,
                "Interview deleted: " + interview.getStageName()
        );
    }
}