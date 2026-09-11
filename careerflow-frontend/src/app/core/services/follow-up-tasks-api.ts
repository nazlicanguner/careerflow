import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { FollowUpTask } from '../models/follow-up-task.model';

@Injectable({
  providedIn: 'root',
})
export class FollowUpTasksApi {
  private readonly apiUrl = 'http://localhost:8080/api/follow-up-tasks';

  constructor(private readonly http: HttpClient) {}

  getAll(): Observable<FollowUpTask[]> {
    return this.http.get<FollowUpTask[]>(this.apiUrl);
  }
}