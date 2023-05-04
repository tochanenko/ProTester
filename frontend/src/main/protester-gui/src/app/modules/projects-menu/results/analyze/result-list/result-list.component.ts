import {Component, Input} from '@angular/core';
import {TestCaseResultModel} from '../../../../../models/run-analyze/result.model';

@Component({
  selector: 'app-result-list',
  templateUrl: './result-list.component.html',
  styleUrls: ['./result-list.component.css']
})
export class ResultListComponent {

  @Input() testCaseResult: TestCaseResultModel;
  panelOpenState = true;

  updateTestCaseResult(result): void {
    this.testCaseResult = result;
  }

}
