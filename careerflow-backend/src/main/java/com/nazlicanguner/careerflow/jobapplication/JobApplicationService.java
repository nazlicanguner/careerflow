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
}