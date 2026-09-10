import { Component, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { Company, CompanyPayload } from '../../core/models/company.model';
import { CompaniesApi } from '../../core/services/companies-api';

@Component({
  selector: 'app-companies',
  imports: [FormsModule],
  templateUrl: './companies.html',
  styleUrl: './companies.scss',
})
export class Companies implements OnInit {
  readonly companies = signal<Company[]>([]);
  readonly isLoading = signal(true);
  readonly errorMessage = signal('');
  readonly formError = signal('');
  readonly editingCompany = signal<Company | null>(null);

  form: CompanyPayload = this.emptyForm();

  constructor(private readonly companiesApi: CompaniesApi) {}

  ngOnInit(): void {
    this.loadCompanies();
  }

  loadCompanies(): void {
    this.isLoading.set(true);
    this.errorMessage.set('');

    this.companiesApi.getAll().subscribe({
      next: (companies) => {
        this.companies.set(companies);
        this.isLoading.set(false);
      },
      error: () => {
        this.errorMessage.set(
          'Companies could not be loaded. Check that the backend is running.',
        );
        this.isLoading.set(false);
      },
    });
  }

  saveCompany(): void {
    if (!this.form.name.trim()) {
      this.formError.set('Company name is required.');
      return;
    }

    this.formError.set('');
    const payload: CompanyPayload = {
      name: this.form.name.trim(),
      industry: this.nullIfEmpty(this.form.industry),
      location: this.nullIfEmpty(this.form.location),
      website: this.nullIfEmpty(this.form.website),
      notes: this.nullIfEmpty(this.form.notes),
    };

    const company = this.editingCompany();

    const request = company
      ? this.companiesApi.update(company.id, payload)
      : this.companiesApi.create(payload);

    request.subscribe({
      next: () => {
        this.resetForm();
        this.loadCompanies();
      },
      error: () => {
        this.formError.set(
          'Company could not be saved. Check the entered data and try again.',
        );
      },
    });
  }

  editCompany(company: Company): void {
    this.editingCompany.set(company);
    this.form = {
      name: company.name,
      industry: company.industry,
      location: company.location,
      website: company.website,
      notes: company.notes,
    };
    this.formError.set('');
  }

  deleteCompany(company: Company): void {
    const confirmed = window.confirm(
      `"${company.name}" will be deleted. Do you want to continue?`,
    );

    if (!confirmed) {
      return;
    }

    this.companiesApi.delete(company.id).subscribe({
      next: () => {
        if (this.editingCompany()?.id === company.id) {
          this.resetForm();
        }

        this.loadCompanies();
      },
      error: () => {
        this.errorMessage.set(
          'This company could not be deleted. It may still be linked to a job application.',
        );
      },
    });
  }

  resetForm(): void {
    this.editingCompany.set(null);
    this.form = this.emptyForm();
    this.formError.set('');
  }

  private emptyForm(): CompanyPayload {
    return {
      name: '',
      industry: null,
      location: null,
      website: null,
      notes: null,
    };
  }

  private nullIfEmpty(value: string | null): string | null {
    return value?.trim() || null;
  }
}