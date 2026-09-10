import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import {
  JobApplication,
  JobApplicationFilters,
  JobApplicationPayload,
} from '../models/job-application.model';

@Injectable({
  providedIn: 'root',
})
export class JobApplicationsApi {
  private readonly apiUrl = 'http://localhost:8080/api/job-applications';

  constructor(private readonly http: HttpClient) {}

  getAll(
    filters: JobApplicationFilters = {},
  ): Observable<JobApplication[]> {
    let params = new HttpParams();

    if (filters.status) {
      params = params.set('status', filters.status);
    }

    if (filters.companyId !== undefined) {
      params = params.set('companyId', filters.companyId);
    }

    if (filters.workMode) {
      params = params.set('workMode', filters.workMode);
    }

    if (filters.sortBy) {
      params = params.set('sortBy', filters.sortBy);
    }

    if (filters.direction) {
      params = params.set('direction', filters.direction);
    }

    return this.http.get<JobApplication[]>(this.apiUrl, { params });
  }

  create(
  companyId: number,
  payload: JobApplicationPayload,
): Observable<JobApplication> {
  const params = new HttpParams().set('companyId', companyId);

  return this.http.post<JobApplication>(this.apiUrl, payload, { params });
}
}