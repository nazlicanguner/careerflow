package com.nazlicanguner.careerflow.jobapplication;

import com.nazlicanguner.careerflow.company.Company;
import com.nazlicanguner.careerflow.company.CompanyNotFoundException;
import com.nazlicanguner.careerflow.company.CompanyRepository;
import com.nazlicanguner.careerflow.followuptask.FollowUpTaskRepository;
import com.nazlicanguner.careerflow.interview.InterviewRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final CompanyRepository companyRepository;
    private final InterviewRepository interviewRepository;
    private final FollowUpTaskRepository followUpTaskRepository;

    public JobApplicationService(
            JobApplicationRepository jobApplicationRepository,
            CompanyRepository companyRepository,
            InterviewRepository interviewRepository,
            FollowUpTaskRepository followUpTaskRepository
    ) {
        this.jobApplicationRepository = jobApplicationRepository;
        this.companyRepository = companyRepository;
        this.interviewRepository = interviewRepository;
        this.followUpTaskRepository = followUpTaskRepository;
    }

    public List<JobApplication> getAllJobApplications() {
        return jobApplicationRepository.findAllWithCompany();
    }

    public JobApplication getJobApplicationById(Long id) {
        return jobApplicationRepository.findByIdWithCompany(id)
                .orElseThrow(() -> new JobApplicationNotFoundException(id));
    }

    public JobApplication createJobApplication(
            Long companyId,
            JobApplication jobApplication
    ) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new CompanyNotFoundException(companyId));

        jobApplication.setCompany(company);

        return jobApplicationRepository.save(jobApplication);
    }

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

        jobApplicationRepository.save(existingJobApplication);

        return getJobApplicationById(id);
    }

        @Transactional
        public void deleteJobApplication(Long id) {
            JobApplication jobApplication = getJobApplicationById(id);

            interviewRepository.deleteByJobApplicationId(id);
            followUpTaskRepository.deleteByJobApplicationId(id);

            jobApplicationRepository.delete(jobApplication);
        }
}