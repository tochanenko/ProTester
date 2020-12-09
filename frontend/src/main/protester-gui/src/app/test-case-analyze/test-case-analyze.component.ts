import {Component, OnInit} from '@angular/core';
import {ActionStatus, ActionType, ExecutionStatus, TestCaseResult} from './result.model';

@Component({
  selector: 'app-test-case-run',
  templateUrl: './test-case-analyze.component.html',
  styleUrls: ['./test-case-analyze.component.css']
})
export class TestCaseAnalyzeComponent implements OnInit {

  resultList: TestCaseResult[] = [
    {
      id: 1,
      name: 'test case 1',
      userId: 2,
      testCaseId: 3,
      status: ExecutionStatus.IN_PROGRESS,
      startDate: '02/03/101',
      endDate: '04/02/303',
      innerResults: [
        {
          id: 4,
          name: 'action1',
          startDate: '9393',
          endDate: '400404',
          extra: {url: 'url1', sssd: 'sss'},
          status: ActionStatus.FAILED,
          type: ActionType.REST,
          // rest
          restInfo: 'rest info'
        },
        {
          id: 4,
          name: 'action2',
          startDate: '9393',
          endDate: '400404',
          extra: {url: 'url2', sssd: 'sss2'},
          status: ActionStatus.PASSED,
          type: ActionType.REST,
          // rest
          restInfo: 'rest info'
        },
        {
          id: 4,
          name: 'action3',
          startDate: '9393',
          endDate: '400404',
          extra: {url: 'url3', sssd: 'sss3'},
          status: ActionStatus.FAILED,
          type: ActionType.REST,
          // rest
          restInfo: 'rest info'
        },
        {
          id: 4,
          name: 'action4',
          startDate: '9393',
          endDate: '400404',
          extra: {url: 'url4', sssd: 'sss4'},
          status: ActionStatus.FAILED,
          type: ActionType.REST,
          // rest
          restInfo: 'rest info'
        }
      ]
    },

    {
      id: 1,
      name: 'test case 2',
      userId: 2,
      testCaseId: 3,
      status: ExecutionStatus.FINISHED,
      startDate: '02/03/101',
      endDate: '04/02/303',
      innerResults: [
        {
          id: 4,
          name: 'action5',
          startDate: '9393',
          endDate: '400404',
          extra: {url: 'url1', sssd: 'sss'},
          status: ActionStatus.STOPPED,
          type: ActionType.REST,
          // rest
          restInfo: 'rest info'
        },
        {
          id: 4,
          name: 'action6',
          startDate: '9393',
          endDate: '400404',
          extra: {url: 'url1', sssd: 'sss'},
          status: ActionStatus.NOT_STARTED,
          type: ActionType.TECHNICAL,
          // rest
          restInfo: 'rest info'
        },
        {
          id: 4,
          name: 'action7',
          startDate: '9393',
          endDate: '400404',
          extra: {url: 'url1', sssd: 'sss'},
          status: ActionStatus.FAILED,
          type: ActionType.REST,
          // rest
          restInfo: 'rest info'
        },
        {
          id: 4,
          name: 'action8',
          startDate: '9393',
          endDate: '400404',
          status: ActionStatus.PASSED,
          type: ActionType.SQL,
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
