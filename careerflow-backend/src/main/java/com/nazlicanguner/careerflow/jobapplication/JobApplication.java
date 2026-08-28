package com.nazlicanguner.careerflow.jobapplication;

import com.nazlicanguner.careerflow.company.Company;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "job_applications")
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @NotBlank(message = "Position title must not be blank.")
    @Size(max = 160, message = "Position title must be at most 160 characters.")
    @Column(nullable = false, length = 160)
    private String positionTitle;

    @Size(max = 500, message = "Job URL must be at most 500 characters.")
    @Column(length = 500)
    private String jobUrl;

    @Size(max = 120, message = "Location must be at most 120 characters.")
    @Column(length = 120)
    private String location;

    @NotNull(message = "Application status is required.")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ApplicationStatus status = ApplicationStatus.SAVED;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private WorkMode workMode;

    @Size(max = 120, message = "Source must be at most 120 characters.")
    @Column(length = 120)
    private String source;

    private LocalDate applicationDate;

    @Size(max = 2000, message = "Notes must be at most 2000 characters.")
    @Column(length = 2000)
    private String notes;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    private void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    private void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Company getCompany() {
        return company;
    }

    public String getPositionTitle() {
        return positionTitle;
    }

    public String getJobUrl() {
        return jobUrl;
    }

    public String getLocation() {
        return location;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public WorkMode getWorkMode() {
        return workMode;
    }

    public String getSource() {
        return source;
    }

    public LocalDate getApplicationDate() {
        return applicationDate;
    }

    public String getNotes() {
        return notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public void setPositionTitle(String positionTitle) {
        this.positionTitle = positionTitle;
    }

    public void setJobUrl(String jobUrl) {
        this.jobUrl = jobUrl;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public void setWorkMode(WorkMode workMode) {
        this.workMode = workMode;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setApplicationDate(LocalDate applicationDate) {
        this.applicationDate = applicationDate;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}