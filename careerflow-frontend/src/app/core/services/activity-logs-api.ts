import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ActivityLog } from '../models/activity-log.model';

@Injectable({
  providedIn: 'root',
})
export class ActivityLogsApi {
  private readonly apiUrl = 'http://localhost:8080/api/activity-logs';

  constructor(private readonly http: HttpClient) {}

  getAll(): Observable<ActivityLog[]> {
    return this.http.get<ActivityLog[]>(this.apiUrl);
  }
}