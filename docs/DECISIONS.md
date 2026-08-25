# CareerFlow - Architecture and Design Decisions

## D-001: Separate application status from interview stages

### Decision

JobApplication stores the overall application status, while each interview stage is stored as a separate Interview record.

### Reason

A hiring process may contain multiple interviews with different dates, formats, and outcomes. A single status field cannot preserve this history.

### Consequence

An application can remain in the INTERVIEWING status while its individual interview records show whether each stage is pending, passed, failed, or cancelled.