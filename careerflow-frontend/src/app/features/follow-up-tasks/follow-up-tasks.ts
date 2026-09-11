import { DatePipe } from '@angular/common';
import { Component, OnInit, signal } from '@angular/core';

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