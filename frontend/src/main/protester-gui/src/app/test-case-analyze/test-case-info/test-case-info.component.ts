import {Component, Input, OnInit} from '@angular/core';
import {TestCaseResult} from '../result.model';

@Component({
  selector: 'app-test-case-info',
  templateUrl: './test-case-info.component.html',
  styleUrls: ['./test-case-info.component.css']
})
export class TestCaseInfoComponent implements OnInit {

  @Input() testCaseResult: TestCaseResult;

  constructor() { }

  ngOnInit(): void {
    console.log(this.testCaseResult);
  }

}
