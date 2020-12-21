import {Step} from "./step.model";

export class TestScenario {
  id?: number;
  name?: string | Object;
  description?: string;
  type?: string;
  parameterNames?: Array<string>;
  steps?: Array<Step>;
}
