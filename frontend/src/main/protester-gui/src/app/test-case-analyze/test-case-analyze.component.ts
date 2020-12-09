import {Component, OnInit} from '@angular/core';
import {ExecutableComponentType, Status, TestCaseResult} from './result.model';

@Component({
  selector: 'app-test-case-run',
  templateUrl: './test-case-analyze.component.html',
  styleUrls: ['./test-case-analyze.component.css']
})
export class TestCaseAnalyzeComponent implements OnInit {

  resultList: TestCaseResult[] = [
    {
      id: 1,
      userId: 2,
      name: 'Test case 1',
      testCaseId: 3,
      status: Status.PASSED,
      startDate: '02/03/101',
      endDate: '04/02/303',
      innerResults: [
        {
          id: 4,
          actionName: 'action1',
          startDate: '06/04/2020',
          endDate: '06/05/2020',
          extra: {url: 'url1', sssd: 'sss', ryu: 'sks', ska: 'skskss'},
          message: 'message1',
          type: ExecutableComponentType.REST,
          status: Status.PASSED,
        },
        {
          id: 4,
          actionName: 'action2',
          startDate: '03/04/2020',
          endDate: '03/05/2020',
          extra: {url: 'url1', sssd: 'sss'},
          message: 'message',
          type: ExecutableComponentType.UI,
          status: Status.IN_PROGRESS,
        },
        {
          id: 4,
          actionName: 'action3',
          startDate: '03/04/2020',
          endDate: '03/05/2020',
          extra: {url: 'url1', sssd: 'sss'},
          message: 'message',
          type: ExecutableComponentType.SQL,
          status: Status.FAILED,
        },
        {
          id: 4,
          actionName: 'action4',
          startDate: '03/04/2020',
          endDate: '03/05/2020',
          extra: {url: 'url1', sssd: 'sss'},
          message: 'message',
          type: ExecutableComponentType.REST,
          status: Status.FAILED,
        },
      ]
    },

    {
      id: 2,
      userId: 2,
      name: 'Test case 2',
      testCaseId: 3,
      status: Status.FAILED,
      startDate: '03/04/2020',
      endDate: '03/05/2020',
      innerResults: [
        {
          id: 4,
          actionName: 'action5',
          startDate: '03/04/2020',
          endDate: '03/05/2020',
          extra: {url: 'url1', sssd: 'sss'},
          message: 'message',
          type: ExecutableComponentType.REST,
          status: Status.FAILED,
        },
        {
          id: 4,
          actionName: 'action6',
          startDate: '03/04/2020',
          endDate: '03/05/2020',
          extra: {url: 'url1', sssd: 'sss'},
          message: 'message',
          type: ExecutableComponentType.TECHNICAL,
          status: Status.IN_PROGRESS,
        },
        {
          id: 4,
          actionName: 'action7',
          startDate: '03/04/2020',
          endDate: '03/05/2020',
          extra: {url: 'url1', sssd: 'sss'},
          message: 'message',
          type: ExecutableComponentType.REST,
          status: Status.FAILED,
        },
        {
          id: 4,
          actionName: 'action8',
          startDate: '03/04/2020',
          endDate: '03/05/2020',
          extra: {url: 'url1', sssd: 'sss'},
          message: 'message',
          type: ExecutableComponentType.REST,
          status: Status.NOT_STARTED,
        },
      ]
    },
  ];

  constructor() {
  }

  ngOnInit(): void {
  }

}
