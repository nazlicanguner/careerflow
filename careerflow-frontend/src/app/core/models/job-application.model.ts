export type ApplicationStatus =
  | 'SAVED'
  | 'APPLIED'
  | 'SCREENING'
  | 'INTERVIEWING'
  | 'OFFER'
  | 'REJECTED'
  | 'WITHDRAWN';

export type WorkMode = 'ONSITE' | 'HYBRID' | 'REMOTE';

export interface CompanyReference {
  id: number;
  name: string;
}

export interface JobApplication {
  id: number;
  company: CompanyReference;
  positionTitle: string;
  jobUrl: string | null;
  location: string | null;
  status: ApplicationStatus;
  workMode: WorkMode | null;
  source: string | null;
  applicationDate: string | null;
  notes: string | null;
  createdAt: string;
  updatedAt: string;
}

export interface JobApplicationFilters {
  status?: ApplicationStatus;
  companyId?: number;
  workMode?: WorkMode;
  sortBy?: string;
  direction?: 'asc' | 'desc';
}

export interface JobApplicationPayload {
  positionTitle: string;
  jobUrl: string | null;
  location: string | null;
  status: ApplicationStatus;
  workMode: WorkMode | null;
  source: string | null;
  applicationDate: string | null;
  notes: string | null;
}