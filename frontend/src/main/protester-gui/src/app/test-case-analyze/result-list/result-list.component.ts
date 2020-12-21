import {Component, Input, OnChanges, SimpleChanges} from '@angular/core';
import {TestCaseResult} from '../result.model';

@Component({
  selector: 'app-result-list',
  templateUrl: './result-list.component.html',
  styleUrls: ['./result-list.component.css']
})
export class ResultListComponent implements OnChanges {

  @Input() testCaseResult: TestCaseResult;
  panelOpenState = true;

  constructor() { }

  updateTestCaseResult(result): void {
    this.testCaseResult = result;
  }

  ngOnChanges(changes: SimpleChanges): void {
  }

}
