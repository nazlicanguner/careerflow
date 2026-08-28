package com.nazlicanguner.careerflow.followuptask;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.List;

public interface FollowUpTaskRepository extends JpaRepository<FollowUpTask, Long> {

    @Query("""
            select task
            from FollowUpTask task
            join fetch task.jobApplication jobApplication
            join fetch jobApplication.company
            """)
    List<FollowUpTask> findAllWithJobApplication();

    @Query("""
            select task
            from FollowUpTask task
            join fetch task.jobApplication jobApplication
            join fetch jobApplication.company
            where task.id = :id
            """)
    Optional<FollowUpTask> findByIdWithJobApplication(@Param("id") Long id);
}