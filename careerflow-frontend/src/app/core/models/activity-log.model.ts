export type ActivityAction = 'CREATED' | 'UPDATED' | 'DELETED';

export interface ActivityLog {
  id: string;
  action: ActivityAction;
  entityId: number;
  entityType: string;
  message: string;
  occurredAt: string;
}