export interface Company {
  id: number;
  name: string;
  industry: string | null;
  location: string | null;
  website: string | null;
  notes: string | null;
  createdAt: string;
  updatedAt: string;
}

export interface CompanyPayload {
  name: string;
  industry: string | null;
  location: string | null;
  website: string | null;
  notes: string | null;
}