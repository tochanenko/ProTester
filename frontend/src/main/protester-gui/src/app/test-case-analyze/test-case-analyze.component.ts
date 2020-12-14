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
          extra: {url: 'url1', sssd: 'sssssssss', ryu: 'sks', ska: 'skskss'},
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
      id: 4,
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
          id: 6,
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

  // resultList: TestCaseResult[] = [];
  idList: number[] = [1, 4];
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

            this.analyzeService.getTestCaseById(data.testCaseId)
              .subscribe(d => {
                data.name = d.name;
                this.resultList.push(data);
              }, error => console.log(error));
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
      actionName: JSON.parse(hello.body).message,
      startDate: '06/04/2020',
      endDate: '06/05/2020',
      extra: {url: 'url1', sssd: 'sss', ryu: 'sks', ska: 'skskss'},
      message: 'message1',
      type: ExecutableComponentType.REST,
      status: Status.PASSED,
    };
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
