export class TestCaseResult {
  id: number;
  userId: number;
  name: string;
  testCaseId: number;
  status: Status;
  startDate = '';
  endDate = '';
  innerResults?: ActionResult[];
}

export class ActionResult {
  id: number;
  actionName: string;
  startDate = '';
  endDate = '';
  extra?: { [name: string]: string };
  message?: string;
  type: ExecutableComponentType = ExecutableComponentType.REST;
  status: Status = Status.PASSED;
}

export enum Status {
  PASSED = 'PASSED',
  IN_PROGRESS = 'IN PROGRESS',
  NOT_STARTED = 'NOT STARTED',
  FAILED = 'FAILED'
}

export enum ExecutableComponentType {
  REST = 'REST',
  SQL = 'SQL',
  TECHNICAL = 'TECHNICAL',
  UI = 'UI'
}
