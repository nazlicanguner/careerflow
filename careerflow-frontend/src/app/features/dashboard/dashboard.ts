import { Component, OnInit, signal } from '@angular/core';

import {
  DashboardApi,
  DashboardSummary,
} from '../../core/services/dashboard-api';

@Component({
  selector: 'app-dashboard',
  imports: [],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss',
})
export class Dashboard implements OnInit {
  readonly summary = signal<DashboardSummary | null>(null);
  readonly isLoading = signal(true);
  readonly errorMessage = signal('');

  constructor(private readonly dashboardApi: DashboardApi) {}

  ngOnInit(): void {
    this.dashboardApi.getSummary().subscribe({
      next: (summary) => {
        this.summary.set(summary);
        this.isLoading.set(false);
      },
      error: () => {
        this.errorMessage.set(
          'Dashboard data could not be loaded. Check that the backend is running.',
        );
        this.isLoading.set(false);
      },
    });
  }
}