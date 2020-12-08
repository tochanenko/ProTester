import {ExecutableComponentType} from "./executable-type.model";

export class ExecutableComponent {
  name: string;
  description: string;
  type: ExecutableComponentType;
  parameterNames: Array<string>;

  constructor(name: string, description: string, type: ExecutableComponentType, parameterNames: Array<string>) {
    this.name = name;
    this.description = description;
    this.type = type;
    this.parameterNames = parameterNames;
  }
}

