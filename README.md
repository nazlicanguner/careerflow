# CareerFlow

CareerFlow is a full-stack job-search management application. It helps a user keep track of companies, job applications, interviews, follow-up tasks and the activity history created by those actions.

The project uses Spring Boot and Angular, with PostgreSQL for the operational workflow data and MongoDB for append-only activity logs.

## Features

* Create, view, update and delete companies and job applications
* Track application status, work mode, location and application date
* Schedule interviews and record follow-up tasks
* Display dashboard metrics and application status totals
* Keep an activity history for created, updated and deleted records
* Filter and sort job applications
* Expose REST endpoints through Swagger UI
* Summarise activity logs with a MongoDB aggregation query

## Technology Stack

| Area                | Technology             |
| ------------------- | ---------------------- |
| Backend             | Java, Spring Boot 4    |
| Frontend            | Angular 22, TypeScript |
| Relational database | PostgreSQL             |
| Document database   | MongoDB                |
| API documentation   | OpenAPI / Swagger UI   |
| Backend testing     | Maven and JUnit        |
| Frontend build      | Angular CLI and npm    |

## Project Structure

```text
careerflow/
├── careerflow-backend/
│   ├── src/main/java/
│   ├── src/main/resources/
│   └── pom.xml
└── careerflow-frontend/
    ├── src/
    └── package.json
```

## Database Design

CareerFlow uses a hybrid database design because the main workflow and the audit history have different requirements.

### PostgreSQL

PostgreSQL is the source of truth for the current operational state of the application.

| Table              | Purpose                                         | Current records |
| ------------------ | ----------------------------------------------- | --------------: |
| `companies`        | Stores company details                          |             122 |
| `job_applications` | Stores applications linked to companies         |             122 |
| `interviews`       | Stores interview stages linked to applications  |             111 |
| `follow_up_tasks`  | Stores follow-up actions linked to applications |             132 |

Foreign keys preserve the relationships between companies, job applications, interviews and follow-up tasks. Indexes support the most common filtering, sorting and date-based queries.

### MongoDB

MongoDB stores the activity history in the `careerflow_logs.activity_logs` collection. Activity logs are separate from the current operational data because they describe events that have already happened, including events for relational records that may later be deleted.

Each document contains:

```text
id
entityType
entityId
action
message
occurredAt
```

`entityType` and `entityId` form a logical reference to the related PostgreSQL record. The collection currently contains 250 documents.

The MongoDB collection has indexes on:

* `_id`
* `entityType`
* `entityId`
* `occurredAt`

## MongoDB Aggregation

The endpoint below groups activity logs by entity type and action, then returns the number of records in each group.

```text
GET /api/activity-logs/summary
```

Example response:

```json
[
  {
    "entityType": "COMPANY",
    "action": "CREATED",
    "count": 22
  },
  {
    "entityType": "COMPANY",
    "action": "UPDATED",
    "count": 21
  }
]
```

## Prerequisites

Install and run the following locally:

* JDK 26, or a compatible JDK configured through `JAVA_HOME`
* PostgreSQL
* MongoDB Community Server
* Node.js and npm

The frontend was developed with Angular 22 and npm 11.19.0.

## Local Setup

### 1. Create the PostgreSQL database

Create a local database named `careerflow_db`.

```sql
CREATE DATABASE careerflow_db;
```

### 2. Configure the backend

The backend configuration is in:

```text
careerflow-backend/src/main/resources/application.properties
```

Set the PostgreSQL password as an environment variable before starting the backend.

Windows PowerShell example:

```powershell
$env:JAVA_HOME = "C:\path\to\your\jdk"
$env:DB_PASSWORD = "your_postgresql_password"

cd careerflow-backend
.\mvnw.cmd spring-boot:run
```

The application connects to:

```text
PostgreSQL: careerflow_db
MongoDB:    careerflow_logs
```

Hibernate creates or updates the PostgreSQL schema, and the application provides seeded demonstration data when a new database is used.

### 3. Run the frontend

Open a second terminal:

```powershell
cd careerflow-frontend
npm install
npm start
```

If PowerShell blocks npm scripts, use:

```powershell
npm.cmd install
npm.cmd start
```

The Angular application is available at:

```text
http://localhost:4200
```

## API Documentation

Swagger UI is available while the backend is running:

```text
http://localhost:8080/swagger-ui/index.html
```

Main API areas include:

* `/api/companies`
* `/api/job-applications`
* `/api/interviews`
* `/api/follow-up-tasks`
* `/api/activity-logs`
* `/api/activity-logs/summary`
* `/api/dashboard/summary`

## Verification

Run backend tests from the backend folder:

```powershell
.\mvnw.cmd test
```

The current backend test run completes successfully with three tests and no failures.

Build the frontend from the frontend folder:

```powershell
npm.cmd run build
```

The generated frontend bundle is placed in:

```text
careerflow-frontend/dist/careerflow-frontend
```
