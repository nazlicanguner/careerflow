import { Routes } from '@angular/router';

import { ActivityLogs } from './features/activity-logs/activity-logs';
import { Companies } from './features/companies/companies';
import { Dashboard } from './features/dashboard/dashboard';
import { FollowUpTasks } from './features/follow-up-tasks/follow-up-tasks';
import { Interviews } from './features/interviews/interviews';
import { JobApplications } from './features/job-applications/job-applications';

export const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'dashboard',
  },
  {
    path: 'dashboard',
    component: Dashboard,
  },
  {
    path: 'applications',
    component: JobApplications,
  },
  {
    path: 'companies',
    component: Companies,
  },
  {
    path: 'interviews',
    component: Interviews,
  },
  {
    path: 'tasks',
    component: FollowUpTasks,
  },
  {
    path: 'activity',
    component: ActivityLogs,
  },
  {
    path: '**',
    redirectTo: 'dashboard',
  },
];