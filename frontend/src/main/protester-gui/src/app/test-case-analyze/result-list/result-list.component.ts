import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {TestCaseResult} from '../result.model';

@Component({
  selector: 'app-result-list',
  templateUrl: './result-list.component.html',
  styleUrls: ['./result-list.component.css']
})
export class ResultListComponent implements OnInit, OnChanges {

  @Input() testCaseResult: TestCaseResult;

  panelOpenState = true;

  constructor() { }

  ngOnInit(): void {
    // console.log(this.testCaseResult);
  }

  updateTestCaseResult(result): void {
    console.log('ResultListComponent:  ' + result);
    this.testCaseResult = result;
  }

  ngOnChanges(changes: SimpleChanges): void {
    console.log('onchange');
  }

}
