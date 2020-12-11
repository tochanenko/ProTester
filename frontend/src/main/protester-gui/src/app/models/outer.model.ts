import {ExecutableComponent} from "./executable.model";
import {Step} from "./step.model";
import {ExecutableComponentType} from "./executable-type.model";

export class OuterComponent extends ExecutableComponent {
  id: number;
  steps: Array<Step>;

  constructor(name: string, description: string, type: ExecutableComponentType, parameterNames: Array<string>, id: number, steps: Array<Step>) {
    super(name, description, type, parameterNames);
    this.id = id;
    this.steps = steps;
  }
}
