import {Component, OnDestroy, OnInit, QueryList, ViewChildren} from '@angular/core';
import {ExecutableComponentType, Status, TestCaseResult} from './result.model';
import {TestCaseAnalyzeService} from './test-case-analyze.service';
import {TestCaseInfoComponent} from './test-case-info/test-case-info.component';
import {TestCaseService} from '../services/test-case/test-case-service';
import {WebsocketsService} from './websockets.service';

@Component({
  selector: 'app-test-case-run',
  templateUrl: './test-case-analyze.component.html',
  styleUrls: ['./test-case-analyze.component.css']
})
export class TestCaseAnalyzeComponent implements OnInit, OnDestroy {

  resultList: TestCaseResult[] = [
    {
      id: 1,
      user: {
        username: 'username1',
        password: 'password',
        email: 'email',
        firstName: 'firstname',
        lastName: 'lastname',
        role: 'role',
      },
      testCase: {
        name: 'testcase1',
        scenarioId: 1
      },
      status: Status.PASSED,
      startDate: '02/03/101',
      endDate: '04/02/303',
      innerResults: [
        {
          id: 4,
          action: {
            id: 3,
            name: 'action1',
            type: ExecutableComponentType.REST
          },
          startDate: '06/04/2020',
          endDate: '06/05/2020',
          status: Status.PASSED,
          inputParameters: {urljjjjjjjjjjjjjjjjjjurljjjjjjjjjjjjjjjjjjjjurljjjjjjjjjjjjjjjjjjjjurljjjjjjjjjjjjjjjjjjjjjj: 'urjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjl1', sssd: 'sssssssss'},
          request: 'request',
          response: 'response',
          statusCode: 202
        },
        {
          id: 7,
          action: {
            id: 6,
            name: 'action5',
            type: ExecutableComponentType.SQL
          },
          startDate: '06/04/2220',
          endDate: '06/05/2320',
          status: Status.PASSED,
          inputParameters: {url: 'url1', sssd: 'sssssssss'},
          connectionUrl: 'url',
          username: 'username',
          query: 'select * from users',
          columns: [
            {
              id: 1,
              name: 'id',
              rows: ['560', '561']
            },
            {
              id: 2,
              name: 'nickNamnnnnnnnnn',
              rows: ['Vasya', 'Illya']
            },
            //
            {
              id: 2,
              name: 'nickNamennnnnnn',
              rows: ['Vasya', 'Illya']
            },
            //

            {
              id: 2,
              name: 'ddddddd',
              rows: ['Vasya', 'Illya']
            },

            // {
            //   id: 2,
            //   name: 'ddddddd',
            //   rows: ['Vasya', 'Illya']
            // },
            //
            // {
            //   id: 2,
            //   name: 'dddddd333d',
            //   rows: ['Vasya', 'Illya']
            // },
            // {
            //   id: 2,
            //   name: 'ddddddd',
            //   rows: ['Vasya', 'Illya']
            // },

            // {
            //   id: 2,
            //   name: 'dddddd333d',
            //   rows: ['Vasya', 'Illya']
            // },
            //
            // {
            //   id: 2,
            //   name: 'ddddddd',
            //   rows: ['Vasya', 'Illya']
            // },
            //
            // {
            //   id: 2,
            //   name: 'dddddd333d',
            //   rows: ['Vasya', 'Illya']
            // },
            //
            // {
            //   id: 2,
            //   name: 'ddddddd',
            //   rows: ['Vasya', 'Illya']
            // },
            //
            // {
            //   id: 2,
            //   name: 'dddddd333d',
            //   rows: ['Vasya', 'Illya']
            // },

          ]
        }
      ]
    },
  ];

  // resultList: TestCaseResult[] = [];
  idList: number[] = [1];
  isLoading = true;
  isError = false;

  @ViewChildren('child') testCaseInfoComponents: QueryList<TestCaseInfoComponent>;

  constructor(private analyzeService: TestCaseAnalyzeService,
              private testCaseService: TestCaseService,
              private websocketsService: WebsocketsService) {
  }

  ngOnInit(): void {
    // this.loadTestCasesResults();
    this.isLoading = false;
    this.openWebSocketWithActionResults();
  }

  loadTestCasesResults(): void {
    for (let i = 0; i < this.idList.length; i++) {
      this.analyzeService.loadTestCasesResults(this.idList[i])
        .subscribe(
          data => {
            this.isLoading = false;
            this.resultList.push(data);
          },
          error => console.log('error')
        );
    }
  }

  openWebSocketWithActionResults(): void {
    this.websocketsService.connect(() => {
      for (let index = 0; index < this.idList.length; index++) {
        const id = this.idList[index];
        this.websocketsService.getStompClient().subscribe('/topic/hi/' + id, (hello) => {
          this.onMessageReceive(hello, id);
        });
      }
    });
  }

  onMessageReceive(hello, id: number): void {

    const actionToAdd = {
        id: 4,
        action: {
          id: 3,
          name: JSON.parse(hello.body).message,
          type: ExecutableComponentType.REST
        },
        startDate: '06/04/2020',
        endDate: '06/05/2020',
        status: Status.PASSED,
        inputParameters: {url: 'url1', sssd: 'sssssssss'},
        request: 'request',
        response: 'response',
        statusCode: 202
      };
    console.log(id + '--------');
    const indexOfTestCase: number = this.resultList
      .findIndex(e => e.id === id);

    console.log('index = ' + indexOfTestCase);

    if (indexOfTestCase === undefined) {
      this.resultList[indexOfTestCase].innerResults.push(actionToAdd);
    } else {
      const actionIndex = this.resultList[indexOfTestCase].innerResults.findIndex(e => e.id === actionToAdd.id);
      this.resultList[indexOfTestCase].innerResults[actionIndex] = (actionToAdd);
    }

    this.testCaseInfoComponents
      .forEach((child) => child.refreshTree());
  }

  ngOnDestroy(): void {
    this.websocketsService.disconnectClient();
  }
}
