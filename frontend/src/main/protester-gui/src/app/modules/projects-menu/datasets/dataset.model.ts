export interface DataSet {
  id?: number;
  name: string;
  description: string;
  parameters: object;
}

export interface DataSetParameter {
  name: string;
  value: string;
}
