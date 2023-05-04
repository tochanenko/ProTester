import {ActionWrapperModel} from './wrapper.model';
import {User} from '../user.model';
import {TestCaseModel} from '../test-case/test-case.model';

export class TestCaseResultModel {
  id: number;
  user: User;
  testCase: TestCaseModel;
  status: StatusModel;
  startDate = '';
  endDate = '';
  innerResults?: ActionResultModel[];
}

export class AbstractActionModel {
  id?: number;
  name: string;
  description?: string;
  type: ExecutableComponentTypeModel;
  parameterNames?: string[];
  className?: string;
}

export class ActionResultModel {
  id?: number;
  action?: AbstractActionModel;
  startDate = '';
  endDate = '';
  startDateStr = '';
  endDateStr = '';
  status?: StatusModel;
  inputParameters?: { [name: string]: string };
  last?: boolean;
  message?: string;

  request?: any;
  response?: any;
  statusCode?: number;

  connectionUrl?: string;
  username?: string;
  query?: string;
  columns?: SqlColumnDtoModel[];

  extra?: { [name: string]: string };

  path?: string;
  image?: any;

  actionWrapperId?: number;

  constructor(actionWrapper: ActionWrapperModel) {
    this.actionWrapperId = actionWrapper.id;

    const tempAction: AbstractActionModel = new AbstractActionModel();
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

export class SqlColumnDtoModel {
  id: number;
  name: string;
  rows: string[];
}

export enum StatusModel {
  PASSED = 'PASSED',
  IN_PROGRESS = 'IN_PROGRESS',
  NOT_STARTED = 'NOT STARTED',
  FAILED = 'FAILED'
}

export enum ExecutableComponentTypeModel {
  REST = 'REST',
  SQL = 'SQL',
  TECHNICAL = 'TECHNICAL',
  UI = 'UI'
}
