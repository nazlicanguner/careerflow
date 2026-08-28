package com.nazlicanguner.careerflow.followuptask;

import com.nazlicanguner.careerflow.jobapplication.JobApplication;
import com.nazlicanguner.careerflow.jobapplication.JobApplicationNotFoundException;
import com.nazlicanguner.careerflow.jobapplication.JobApplicationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowUpTaskService {

    private final FollowUpTaskRepository followUpTaskRepository;
    private final JobApplicationRepository jobApplicationRepository;

    public FollowUpTaskService(
            FollowUpTaskRepository followUpTaskRepository,
            JobApplicationRepository jobApplicationRepository
    ) {
        this.followUpTaskRepository = followUpTaskRepository;
        this.jobApplicationRepository = jobApplicationRepository;
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

        followUpTaskRepository.save(existingTask);
        return getFollowUpTaskById(id);
    }

    public void deleteFollowUpTask(Long id) {
        FollowUpTask followUpTask = getFollowUpTaskById(id);
        followUpTaskRepository.delete(followUpTask);
    }
}