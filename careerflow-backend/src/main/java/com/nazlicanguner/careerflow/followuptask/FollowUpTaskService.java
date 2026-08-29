package com.nazlicanguner.careerflow.followuptask;

import com.nazlicanguner.careerflow.jobapplication.JobApplication;
import com.nazlicanguner.careerflow.jobapplication.JobApplicationNotFoundException;
import com.nazlicanguner.careerflow.jobapplication.JobApplicationRepository;
import org.springframework.stereotype.Service;
import com.nazlicanguner.careerflow.activitylog.ActivityAction;
import com.nazlicanguner.careerflow.activitylog.ActivityEntityType;
import com.nazlicanguner.careerflow.activitylog.ActivityLogService;

import java.util.List;

@Service
public class FollowUpTaskService {

    private final FollowUpTaskRepository followUpTaskRepository;
    private final JobApplicationRepository jobApplicationRepository;
    private final ActivityLogService activityLogService;

    public FollowUpTaskService(
            FollowUpTaskRepository followUpTaskRepository,
            JobApplicationRepository jobApplicationRepository,
            ActivityLogService activityLogService
    ) {
        this.followUpTaskRepository = followUpTaskRepository;
        this.jobApplicationRepository = jobApplicationRepository;
        this.activityLogService = activityLogService;
    }

    public List<FollowUpTask> getAllFollowUpTasks() {
        return followUpTaskRepository.findAllWithJobApplication();
    }

    public FollowUpTask getFollowUpTaskById(Long id) {
        return followUpTaskRepository.findByIdWithJobApplication(id)
                .orElseThrow(() -> new FollowUpTaskNotFoundException(id));
    }

    public FollowUpTask createFollowUpTask(
            Long jobApplicationId,
            FollowUpTask followUpTask
    ) {
        JobApplication jobApplication = jobApplicationRepository
                .findByIdWithCompany(jobApplicationId)
                .orElseThrow(() -> new JobApplicationNotFoundException(jobApplicationId));

        followUpTask.setJobApplication(jobApplication);

        FollowUpTask savedTask = followUpTaskRepository.save(followUpTask);

        activityLogService.log(
                ActivityEntityType.FOLLOW_UP_TASK,
                savedTask.getId(),
                ActivityAction.CREATED,
                "Follow-up task created: " + savedTask.getTitle()
        );

        return getFollowUpTaskById(savedTask.getId());
    }

    public FollowUpTask updateFollowUpTask(
            Long id,
            FollowUpTask updatedTask
    ) {
        FollowUpTask existingTask = getFollowUpTaskById(id);

        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDueDate(updatedTask.getDueDate());
        existingTask.setStatus(updatedTask.getStatus());
        existingTask.setNotes(updatedTask.getNotes());

        FollowUpTask savedTask = followUpTaskRepository.save(existingTask);

        activityLogService.log(
                ActivityEntityType.FOLLOW_UP_TASK,
                savedTask.getId(),
                ActivityAction.UPDATED,
                "Follow-up task updated: " + savedTask.getTitle()
        );

        return getFollowUpTaskById(id);
    }

    public void deleteFollowUpTask(Long id) {
        FollowUpTask followUpTask = getFollowUpTaskById(id);
        followUpTaskRepository.delete(followUpTask);
        activityLogService.log(
                ActivityEntityType.FOLLOW_UP_TASK,
                followUpTask.getId(),
                ActivityAction.DELETED,
                "Follow-up task deleted: " + followUpTask.getTitle()
        );
    }
}