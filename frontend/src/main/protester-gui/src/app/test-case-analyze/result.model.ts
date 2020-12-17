import {User} from '../models/user.model';
import {TestCaseModel} from '../test-case/test-case.model';

export class TestCaseResult {
  id: number;
  user: User;
  testCase: TestCaseModel;
  status: Status;
  startDate = '';
  endDate = '';
  innerResults?: ActionResult[];
}

export class AbstractAction {
  id?: number;
  name: string;
  description?: string;
  type: ExecutableComponentType;
  parameterNames?: string[];
  className?: string;
}

export class Message {
  hello: ActionResult;
}

export class ActionResult {
  id: number;
  action: AbstractAction;
  startDate = '';
  endDate = '';
  status: Status;
  inputParameters?: { [name: string]: string };

  isLastAction: boolean;

  exception?: ActionExecutionException;
  message?: string;

  // rest
  request?: string;
  response?: string;
  statusCode?: number;

  // sql
  connectionUrl?: string;
  username?: string;
  query?: string;
  columns?: SqlColumnDto[];

  // technical
  extra?: { [name: string]: string };

  // ui
  path?: string;
}

export class ActionExecutionException {
}

export class SqlColumnDto {
  id: number;
  name: string;
  rows: string[];
}

export enum Status {
  PASSED = 'PASSED',
  IN_PROGRESS = 'IN_PROGRESS',
  NOT_STARTED = 'NOT STARTED',
  FAILED = 'FAILED'
}

export enum ExecutableComponentType {
  REST = 'REST',
  SQL = 'SQL',
  TECHNICAL = 'TECHNICAL',
  UI = 'UI'
}
