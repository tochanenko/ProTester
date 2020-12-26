import {Component, Input, OnChanges, SimpleChanges} from '@angular/core';
import {TestCaseResultModel} from '../../../../../models/run-analyze/result.model';

@Component({
  selector: 'app-result-list',
  templateUrl: './result-list.component.html',
  styleUrls: ['./result-list.component.css']
})
export class ResultListComponent implements OnChanges {

  @Input() testCaseResult: TestCaseResultModel;
  panelOpenState = true;

  updateTestCaseResult(result): void {
    this.testCaseResult = result;
  }

  ngOnChanges(changes: SimpleChanges): void {
  }

}
