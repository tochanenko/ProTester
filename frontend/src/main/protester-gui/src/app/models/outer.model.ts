import {ExecutableComponent} from "./executable.model";
import {Step} from "./step.model";

export class OuterComponent extends ExecutableComponent {
  id: number;
  steps: Array<Step>;
}
