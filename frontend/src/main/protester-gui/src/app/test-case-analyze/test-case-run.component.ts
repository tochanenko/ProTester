import {Component, OnInit} from '@angular/core';
import {ActionResult, TestCaseResult} from './result.model';

@Component({
  selector: 'app-test-case-run',
  templateUrl: './test-case-run.component.html',
  styleUrls: ['./test-case-run.component.css']
})
export class TestCaseRunComponent implements OnInit {

  resultList: TestCaseResult[] = [
    {
      id: 1,
      userId: 2,
      testCaseId: 3,
      statusId: 1,
      startDate: '02/03/101',
      endDate: '04/02/303',
      innerResults: [
        {
          id: 4,
          startDate: '9393',
          endDate: '400404',
          extra: {url: 'url1', sssd: 'sss'},
          result: true,
          // rest
          restInfo: 'rest info'
        },
        {
          id: 4,
          startDate: '9393',
          endDate: '400404',
          extra: {url: 'url2', sssd: 'sss2'},
          result: true,
          // rest
          restInfo: 'rest info'
        },
        {
          id: 4,
          startDate: '9393',
          endDate: '400404',
          extra: {url: 'url3', sssd: 'sss3'},
          result: true,
          // rest
          restInfo: 'rest info'
        },
        {
          id: 4,
          startDate: '9393',
          endDate: '400404',
          extra: {url: 'url4', sssd: 'sss4'},
          result: true,
          // rest
          restInfo: 'rest info'
        }
      ]
    },

    {
      id: 1,
      userId: 2,
      testCaseId: 3,
      statusId: 1,
      startDate: '02/03/101',
      endDate: '04/02/303',
      innerResults: [
        {
          id: 4,
          startDate: '9393',
          endDate: '400404',
          extra: {url: 'url1', sssd: 'sss'},
          result: true,
          // rest
          restInfo: 'rest info'
        },
        {
          id: 4,
          startDate: '9393',
          endDate: '400404',
          extra: {url: 'url1', sssd: 'sss'},
          result: true,
          // rest
          restInfo: 'rest info'
        },
        {
          id: 4,
          startDate: '9393',
          endDate: '400404',
          extra: {url: 'url1', sssd: 'sss'},
          result: true,
          // rest
          restInfo: 'rest info'
        },
        {
          id: 4,
          startDate: '9393',
          endDate: '400404',
          result: true,
          // rest
          restInfo: 'rest info'
        }
      ]
    },
  ];

  constructor() {
    console.log(this.resultList);
  }

  ngOnInit(): void {
  }

}
