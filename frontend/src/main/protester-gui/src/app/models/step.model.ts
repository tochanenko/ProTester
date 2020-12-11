import {ExecutableComponent} from "./executable.model";

export class Step {
  id: number;
  isAction: boolean;
  component: ExecutableComponent;
  parameters: Map<String, String>;



}
