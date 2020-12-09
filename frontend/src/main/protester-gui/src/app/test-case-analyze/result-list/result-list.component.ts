import {Component, Input, OnInit} from '@angular/core';
import {TestCaseResult} from '../result.model';

@Component({
  selector: 'app-result-list',
  templateUrl: './result-list.component.html',
  styleUrls: ['./result-list.component.css']
})
export class ResultListComponent implements OnInit {

  @Input() testCaseResult: TestCaseResult;

  panelOpenState = true;

  constructor() { }

  ngOnInit(): void {
    console.log(this.testCaseResult);
  }

}
