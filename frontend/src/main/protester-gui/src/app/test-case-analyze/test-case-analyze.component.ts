import {Component, OnDestroy, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {ActionResult, ExecutableComponentType, Status, TestCaseResult} from './result.model';
import * as Stomp from '@stomp/stompjs';
import * as SockJS from 'sockjs-client';
import {ResultListComponent} from './result-list/result-list.component';
import {TestCaseAnalyzeService} from './test-case-analyze.service';
import {element} from 'protractor';
import {TestCaseInfoComponent} from './test-case-info/test-case-info.component';

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
  idList: number[] = [1, 4];
  isError = false;
  isLoading = true;
  numberOfReconnects = 0;
  maxNumberOfReconnects = 2;

  public stompClient = null;

  @ViewChildren('child') testCaseInfoComponents: QueryList<TestCaseInfoComponent>;

  constructor(private analyzeService: TestCaseAnalyzeService) {
  }

  ngOnInit(): void {
    // this.loadTestCasesResults();
    this.openWebSocketWithActionResults();
  }

  loadTestCasesResults(): void {
    for (let i = 0; i < this.idList.length; i++) {
      this.analyzeService.loadTestCasesResults(i)
        .subscribe(
          data => {
            this.isLoading = false;
            const index: number = this.resultList.findIndex(e => e.id === data.id);
            index ? this.resultList[index] = data : this.resultList.push(data);
          },
          error => this.isError = true
        );
    }
  }

  openWebSocketWithActionResults(): void {
    this.connect();
  }

  connect(): void {
    const socket = new SockJS('http://localhost:8080/onlyfullstack-stomp-endpoint');
    this.stompClient = Stomp.over(socket);
    const _this = this;
    this.stompClient.connect({}, function(frame) {
      _this.isLoading = false;
      _this.numberOfReconnects = 0;
      for (let index = 0; index < _this.idList.length; index++) {
        let id = _this.idList[index];
        _this.stompClient.subscribe('/topic/hi/' + id, function(hello) {

          _this.onMessageReceive(hello, id);

        });
      }
      _this.stompClient.reconnect_delay = 2000;

    }, function(errorCallback) {
      _this.numberOfReconnects++;
      _this.reconnect();
    });
  }

  onMessageReceive(hello, id: number): void {
    const _this = this;

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
    const indexOfTestCase: number = _this.resultList
      .findIndex(e => e.id === id);

    console.log('index = ' + indexOfTestCase);

    if (indexOfTestCase === undefined) {
      _this.resultList[indexOfTestCase].innerResults.push(actionToAdd);
    } else {
      const actionIndex = _this.resultList[indexOfTestCase].innerResults.findIndex(e => e.id === actionToAdd.id);
      _this.resultList[indexOfTestCase].innerResults[actionIndex] = (actionToAdd);
    }

    _this.testCaseInfoComponents
      .forEach((child) => child.refreshTree());
  }

  reconnect(): void {
    if (this.numberOfReconnects > this.maxNumberOfReconnects) {
      console.log('The number of reconnections to reach the upper limit failed');
      this.isError = true;
      return;
    }

    const _this = this;
    setTimeout(function() {
      console.log('---------------' + _this.numberOfReconnects);
      _this.connect();
    }, 3000);
  }

  disconnectStompClient(): void {
    if (this.stompClient != null) {
      this.stompClient.disconnect();
    }
  }

  onCliCkReconnect(): void {
    this.numberOfReconnects = 0;
    this.isError  = false;
    this.isLoading = true;
    this.reconnect();
  }
  ngOnDestroy(): void {
    this.disconnectStompClient();
  }
}
