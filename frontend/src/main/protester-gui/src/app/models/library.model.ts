import {Step} from "./step.model";

export class Library {
  id: number;
  name: string;
  description: string;
  components: Array<Step>;
}
