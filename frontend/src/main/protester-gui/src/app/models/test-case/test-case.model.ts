import {DataSetResponse} from "../data-set-response";


export class TestCaseModel {
  id?: number;
  name?: string;
  description?: string;
  projectId?: number;
  authorId?: number;
  scenarioId: number;
  dataSetId?: number;
  dataSetName?: string;
  scenarioName?: string | Object;
  environmentId?: number;
}
