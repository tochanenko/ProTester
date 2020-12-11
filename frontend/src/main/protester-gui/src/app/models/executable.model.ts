import {ExecutableComponentType} from "./executable-type.model";

export class ExecutableComponent {
  id: number;
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

