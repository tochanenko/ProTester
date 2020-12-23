import {DataSetResponse} from "../../../../../models/data-set-response";


export class TestCaseModel {
  id?: number;
  name?: string;
  description?: string;
  projectId?: number;
  authorId?: number;
  scenarioId: number;
  dataSetResponseList?: Array<DataSetResponse>;
  dataSetId?: Array<number>;
  scenarioName?: string | Object;
  environment?: number;
}
