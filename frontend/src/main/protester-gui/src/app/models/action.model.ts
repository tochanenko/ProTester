export class Action {
  id: number;
  declarationId: number;
  type:  string;
  name: string;
  description: string;
  parameterNames: Array<string>;
  preparedParams: Map<string,string>;

}
