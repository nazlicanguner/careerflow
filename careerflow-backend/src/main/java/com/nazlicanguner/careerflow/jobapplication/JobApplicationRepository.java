package com.nazlicanguner.careerflow.jobapplication;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JobApplicationRepository
        extends JpaRepository<JobApplication, Long> {

    @Query("""
            select jobApplication
            from JobApplication jobApplication
            join fetch jobApplication.company
            """)
    List<JobApplication> findAllWithCompany();

    @Query("""
            select jobApplication
            from JobApplication jobApplication
            join fetch jobApplication.company
            where jobApplication.id = :id
            """)
    Optional<JobApplication> findByIdWithCompany(
            @Param("id") Long id
    );

    boolean existsByCompanyId(Long companyId);
}