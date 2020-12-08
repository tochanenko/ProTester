import {ExecutableComponent} from "./executable.model";
import {ExecutableComponentType} from "./executable-type.model";

export class Action extends ExecutableComponent{
  id: number;
  className: string;
  prepared: boolean;
  preparedParams: Map<string,string>;

  constructor(name: string, description: string, type: ExecutableComponentType, parameterNames: Array<string>, id: number, className: string, prepared: boolean, preparedParams: Map<string, string>) {
    super(name, description, type, parameterNames);
    this.id = id;
    this.className = className;
    this.prepared = prepared;
    this.preparedParams = preparedParams;
  }
}
