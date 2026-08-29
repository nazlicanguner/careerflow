package com.nazlicanguner.careerflow.jobapplication;

import com.nazlicanguner.careerflow.company.Company;
import com.nazlicanguner.careerflow.company.CompanyNotFoundException;
import com.nazlicanguner.careerflow.company.CompanyRepository;
import com.nazlicanguner.careerflow.followuptask.FollowUpTaskRepository;
import com.nazlicanguner.careerflow.interview.InterviewRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.nazlicanguner.careerflow.activitylog.ActivityAction;
import com.nazlicanguner.careerflow.activitylog.ActivityEntityType;
import com.nazlicanguner.careerflow.activitylog.ActivityLogService;
import org.springframework.data.domain.Sort;

import java.util.Set;
import java.util.List;

@Service
public class JobApplicationService {

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
            "applicationDate",
            "createdAt",
            "updatedAt",
            "positionTitle",
            "status"
    );

    private final JobApplicationRepository jobApplicationRepository;
    private final CompanyRepository companyRepository;
    private final InterviewRepository interviewRepository;
    private final FollowUpTaskRepository followUpTaskRepository;
    private final ActivityLogService activityLogService;

    public JobApplicationService(
            JobApplicationRepository jobApplicationRepository,
            ActivityLogService activityLogService,
            CompanyRepository companyRepository,
            InterviewRepository interviewRepository,
            FollowUpTaskRepository followUpTaskRepository
    ) {
        this.jobApplicationRepository = jobApplicationRepository;
        this.activityLogService = activityLogService;
        this.companyRepository = companyRepository;
        this.interviewRepository = interviewRepository;
        this.followUpTaskRepository = followUpTaskRepository;
    }

    public List<JobApplication> getAllJobApplications() {
        return jobApplicationRepository.findAllWithCompany();
    }

    public List<JobApplication> searchJobApplications(
            ApplicationStatus status,
            Long companyId,
            WorkMode workMode,
            String sortBy,
            String direction
    ) {
        String safeSortBy = sortBy != null && ALLOWED_SORT_FIELDS.contains(sortBy)
                ? sortBy
                : "applicationDate";

        Sort.Direction sortDirection = "asc".equalsIgnoreCase(direction)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        Sort sort = Sort.by(sortDirection, safeSortBy);

        return jobApplicationRepository.findByFilters(
                status,
                companyId,
                workMode,
                sort
        );
    }

    public JobApplication getJobApplicationById(Long id) {
        return jobApplicationRepository.findByIdWithCompany(id)
                .orElseThrow(() -> new JobApplicationNotFoundException(id));
    }

    @Transactional
    public JobApplication createJobApplication(
            Long companyId,
            JobApplication jobApplication
    ) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new CompanyNotFoundException(companyId));

        jobApplication.setCompany(company);

        JobApplication savedJobApplication = jobApplicationRepository.save(jobApplication);

        activityLogService.log(
                ActivityEntityType.JOB_APPLICATION,
                savedJobApplication.getId(),
                ActivityAction.CREATED,
                "Job application created: " + savedJobApplication.getPositionTitle()
        );

        return savedJobApplication;
    }

    @Transactional
    public JobApplication updateJobApplication(
            Long id,
            JobApplication updatedJobApplication
    ) {
        JobApplication existingJobApplication = getJobApplicationById(id);

        existingJobApplication.setPositionTitle(
                updatedJobApplication.getPositionTitle()
        );
        existingJobApplication.setJobUrl(
                updatedJobApplication.getJobUrl()
        );
        existingJobApplication.setLocation(
                updatedJobApplication.getLocation()
        );
        existingJobApplication.setStatus(
                updatedJobApplication.getStatus()
        );
        existingJobApplication.setWorkMode(
                updatedJobApplication.getWorkMode()
        );
        existingJobApplication.setSource(
                updatedJobApplication.getSource()
        );
        existingJobApplication.setApplicationDate(
                updatedJobApplication.getApplicationDate()
        );
        existingJobApplication.setNotes(
                updatedJobApplication.getNotes()
        );

        JobApplication savedJobApplication =
                jobApplicationRepository.save(existingJobApplication);

        activityLogService.log(
                ActivityEntityType.JOB_APPLICATION,
                savedJobApplication.getId(),
                ActivityAction.UPDATED,
                "Job application updated: " + savedJobApplication.getPositionTitle()
        );

        return getJobApplicationById(id);
    }

    @Transactional
    public void deleteJobApplication(Long id) {
        JobApplication jobApplication = getJobApplicationById(id);

        interviewRepository.deleteByJobApplicationId(id);
        followUpTaskRepository.deleteByJobApplicationId(id);

        jobApplicationRepository.delete(jobApplication);

        activityLogService.log(
                ActivityEntityType.JOB_APPLICATION,
                jobApplication.getId(),
                ActivityAction.DELETED,
                "Job application deleted: " + jobApplication.getPositionTitle()
        );
    }
}