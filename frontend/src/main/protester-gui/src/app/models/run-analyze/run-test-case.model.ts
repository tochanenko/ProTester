import {TestCaseModel} from '../test-case/test-case.model';

export class RunTestCaseModel {
  id?: number;
  testCaseResponseList: TestCaseModel[];
  userId: number;
}
