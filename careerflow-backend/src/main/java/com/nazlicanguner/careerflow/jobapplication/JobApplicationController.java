package com.nazlicanguner.careerflow.jobapplication;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Job Applications", description = "Manage job applications.")
@RestController
@RequestMapping("/api/job-applications")
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;

    public JobApplicationController(
            JobApplicationService jobApplicationService
    ) {
        this.jobApplicationService = jobApplicationService;
    }

    @GetMapping
    public List<JobApplication> getAllJobApplications(
            @RequestParam(required = false) ApplicationStatus status,
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) WorkMode workMode,
            @RequestParam(defaultValue = "applicationDate") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        return jobApplicationService.searchJobApplications(
                status,
                companyId,
                workMode,
                sortBy,
                direction
        );
    }

    @GetMapping("/{id}")
    public JobApplication getJobApplicationById(
            @PathVariable("id") Long id
    ) {
        return jobApplicationService.getJobApplicationById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public JobApplication createJobApplication(
            @RequestParam Long companyId,
            @Valid @RequestBody JobApplication jobApplication
    ) {
        return jobApplicationService.createJobApplication(
                companyId,
                jobApplication
        );
    }

    @PutMapping("/{id}")
    public JobApplication updateJobApplication(
            @PathVariable("id") Long id,
            @Valid @RequestBody JobApplication jobApplication
    ) {
        return jobApplicationService.updateJobApplication(
                id,
                jobApplication
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteJobApplication(
            @PathVariable("id") Long id
    ) {
        jobApplicationService.deleteJobApplication(id);
    }
}