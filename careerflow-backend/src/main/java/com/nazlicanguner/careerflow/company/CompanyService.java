package com.nazlicanguner.careerflow.company;

import org.springframework.stereotype.Service;
import com.nazlicanguner.careerflow.jobapplication.JobApplicationRepository;

import java.util.List;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final JobApplicationRepository jobApplicationRepository;

    public CompanyService(
            CompanyRepository companyRepository,
            JobApplicationRepository jobApplicationRepository
    ) {
        this.companyRepository = companyRepository;
        this.jobApplicationRepository = jobApplicationRepository;
    }
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public Company createCompany(Company company) {
        return companyRepository.save(company);
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

        return companyRepository.save(existingCompany);
    }

    public void deleteCompany(Long id) {
        Company company = getCompanyById(id);

        if (jobApplicationRepository.existsByCompanyId(id)) {
            throw new CompanyHasJobApplicationsException(id);
        }

        companyRepository.delete(company);
    }
}