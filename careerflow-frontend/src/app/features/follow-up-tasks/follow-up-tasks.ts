import { DatePipe } from '@angular/common';
import { Component, computed, OnInit, signal } from '@angular/core';

import { FollowUpTask } from '../../core/models/follow-up-task.model';
import { FollowUpTasksApi } from '../../core/services/follow-up-tasks-api';

@Component({
  selector: 'app-follow-up-tasks',
  imports: [DatePipe],
  templateUrl: './follow-up-tasks.html',
  styleUrl: './follow-up-tasks.scss',
})
export class FollowUpTasks implements OnInit {
  readonly tasks = signal<FollowUpTask[]>([]);
  readonly isLoading = signal(true);
  readonly errorMessage = signal('');

  readonly pageSize = 10;
  readonly currentPage = signal(1);

  readonly totalPages = computed(() =>
    Math.ceil(this.tasks().length / this.pageSize),
  );

  readonly paginatedTasks = computed(() => {
    const startIndex = (this.currentPage() - 1) * this.pageSize;

    return this.tasks().slice(startIndex, startIndex + this.pageSize);
  });

  readonly pageNumbers = computed(() =>
    Array.from({ length: this.totalPages() }, (_, index) => index + 1),
  );

  constructor(private readonly followUpTasksApi: FollowUpTasksApi) {}

  ngOnInit(): void {
    this.loadTasks();
  }

  loadTasks(): void {
    this.isLoading.set(true);
    this.errorMessage.set('');

    this.followUpTasksApi.getAll().subscribe({
      next: (tasks) => {
        this.tasks.set(tasks);
        this.currentPage.set(1);
        this.isLoading.set(false);
      },
      error: () => {
        this.errorMessage.set(
          'Follow-up tasks could not be loaded. Check that the backend is running.',
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

  getStatusClass(status: string): string {
    return `task-status--${status.toLowerCase()}`;
  }

  isOverdue(task: FollowUpTask): boolean {
    return (
      task.status !== 'COMPLETED' &&
      new Date(task.dueDate).getTime() < new Date().getTime()
    );
  }
}