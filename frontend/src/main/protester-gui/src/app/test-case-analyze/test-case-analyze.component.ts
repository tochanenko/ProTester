import {Component, OnDestroy, OnInit, QueryList, ViewChildren} from '@angular/core';
import {ActionResult, ExecutableComponentType, Message, Status, TestCaseResult} from './result.model';
import {TestCaseAnalyzeService} from './test-case-analyze.service';
import {TestCaseInfoComponent} from './test-case-info/test-case-info.component';
import {TestCaseService} from '../services/test-case/test-case-service';
import {WebsocketsService} from './websockets.service';
import {TestCaseRunAnalyzeService} from '../services/test-case-run-analyze.service';
import {RunResultModel} from '../test-case/run-result.model';
import {Observable, of, Subscription} from 'rxjs';
import {StompSubscription} from '@stomp/stompjs';

@Component({
  selector: 'app-test-case-run',
  templateUrl: './test-case-analyze.component.html',
  styleUrls: ['./test-case-analyze.component.css']
})
export class TestCaseAnalyzeComponent implements OnInit, OnDestroy {

  // // resultList: TestCaseResult[] = [
  //    {
  //      id: 1,
  //      user: {
  //        username: 'username1',
  //        password: 'password',
  //        email: 'email',
  //        firstName: 'firstname',
  //        lastName: 'lastname',
  //        role: 'role',
  //      },
  //      testCase: {
  //        name: 'testcase1',
  //        scenarioId: 1
  //      },
  //      status: Status.PASSED,
  //      startDate: '02/03/101',
  //      endDate: '04/02/303',
  //      innerResults: [
  //        {
  //          id: 4,
  //          action: {
  //            id: 3,
  //            name: 'action1',
  //            type: ExecutableComponentType.REST
  //          },
  //          startDate: '06/04/2020',
  //          endDate: '06/05/2020',
  //          status: Status.PASSED,
  //          inputParameters: {urljjjjjjjjjjjjjjjjjjurljjjjjjjjjjjjjjjjjjjjurljjjjjjjjjjjjjjjjjjjjurljjjjjjjjjjjjjjjjjjjjjj: 'urjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjl1', sssd: 'sssssssss'},
  //          request: 'request',
  //          response: 'response',
  //          statusCode: 202
  //        },
  //        {
  //          id: 7,
  //          action: {
  //            id: 6,
  //            name: 'action5',
  //            type: ExecutableComponentType.SQL
  //          },
  //          startDate: '06/04/2220',
  //          endDate: '06/05/2320',
  //          status: Status.PASSED,
  //          inputParameters: {url: 'url1', sssd: 'sssssssss'},
  //          connectionUrl: 'url',
  //          username: 'username',
  //          query: 'select * from users',
  //          columns: [
  //            {
  //              id: 1,
  //              name: 'id',
  //              rows: ['560', '561']
  //            },
  //            {
  //              id: 2,
  //              name: 'nickNamnnnnnnnnn',
  //              rows: ['Vasya', 'Illya']
  //            },
  //            //
  //            {
  //              id: 2,
  //              name: 'nickNamennnnnnn',
  //              rows: ['Vasya', 'Illya']
  //            },
  //            //
  //
  //            {
  //              id: 2,
  //              name: 'ddddddd',
  //              rows: ['Vasya', 'Illya']
  //            },
  //
  //            {
  //              id: 2,
  //              name: 'ddddddd',
  //              rows: ['Vasya', 'Illya']
  //            },
  //
  //            {
  //              id: 2,
  //              name: 'dddddd333d',
  //              rows: ['Vasya', 'Illya']
  //            },
  //            {
  //              id: 2,
  //              name: 'ddddddd',
  //              rows: ['Vasya', 'Illya']
  //            },
  //
  //            {
  //              id: 2,
  //              name: 'dddddd333d',
  //              rows: ['Vasya', 'Illya']
  //            },
  //            //
  //            // {
  //            //   id: 2,
  //            //   name: 'ddddddd',
  //            //   rows: ['Vasya', 'Illya']
  //            // },
  //            //
  //            // {
  //            //   id: 2,
  //            //   name: 'dddddd333d',
  //            //   rows: ['Vasya', 'Illya']
  //            // },
  //            //
  //            // {
  //            //   id: 2,
  //            //   name: 'ddddddd',
  //            //   rows: ['Vasya', 'Illya']
  //            // },
  //            //
  //            // {
  //            //   id: 2,
  //            //   name: 'dddddd333d',
  //            //   rows: ['Vasya', 'Illya']
  //            // },
  //
  //          ]
  //        }
  //      ]
  //    },
  //  ];

  resultList: TestCaseResult[] = [];
  idList: number[] = [];
  isLoading = true;
  isError = false;
  idToRun: number;
  subsc: StompSubscription;

  @ViewChildren('child') testCaseInfoComponents: QueryList<TestCaseInfoComponent>;

  constructor(private analyzeService: TestCaseAnalyzeService,
              private testCaseService: TestCaseService,
              private websocketsService: WebsocketsService,
              private runAnalyzeService: TestCaseRunAnalyzeService) {
  }

  ngOnInit(): void {
    // const result: RunResultModel = this.runAnalyzeService.runResultModel;
    // console.log('------------' + result.id + ',  ids:' + result.testCaseResult);
    this.idList = this.runAnalyzeService.runResultModel.testCaseResult;
    of(this.loadTestCasesResults()).subscribe(() => {
      this.openWebSocketWithActionResults();
    });

    // //of(this.openWebSocketWithActionResults())
    //   .subscribe(() => {
    //     console.log(3);
    //     this.testCaseService.runTestCase(this.runAnalyzeService.runResultModel.id)
    //       .subscribe(result => console.log('running'));
    //  console.log(4);

    //  });
    //console.log(5);
    // of(this.loadTestCasesResults()).subscribe(() => this.openWebSocketWithActionResults());
    // this.openWebSocketWithActionResults();
    this.isLoading = false;


  }


  loadTestCasesResults(): void {
    for (let i = 0; i < this.idList.length; i++) {
      this.analyzeService.loadTestCasesResults(this.idList[i])
        .subscribe(
          data => {
            console.log('in loadTestCasesResults');
            this.isLoading = false;
            this.resultList.push(data);
            this.idToRun = this.runAnalyzeService.runResultModel.id;
          },
          error => console.log('error')
        );
    }

  }

  openWebSocketWithActionResults(): void {
    // this.websocketsService.disconnectClient();
    console.log(5);
    this.websocketsService.connect(() => {
      console.log(this.runAnalyzeService.runResultModel.id + '*********');
      console.log('----------------------------------------');
      for (let index = 0; index < this.idList.length; index++) {
        const id = this.idList[index];
        this.websocketsService.getStompClient().subscribe('/topic/public/' + id, (hello) => {
          console.log('------------------------hello');
          this.onMessageReceive(hello, id);
        });
      }

      this.testCaseService.runTestCase(this.runAnalyzeService.runResultModel.id)
        .subscribe(result => {
          console.log('running');
        });
    });
  }

  onMessageReceive(hello, id: number): void {

    // console.log(JSON.parse(hello.body));
    // const actionToAdd = {
    //     id: 4,
    //     action: {
    //       id: 3,
    //       name: JSON.parse(hello.body).hello,
    //       type: ExecutableComponentType.REST
    //     },
    //     startDate: '06/04/2020',
    //     endDate: '06/05/2020',
    //     status: Status.PASSED,
    //     inputParameters: {url: 'url1', sssd: 'sssssssss'},
    //     request: 'request',
    //     response: 'response',
    //     statusCode: 202
    //   };

    // const actionToAddjson: any = JSON.parse(hello.body);
    // const message: Message = actionToAddjson as Message;

    console.log('---');

    const actionToAdd: ActionResult = JSON.parse(hello.body);
    console.log('id-----' + actionToAdd.id);
    console.log(id + '--------');
    console.log('type' + typeof actionToAdd);
    console.log('!!!!!!!!!!' + JSON.stringify(actionToAdd));
    const indexOfTestCase: number = this.resultList
      .findIndex(e => e.id === id);

    console.log('index = ' + indexOfTestCase);

    if (indexOfTestCase === undefined) {
      this.resultList[indexOfTestCase].innerResults.push(actionToAdd);
    } else {
      const actionIndex = this.resultList[indexOfTestCase].innerResults.findIndex(e => e.id === actionToAdd.id);
      this.resultList[indexOfTestCase].innerResults[actionIndex] = (actionToAdd);
    }

    this.resultList[indexOfTestCase].innerResults.push(actionToAdd);
    console.log('_______________' + typeof this.resultList[indexOfTestCase].innerResults[0]);
    console.log('--------------------------' + this.resultList[indexOfTestCase].startDate);

    this.testCaseInfoComponents
      .forEach((child) => child.refreshTree());
  }

  ngOnDestroy(): void {
    this.websocketsService.disconnectClient();
  }

}
