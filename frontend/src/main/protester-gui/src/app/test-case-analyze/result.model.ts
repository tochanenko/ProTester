import {User} from '../models/user.model';
import {TestCaseModel} from '../test-case/test-case.model';
import {ActionWrapperModel} from './wrapper.model';

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
  id?: number;
  action?: AbstractAction;
  startDate = '';
  endDate = '';

  startDateStr = '';
  endDateStr = '';
  status?: Status;
  inputParameters?: { [name: string]: string };

  last?: boolean;

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


  constructor(actionWrapper: ActionWrapperModel) {
    this.id = actionWrapper.id;

    const tempAction: AbstractAction = new AbstractAction();
    tempAction.name = actionWrapper.name;
    tempAction.description = actionWrapper.description;
    tempAction.className = actionWrapper.className;
    tempAction.type = actionWrapper.type;

    this.action = tempAction;
    this.startDate = actionWrapper.startDate;
    this.endDate = actionWrapper.endDate;
    this.status = actionWrapper.resultStatus;
    this.inputParameters = actionWrapper.parameters;
    this.message = actionWrapper.message;
  }
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
