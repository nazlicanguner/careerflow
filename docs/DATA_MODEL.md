# CareerFlow - Data Model

## Overview

CareerFlow stores job-search data through four main entities:

- Company
- JobApplication
- Interview
- FollowUpTask

## Relationships

- One Company can have many JobApplications.
- One JobApplication belongs to one Company.
- One JobApplication can have many Interviews.
- One JobApplication can have many FollowUpTasks.

## Application Status and Interview Stages

A JobApplication stores the overall current status of an application:

- SAVED
- APPLIED
- SCREENING
- INTERVIEWING
- OFFER
- REJECTED
- WITHDRAWN

Interview stages are stored as separate Interview records. This preserves the history of multi-stage hiring processes.

Each Interview record has its own outcome:

- PENDING
- PASSED
- FAILED
- CANCELLED

For example, one application can contain:

1. HR Interview — PASSED
2. Technical Interview — PASSED
3. Final Interview — PENDING