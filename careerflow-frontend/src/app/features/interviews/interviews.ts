import { DatePipe } from '@angular/common';
import { Component, computed, OnInit, signal } from '@angular/core';

import { Interview } from '../../core/models/interview.model';
import { InterviewsApi } from '../../core/services/interviews-api';

@Component({
  selector: 'app-interviews',
  imports: [DatePipe],
  templateUrl: './interviews.html',
  styleUrl: './interviews.scss',
})
export class Interviews implements OnInit {
  readonly interviews = signal<Interview[]>([]);
  readonly isLoading = signal(true);
  readonly errorMessage = signal('');

  readonly pageSize = 10;
  readonly currentPage = signal(1);

  readonly totalPages = computed(() =>
    Math.ceil(this.interviews().length / this.pageSize),
  );

  readonly paginatedInterviews = computed(() => {
    const startIndex = (this.currentPage() - 1) * this.pageSize;

    return this.interviews().slice(startIndex, startIndex + this.pageSize);
  });

  readonly pageNumbers = computed(() =>
    Array.from({ length: this.totalPages() }, (_, index) => index + 1),
  );

  constructor(private readonly interviewsApi: InterviewsApi) {}

  ngOnInit(): void {
    this.loadInterviews();
  }

  loadInterviews(): void {
    this.isLoading.set(true);
    this.errorMessage.set('');

    this.interviewsApi.getAll().subscribe({
      next: (interviews) => {
        this.interviews.set(interviews);
        this.currentPage.set(1);
        this.isLoading.set(false);
      },
      error: () => {
        this.errorMessage.set(
          'Interviews could not be loaded. Check that the backend is running.',
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

  getOutcomeClass(outcome: string): string {
    return `outcome-badge--${outcome.toLowerCase()}`;
  }
}