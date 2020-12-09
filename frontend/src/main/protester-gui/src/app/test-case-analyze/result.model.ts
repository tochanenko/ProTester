export class TestCaseResult {
  id: number;
  userId: number;
  testCaseId: number;
  statusId: number;
  startDate = '';
  endDate = '';
  innerResults: ActionResult[];
}

export class ActionResult {
  id: number;
  startDate = '';
  endDate = '';
  extra?: { [name: string]: string };
  result: boolean;

  // rest
  restInfo?: string;
  // technical
  technicalInfo?: string;

}

export enum TestStatus {
  PASSED = 'PASSED',
  FAILED = 'FAILED',
  STOPPED = 'STOPPED',
  NOT_STARTED = 'NOT_STARTED'
}

export enum ActionType {
  REST = 'REST',
  SQL = 'SQL',
  TECHNICAL = 'TECHNICAL',

}
