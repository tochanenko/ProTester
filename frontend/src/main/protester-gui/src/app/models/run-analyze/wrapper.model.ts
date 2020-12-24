import {ExecutableComponentTypeModel, StatusModel} from './result.model';

export class TestCaseWrapperResultModel {

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

  startDate: '';

  endDate: '';

  message: string;

  type: ExecutableComponentTypeModel;

  parameters: { [name: string]: string };

  resultStatus: StatusModel;

  stepId: number;

  actionResultDtoId: number;
}
