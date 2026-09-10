import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ApplicationStatusSummary {
  status: string;
  count: number;
}

export interface DashboardSummary {
  applicationsByStatus: ApplicationStatusSummary[];
  upcomingInterviews: number;
  openTasks: number;
  overdueTasks: number;
}

@Injectable({
  providedIn: 'root',
})
export class DashboardApi {
  private readonly apiUrl = 'http://localhost:8080/api/dashboard/summary';

  constructor(private readonly http: HttpClient) {}

  getSummary(): Observable<DashboardSummary> {
    return this.http.get<DashboardSummary>(this.apiUrl);
  }
}