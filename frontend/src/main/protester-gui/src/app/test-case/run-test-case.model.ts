import {TestCaseModel} from './test-case.model';

export class RunTestCaseModel {
  id?: number;
  testCaseRequestList: TestCaseModel[];
  userId: number;
}
