export class ValidationDataSetResponseModel {
  dataSetName: string;
  status: ValidationDataSetStatusModel;
  missingParameters: string[];
}

export enum ValidationDataSetStatusModel {
  PASSED = 'PASSED',
  FAILED = 'FAILED'
}
