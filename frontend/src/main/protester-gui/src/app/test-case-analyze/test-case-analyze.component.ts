import {Component, OnDestroy, OnInit, QueryList, ViewChildren} from '@angular/core';
import {ActionResult, ExecutableComponentType, Status, TestCaseResult} from './result.model';
import {TestCaseAnalyzeService} from './test-case-analyze.service';
import {TestCaseInfoComponent} from './test-case-info/test-case-info.component';
import {TestCaseService} from '../services/test-case/test-case-service';
import {WebsocketsService} from './websockets.service';
import {of} from 'rxjs';
import {ActivatedRoute} from '@angular/router';
import {concatMap} from 'rxjs/operators';

@Component({
  selector: 'app-test-case-run',
  templateUrl: './test-case-analyze.component.html',
  styleUrls: ['./test-case-analyze.component.css']
})
export class TestCaseAnalyzeComponent implements OnInit, OnDestroy {

  resultList: TestCaseResult[] = [];
  idList: number[] = [];
  isLoading = true;
  isError = false;
  idToRun: number;
  isTestCasesCompleted: number[] = [];

  @ViewChildren('child') testCaseInfoComponents: QueryList<TestCaseInfoComponent>;

  constructor(private analyzeService: TestCaseAnalyzeService,
              private testCaseService: TestCaseService,
              private websocketsService: WebsocketsService,
              private route: ActivatedRoute) {
    this.route.params.subscribe(params => this.idToRun = params.id);
  }

  ngOnInit(): void {

    this.testCaseService.findRunResultByID(this.idToRun).pipe(
      concatMap((item) => {
        this.idList = item.testCaseResult;
        return of(this.idList);
      }),
      concatMap(() => {
        return of(this.loadTestCasesResults());
      }),
      concatMap(() => {
        this.isLoading = false;
        return of(this.openWebSocketWithActionResults());
      })
    ).subscribe(
      () => console.log('successfully'),
      () => console.log('error')
    );
  }

  loadTestCasesResults(): void {
    for (let i = 0; i < this.idList.length; i++) {
      this.analyzeService.loadTestCasesResults(this.idList[i])
        .subscribe(
          data => {
            console.log('in loadTestCasesResults');
            this.isLoading = false;
            this.resultList.push(data);
          },
          error => console.log('error')
        );
    }

    this.resultList.push({
      id: 6,
      user: {
        id: 8,
        username: 'u',
        password: 'p',
        email: 'e',
        firstName: 'f',
        lastName: 'l',
        role: 'ADMIN'
      },
      testCase: {
        name: 'testCase',
        scenarioId: 5
      },
      status: Status.FAILED,
      startDate: '',
      endDate: '',
      innerResults: [
        {
          action: {
            name: 'action',
            type: ExecutableComponentType.SQL
          },
          message: 'message',
          startDate: '',
          endDate: '',
          status: Status.FAILED,
          columns: [
            {
              id: 1,
              name: 'first',
              rows: ['aaa', 'bbb', 'ccc']
            },

            {
              id: 2,
              name: 'first2',
              rows: ['aaa2', 'bbb2', 'ccc2']
            },
            {
              id: 3,
              name: 'first3',
              rows: ['aaa3', 'bbb3', 'ccc3']
            },
            {
              id: 4,
              name: 'first4',
              rows: ['aaa4', 'bbb4', 'ccc4']
            },
            {
              id: 3,
              name: 'first3',
              rows: ['aaa3', 'bbb3', 'ccc3']
            },
            {
              id: 4,
              name: 'first4',
              rows: ['aaa4', 'bbb4', 'ccc4']
            }
          ]
        }
      ]

    });
  }

  openWebSocketWithActionResults(): void {
    if (!this.isAllTestCasesCompleted()) {
      console.log('IN openWebSocketWithActionResults - not all completed');
      this.websocketsService.connect(() => {

        of(this.subscribeToResult()).pipe(
          concatMap(() => {
            console.log('2 run');
            return this.testCaseService.runTestCase(this.idToRun);
          })
        ).subscribe(
          () => console.log('running'),
          () => console.log('error')
        );
      });

    } else {
      console.log('IN openWebSocketWithActionResults - all completed');
    }

  }

  subscribeToResult(): void {
    for (let index = 0; index < this.idList.length; index++) {
      console.log('rrrrrr2');
      const id = this.idList[index];
      this.websocketsService.getStompClient().subscribe('/topic/public/' + id, (hello) => {
        this.onMessageReceive(hello, id);
      });
    }
  }

  onMessageReceive(hello, id: number): void {

    const actionToAdd: ActionResult = JSON.parse(hello.body);

    const indexOfTestCase: number = this.resultList
      .findIndex(e => e.id === id);

    const actionIndex = this.resultList[indexOfTestCase].innerResults.findIndex(e => e.id === actionToAdd.id);

    if (actionIndex === -1) {
      this.resultList[indexOfTestCase].innerResults.push(actionToAdd);
    } else {
      this.resultList[indexOfTestCase].innerResults[actionIndex] = actionToAdd;
    }

    // if (this.findCompletedTestCases(id, actionToAdd)) {
    //   this.isTestCasesCompleted.push(id);
    // }
    //
    // if (this.isAllTestCasesCompleted) {
    //   this.onTestCasesCompleted();
    // }

    this.testCaseInfoComponents
      .forEach((child) => child.refreshTree());
  }

  isAllTestCasesCompleted(): boolean {

    const completedTestCasesId: number[] = [];
    this.resultList.forEach((item) => item.innerResults.forEach((action) => {
      if (this.findCompletedTestCases(item.id, action)) {
        completedTestCasesId.push(item.id);
      }
    }));

    return completedTestCasesId.sort() === this.idList.sort();
  }

  findCompletedTestCases(testCaseId: number, action: ActionResult): boolean {
    console.log('IN findCompletedTestCases');
    return action.isLastAction && !this.isTestCasesCompleted.some((element) => element === testCaseId);
  }

  onTestCasesCompleted(): void {
    if (this.idList.sort() === this.isTestCasesCompleted.sort()) {
      console.log('IN onTestCasesCompleted --- true');
      this.loadTestCasesResults();
      this.websocketsService.disconnectClient();
    } else {
      console.log('IN onTestCasesCompleted --- false');
    }
  }


  ngOnDestroy(): void {
    this.websocketsService.disconnectClient();
  }

}
