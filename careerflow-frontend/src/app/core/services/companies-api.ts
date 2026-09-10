import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Company, CompanyPayload } from '../models/company.model';

@Injectable({
  providedIn: 'root',
})
export class CompaniesApi {
  private readonly apiUrl = 'http://localhost:8080/api/companies';

  constructor(private readonly http: HttpClient) {}

  getAll(): Observable<Company[]> {
    return this.http.get<Company[]>(this.apiUrl);
  }

  create(payload: CompanyPayload): Observable<Company> {
    return this.http.post<Company>(this.apiUrl, payload);
  }

  update(id: number, payload: CompanyPayload): Observable<Company> {
    return this.http.put<Company>(`${this.apiUrl}/${id}`, payload);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}