import { DatePipe } from '@angular/common';
import { Component, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';

import {
  ApplicationStatus,
  JobApplication,
  JobApplicationFilters,
  WorkMode,
} from '../../core/models/job-application.model';
import { JobApplicationsApi } from '../../core/services/job-applications-api';

@Component({
  selector: 'app-job-applications',
  imports: [
    DatePipe,
    FormsModule,
  ],
  templateUrl: './job-applications.html',
  styleUrl: './job-applications.scss',
})
export class JobApplications implements OnInit {
  readonly applications = signal<JobApplication[]>([]);
  readonly isLoading = signal(true);
  readonly errorMessage = signal('');

  readonly statuses: ApplicationStatus[] = [
    'SAVED',
    'APPLIED',
    'SCREENING',
    'INTERVIEWING',
    'OFFER',
    'REJECTED',
    'WITHDRAWN',
  ];

  readonly workModes: WorkMode[] = [
    'ONSITE',
    'HYBRID',
    'REMOTE',
  ];

  statusFilter: ApplicationStatus | '' = '';
  workModeFilter: WorkMode | '' = '';
  sortBy = 'applicationDate';
  direction: 'asc' | 'desc' = 'desc';

  constructor(
    private readonly jobApplicationsApi: JobApplicationsApi,
  ) {}

  ngOnInit(): void {
    this.loadApplications();
  }

  loadApplications(): void {
    this.isLoading.set(true);
    this.errorMessage.set('');

    const filters: JobApplicationFilters = {
      sortBy: this.sortBy,
      direction: this.direction,
    };

    if (this.statusFilter) {
      filters.status = this.statusFilter;
    }

    if (this.workModeFilter) {
      filters.workMode = this.workModeFilter;
    }

    this.jobApplicationsApi.getAll(filters).subscribe({
      next: (applications) => {
        this.applications.set(applications);
        this.isLoading.set(false);
      },
      error: () => {
        this.errorMessage.set(
          'Job applications could not be loaded. Check that the backend is running.',
        );
        this.isLoading.set(false);
      },
    });
  }

  clearFilters(): void {
    this.statusFilter = '';
    this.workModeFilter = '';
    this.sortBy = 'applicationDate';
    this.direction = 'desc';

    this.loadApplications();
  }

  getStatusClass(status: ApplicationStatus): string {
    return `status-badge--${status.toLowerCase()}`;
  }
}