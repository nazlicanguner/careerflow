import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Interview } from '../models/interview.model';

@Injectable({
  providedIn: 'root',
})
export class InterviewsApi {
  private readonly apiUrl = 'http://localhost:8080/api/interviews';

  constructor(private readonly http: HttpClient) {}

  getAll(): Observable<Interview[]> {
    return this.http.get<Interview[]>(this.apiUrl);
  }
}