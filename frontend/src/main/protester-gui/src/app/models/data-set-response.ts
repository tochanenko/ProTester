import {TestScenario} from './test-scenario';

export class DataSetResponse {
  id?: number;
  name?: string;
  description?: string;
  dataset?: Map<string, string>;
  testScenarios?: Array<number>;
}
