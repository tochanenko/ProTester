import {Injectable} from '@angular/core';
import {RunResultModel} from '../../../modules/projects-menu/projects/view/test-cases/run-result.model';

@Injectable({
  providedIn: 'root'
})
export class TestCaseRunAnalyzeService {

  private _runResultModel: RunResultModel;

  get runResultModel(): RunResultModel {
    return this._runResultModel;
  }

  set runResultModel(value: RunResultModel) {
    this._runResultModel = value;
  }
}
