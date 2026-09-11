import { DatePipe } from '@angular/common';
import { Component, computed, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { Company } from '../../core/models/company.model';
import {
  ApplicationStatus,
  JobApplication,
  JobApplicationFilters,
  JobApplicationPayload,
  WorkMode,
} from '../../core/models/job-application.model';
import { CompaniesApi } from '../../core/services/companies-api';
import { JobApplicationsApi } from '../../core/services/job-applications-api';

interface ApplicationForm {
  companyId: number | null;
  positionTitle: string;
  jobUrl: string;
  location: string;
  status: ApplicationStatus;
  workMode: WorkMode | '';
  source: string;
  applicationDate: string;
  notes: string;
}

@Component({
  selector: 'app-job-applications',
  imports: [DatePipe, FormsModule],
  templateUrl: './job-applications.html',
  styleUrl: './job-applications.scss',
})
export class JobApplications implements OnInit {
  readonly applications = signal<JobApplication[]>([]);
  readonly companies = signal<Company[]>([]);
  readonly isLoading = signal(true);
  readonly errorMessage = signal('');
  readonly formError = signal('');
  readonly isFormOpen = signal(false);

  readonly pageSize = 10;
  readonly currentPage = signal(1);

  readonly totalPages = computed(() =>
    Math.ceil(this.applications().length / this.pageSize),
  );

  readonly paginatedApplications = computed(() => {
    const startIndex = (this.currentPage() - 1) * this.pageSize;

    return this.applications().slice(startIndex, startIndex + this.pageSize);
  });

  readonly pageNumbers = computed(() =>
    Array.from({ length: this.totalPages() }, (_, index) => index + 1),
  );

  readonly statuses: ApplicationStatus[] = [
    'SAVED',
    'APPLIED',
    'SCREENING',
    'INTERVIEWING',
    'OFFER',
    'REJECTED',
    'WITHDRAWN',
  ];

  readonly workModes: WorkMode[] = ['ONSITE', 'HYBRID', 'REMOTE'];

  statusFilter: ApplicationStatus | '' = '';
  workModeFilter: WorkMode | '' = '';
  sortBy = 'applicationDate';
  direction: 'asc' | 'desc' = 'desc';

  form: ApplicationForm = this.emptyForm();

  constructor(
    private readonly jobApplicationsApi: JobApplicationsApi,
    private readonly companiesApi: CompaniesApi,
  ) {}

  ngOnInit(): void {
    this.loadApplications();
    this.loadCompanies();
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
        this.currentPage.set(1);
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

  loadCompanies(): void {
    this.companiesApi.getAll().subscribe({
      next: (companies) => this.companies.set(companies),
      error: () =>
        this.formError.set(
          'Companies could not be loaded. Add or check companies first.',
        ),
    });
  }

  saveApplication(): void {
    if (!this.form.companyId || !this.form.positionTitle.trim()) {
      this.formError.set('Company and position title are required.');
      return;
    }

    const payload: JobApplicationPayload = {
      positionTitle: this.form.positionTitle.trim(),
      jobUrl: this.nullIfEmpty(this.form.jobUrl),
      location: this.nullIfEmpty(this.form.location),
      status: this.form.status,
      workMode: this.form.workMode || null,
      source: this.nullIfEmpty(this.form.source),
      applicationDate: this.form.applicationDate || null,
      notes: this.nullIfEmpty(this.form.notes),
    };

    this.formError.set('');

    this.jobApplicationsApi.create(this.form.companyId, payload).subscribe({
      next: () => {
        this.closeForm();
        this.loadApplications();
      },
      error: () => {
        this.formError.set(
          'Application could not be saved. Check the entered data and try again.',
        );
      },
    });
  }

  openForm(): void {
    this.form = this.emptyForm();
    this.formError.set('');
    this.isFormOpen.set(true);
  }

  closeForm(): void {
    this.form = this.emptyForm();
    this.formError.set('');
    this.isFormOpen.set(false);
  }

  clearFilters(): void {
    this.statusFilter = '';
    this.workModeFilter = '';
    this.sortBy = 'applicationDate';
    this.direction = 'desc';
    this.loadApplications();
  }

  previousPage(): void {
    if (this.currentPage() > 1) {
      this.currentPage.update((page) => page - 1);
    }
  }

  nextPage(): void {
    if (this.currentPage() < this.totalPages()) {
      this.currentPage.update((page) => page + 1);
    }
  }

  goToPage(page: number): void {
    if (page >= 1 && page <= this.totalPages()) {
      this.currentPage.set(page);
    }
  }

  getStatusClass(status: ApplicationStatus): string {
    return `status-badge--${status.toLowerCase()}`;
  }

  private emptyForm(): ApplicationForm {
    return {
      companyId: null,
      positionTitle: '',
      jobUrl: '',
      location: '',
      status: 'APPLIED',
      workMode: '',
      source: '',
      applicationDate: '',
      notes: '',
    };
  }

  private nullIfEmpty(value: string): string | null {
    return value.trim() || null;
  }
}