import {ExecutableComponent} from "./executable.model";

export class Action extends ExecutableComponent{
  id: number;
  className: string;
  declarationId: number;
  preparedParams: Map<string,string>;
}
