import {ExecutableComponentType, Status} from './result.model';

export class TestCaseWrapperResult {

  id: number;

  actionWrapperList: ActionWrapperModel[];

  scenarioId: number;

  testResultId: number;

}

export class ActionWrapperModel {

  id: number;

  description: string;

  className: string;

  name: string;

  startDate: string;

  endDate: string;

  message: string;

  type: ExecutableComponentType;

  parameters: { [name: string]: string };

  resultStatus: Status;

  stepId: number;

  actionResultDtoId: number;
}
