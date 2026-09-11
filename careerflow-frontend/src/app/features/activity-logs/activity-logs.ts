import { DatePipe } from '@angular/common';
import { Component, OnInit, signal } from '@angular/core';

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

  getActionClass(action: ActivityAction): string {
    return `activity-item--${action.toLowerCase()}`;
  }

  formatEntityType(entityType: string): string {
    return entityType.replaceAll('_', ' ');
  }
}