# CareerFlow - Architecture and Design Decisions

## D-001: Separate application status from interview stages

### Decision

JobApplication stores the overall application status, while each interview stage is stored as a separate Interview record.

### Reason

A hiring process may contain multiple interviews with different dates, formats, and outcomes. A single status field cannot preserve this history.

### Consequence

An application can remain in the INTERVIEWING status while its individual interview records show whether each stage is pending, passed, failed, or cancelled.

## D-002: Prevent deletion of companies with applications

### Decision

The system does not allow a Company to be deleted while related JobApplication records exist.

### Reason

Deleting the company would leave related application, interview, and follow-up task records without valid context.

### Consequence

The user must remove or reassign the related applications before deleting the company.

## D-003: Cascade delete records dependent on a job application

### Decision

Deleting a JobApplication also deletes its related Interview and FollowUpTask records.

### Reason

Interview and task records have no meaningful context without their parent JobApplication.

### Consequence

The user must confirm the deletion because the related records cannot remain in the system.

## D-004: Keep interview outcomes separate from application status updates

### Decision

Updating an Interview outcome does not automatically update the parent JobApplication status.

### Reason

Interview outcomes may not fully represent the final state of an application. The user must retain control over the overall application status.

### Consequence

The application status is updated explicitly by the user, even after an interview outcome is recorded.

## D-005: Use a simple completion model for follow-up tasks

### Decision

FollowUpTask uses a completed boolean and completedAt timestamp instead of multiple task statuses.

### Reason

The MVP only needs to distinguish pending tasks from completed tasks. Additional states such as in progress or blocked would add complexity without a clear user need.

### Consequence

Task management remains simple while preserving the time when a task was completed.