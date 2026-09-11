import { JobApplication } from './job-application.model';

export interface FollowUpTask {
  id: number;
  jobApplication: JobApplication;
  title: string;
  notes: string | null;
  dueDate: string;
  status: string;
  createdAt: string;
  updatedAt: string;
}