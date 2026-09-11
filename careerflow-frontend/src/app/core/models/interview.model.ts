import { JobApplication } from './job-application.model';

export interface Interview {
  id: number;
  jobApplication: JobApplication;
  stageName: string;
  stageNumber: number;
  interviewType: string;
  scheduledAt: string;
  outcome: string;
  notes: string | null;
  createdAt: string;
  updatedAt: string;
}