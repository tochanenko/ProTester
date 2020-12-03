export class Action {
  id: number;
  type?:  string;
  name?: string;
  description?: string;
  parameterNames? : string [];
  preparedParams?: Map<string,string>;
}
