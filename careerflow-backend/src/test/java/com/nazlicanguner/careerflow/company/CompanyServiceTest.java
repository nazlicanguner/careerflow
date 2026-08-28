package com.nazlicanguner.careerflow.company;

import com.nazlicanguner.careerflow.jobapplication.JobApplicationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private JobApplicationRepository jobApplicationRepository;

    @InjectMocks
    private CompanyService companyService;

    @Test
    void deleteCompanyThrowsExceptionWhenCompanyHasJobApplications() {
        Long companyId = 1L;
        Company company = new Company();

        when(companyRepository.findById(companyId))
                .thenReturn(Optional.of(company));
        when(jobApplicationRepository.existsByCompanyId(companyId))
                .thenReturn(true);

        assertThrows(
                CompanyHasJobApplicationsException.class,
                () -> companyService.deleteCompany(companyId)
        );

        verify(companyRepository, never()).delete(company);
    }

    @Test
    void deleteCompanyDeletesCompanyWhenItHasNoJobApplications() {
        Long companyId = 1L;
        Company company = new Company();

        when(companyRepository.findById(companyId))
                .thenReturn(Optional.of(company));
        when(jobApplicationRepository.existsByCompanyId(companyId))
                .thenReturn(false);

        companyService.deleteCompany(companyId);

        verify(companyRepository).delete(company);
    }
}