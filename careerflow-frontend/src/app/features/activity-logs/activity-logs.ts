import { DatePipe } from '@angular/common';
import { Component, computed, OnInit, signal } from '@angular/core';

import {
  ActivityAction,
  ActivityLog,
} from '../../core/models/activity-log.model';
import { ActivityLogsApi } from '../../core/services/activity-logs-api';

@Component({
  selector: 'app-activity-logs',
  imports: [DatePipe],
  templateUrl: './activity-logs.html',
  styleUrl: './activity-logs.scss',
})
export class ActivityLogs implements OnInit {
  readonly logs = signal<ActivityLog[]>([]);
  readonly isLoading = signal(true);
  readonly errorMessage = signal('');

  readonly pageSize = 10;
  readonly currentPage = signal(1);

  readonly totalPages = computed(() =>
    Math.ceil(this.logs().length / this.pageSize),
  );

  readonly paginatedLogs = computed(() => {
    const startIndex = (this.currentPage() - 1) * this.pageSize;

    return this.logs().slice(startIndex, startIndex + this.pageSize);
  });

  readonly pageNumbers = computed(() =>
    Array.from({ length: this.totalPages() }, (_, index) => index + 1),
  );

  constructor(private readonly activityLogsApi: ActivityLogsApi) {}

  ngOnInit(): void {
    this.loadLogs();
  }

  loadLogs(): void {
    this.isLoading.set(true);
    this.errorMessage.set('');

    this.activityLogsApi.getAll().subscribe({
      next: (logs) => {
        this.logs.set(logs);
        this.currentPage.set(1);
        this.isLoading.set(false);
      },
      error: () => {
        this.errorMessage.set(
          'Activity logs could not be loaded. Check that the backend is running.',
        );
        this.isLoading.set(false);
      },
    });
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

  getActionClass(action: ActivityAction): string {
    return `activity-item--${action.toLowerCase()}`;
  }

  formatEntityType(entityType: string): string {
    return entityType.replaceAll('_', ' ');
  }
}