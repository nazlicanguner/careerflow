package com.nazlicanguner.careerflow.jobapplication;

import com.nazlicanguner.careerflow.company.Company;
import com.nazlicanguner.careerflow.company.CompanyNotFoundException;
import com.nazlicanguner.careerflow.company.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final CompanyRepository companyRepository;

    public JobApplicationService(
            JobApplicationRepository jobApplicationRepository,
            CompanyRepository companyRepository
    ) {
        this.jobApplicationRepository = jobApplicationRepository;
        this.companyRepository = companyRepository;
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

    public void deleteJobApplication(Long id) {
        JobApplication jobApplication = getJobApplicationById(id);
        jobApplicationRepository.delete(jobApplication);
    }
}