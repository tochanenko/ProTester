import {ExecutableComponentType} from "./executable-type.model";

export class ExecutableComponent {
  name: string;
  description: string;
  type: ExecutableComponentType;
  parameterNames: Array<string>;
}

