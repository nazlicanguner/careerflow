package com.nazlicanguner.careerflow.company;

import org.springframework.stereotype.Service;
import com.nazlicanguner.careerflow.jobapplication.JobApplicationRepository;
import com.nazlicanguner.careerflow.activitylog.ActivityAction;
import com.nazlicanguner.careerflow.activitylog.ActivityEntityType;
import com.nazlicanguner.careerflow.activitylog.ActivityLogService;

import java.util.List;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final JobApplicationRepository jobApplicationRepository;
    private final ActivityLogService activityLogService;

    public CompanyService(
            CompanyRepository companyRepository,
            JobApplicationRepository jobApplicationRepository,
            ActivityLogService activityLogService
    ) {
        this.companyRepository = companyRepository;
        this.jobApplicationRepository = jobApplicationRepository;
        this.activityLogService = activityLogService;
    }
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public Company createCompany(Company company) {
        Company savedCompany = companyRepository.save(company);

        activityLogService.log(
                ActivityEntityType.COMPANY,
                savedCompany.getId(),
                ActivityAction.CREATED,
                "Company created: " + savedCompany.getName()
        );

        return savedCompany;
    }

    public Company getCompanyById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException(id));
    }

    public Company updateCompany(Long id, Company updatedCompany) {
        Company existingCompany = getCompanyById(id);

        existingCompany.setName(updatedCompany.getName());
        existingCompany.setIndustry(updatedCompany.getIndustry());
        existingCompany.setLocation(updatedCompany.getLocation());
        existingCompany.setWebsite(updatedCompany.getWebsite());
        existingCompany.setNotes(updatedCompany.getNotes());

        Company savedCompany = companyRepository.save(existingCompany);

        activityLogService.log(
                ActivityEntityType.COMPANY,
                savedCompany.getId(),
                ActivityAction.UPDATED,
                "Company updated: " + savedCompany.getName()
        );

        return savedCompany;
    }

    public void deleteCompany(Long id) {
        Company company = getCompanyById(id);

        if (jobApplicationRepository.existsByCompanyId(id)) {
            throw new CompanyHasJobApplicationsException(id);
        }

        companyRepository.delete(company);
        activityLogService.log(
                ActivityEntityType.COMPANY,
                company.getId(),
                ActivityAction.DELETED,
                "Company deleted: " + company.getName()
        );
    }
}