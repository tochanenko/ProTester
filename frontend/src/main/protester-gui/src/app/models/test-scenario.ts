import {Step} from "./step.model";

export class TestScenario {
  id?: number;
  name?: string;
  description?: string | Object;
  type?: string;
  parameterNames?: Array<string>;
  steps?: Array<Step>;
}
