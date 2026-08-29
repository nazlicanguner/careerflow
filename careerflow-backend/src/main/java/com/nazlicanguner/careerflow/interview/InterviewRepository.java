package com.nazlicanguner.careerflow.interview;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;

public interface InterviewRepository extends JpaRepository<Interview, Long> {

    @Query("""
            select interview
            from Interview interview
            join fetch interview.jobApplication jobApplication
            join fetch jobApplication.company
            """)
    List<Interview> findAllWithJobApplication();

    @Query("""
            select interview
            from Interview interview
            join fetch interview.jobApplication jobApplication
            join fetch jobApplication.company
            where interview.id = :id
            """)
    Optional<Interview> findByIdWithJobApplication(
            @Param("id") Long id
    );

    boolean existsByJobApplicationIdAndStageNumber(
            Long jobApplicationId,
            Integer stageNumber
    );

    @Query("""
        select interview
        from Interview interview
        join fetch interview.jobApplication jobApplication
        join fetch jobApplication.company
        where (:jobApplicationId is null or jobApplication.id = :jobApplicationId)
          and (:upcoming = false or interview.scheduledAt >= :now)
        order by interview.scheduledAt asc
        """)
    List<Interview> findByFilters(
            @Param("jobApplicationId") Long jobApplicationId,
            @Param("upcoming") boolean upcoming,
            @Param("now") LocalDateTime now
    );

    @Query("""
        select count(interview)
        from Interview interview
        where interview.scheduledAt >= :now
        """)
    long countUpcomingInterviews(@Param("now") LocalDateTime now);

    void deleteByJobApplicationId(Long jobApplicationId);
}