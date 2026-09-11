import { DatePipe } from '@angular/common';
import { Component, OnInit, signal } from '@angular/core';

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

  getOutcomeClass(outcome: string): string {
    return `outcome-badge--${outcome.toLowerCase()}`;
  }
}