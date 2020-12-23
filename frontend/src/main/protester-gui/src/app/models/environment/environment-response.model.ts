import {Project} from '../project/project.model';
import {EnvironmentModel} from './environment.model';

export class EnvironmentResponseModel {
  list: EnvironmentModel[];
  totalItems: number;
}
