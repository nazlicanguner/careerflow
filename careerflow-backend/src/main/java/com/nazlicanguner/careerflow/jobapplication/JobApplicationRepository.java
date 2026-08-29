package com.nazlicanguner.careerflow.jobapplication;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Sort;

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

    @Query("""
        select jobApplication
        from JobApplication jobApplication
        join fetch jobApplication.company
        where (:status is null or jobApplication.status = :status)
          and (:companyId is null or jobApplication.company.id = :companyId)
          and (:workMode is null or jobApplication.workMode = :workMode)
        """)
    List<JobApplication> findByFilters(
            @Param("status") ApplicationStatus status,
            @Param("companyId") Long companyId,
            @Param("workMode") WorkMode workMode,
            Sort sort
    );

    boolean existsByCompanyId(Long companyId);
}