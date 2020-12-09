export class TestCaseResult {
  id: number;
  userId: number;
  name: string;
  testCaseId: number;
  status: ExecutionStatus;
  startDate = '';
  endDate = '';
  innerResults: ActionResult[];
}

export class ActionResult {
  id: number;
  name: string;
  startDate = '';
  endDate = '';
  extra?: { [name: string]: string };
  result?: boolean;
  status: ActionStatus;
  type: ActionType;

  // rest
  restInfo?: string;
  // technical
  technicalInfo?: string;

}

export enum ActionStatus {
  PASSED = 'PASSED',
  FAILED = 'FAILED',
  STOPPED = 'STOPPED',
  NOT_STARTED = 'NOT_STARTED'
}

export enum ExecutionStatus {
  NOT_STARTED = 'NOT_STARTED',
  IN_PROGRESS = 'IN_PROGRESS',
  FINISHED = 'FINISHED',
  SUSPENDED = 'SUSPENDED',
  RESUMED = 'RESUMED',
  FAILED = 'FAILED'
}

export enum ActionType {
  REST = 'REST',
  SQL = 'SQL',
  TECHNICAL = 'TECHNICAL',
  UI = 'UI'
}
