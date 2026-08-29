package com.nazlicanguner.careerflow.followuptask;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;

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

    @Query("""
        select task
        from FollowUpTask task
        join fetch task.jobApplication jobApplication
        join fetch jobApplication.company
        where (:status is null or task.status = :status)
          and (:jobApplicationId is null or jobApplication.id = :jobApplicationId)
          and (
              :overdue = false
              or (task.dueDate < :today and task.status <> :completedStatus)
          )
        order by task.dueDate asc
        """)
    List<FollowUpTask> findByFilters(
            @Param("status") TaskStatus status,
            @Param("jobApplicationId") Long jobApplicationId,
            @Param("overdue") boolean overdue,
            @Param("today") LocalDate today,
            @Param("completedStatus") TaskStatus completedStatus
    );

    void deleteByJobApplicationId(Long jobApplicationId);
}