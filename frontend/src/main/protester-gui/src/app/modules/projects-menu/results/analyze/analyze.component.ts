import {Component, OnDestroy, OnInit, QueryList, ViewChildren} from '@angular/core';

import {TestCaseAnalyzeService} from '../../../../services/analyze/test-case-analyze.service';
import {TestCaseInfoComponent} from './test-case-info/test-case-info.component';
import {TestCaseService} from '../../../../services/test-case/test-case-service';
import {WebsocketService} from '../../../../services/websocket.service';
import {forkJoin, Observable, of, Subscription} from 'rxjs';
import {ActivatedRoute} from '@angular/router';
import {concatMap, map, mergeMap} from 'rxjs/operators';
import {ActionResultModel, StatusModel, TestCaseResultModel} from '../../../../models/run-analyze/result.model';
import {TestCaseWrapperResultModel} from '../../../../models/run-analyze/wrapper.model';


@Component({
  selector: 'app-analyze',
  templateUrl: './analyze.component.html',
  styleUrls: ['./analyze.component.css']
})
export class AnalyzeComponent implements OnInit, OnDestroy {

  public resultList: TestCaseResultModel[] = [];
  private idWrapperList: number[] = [];
  private idTestCaseList: number[] = [];
  public isLoading = true;
  public isError = false;
  private idToRun: number;
  private subscription: Subscription = new Subscription();
  private testCaseWrapperResult: TestCaseWrapperResultModel[];
  private socketEndpoint = '/topic/public/';

  @ViewChildren('child') testCaseInfoComponents: QueryList<TestCaseInfoComponent>;

  constructor(private analyzeService: TestCaseAnalyzeService,
              private testCaseService: TestCaseService,
              private websocketsService: WebsocketService,
              private route: ActivatedRoute) {
    this.route.params.subscribe(params => this.idToRun = params.id);
  }

  ngOnInit(): void {

    this.subscription.add(
      this.testCaseService.findRunResultByID(this.idToRun).pipe(
        concatMap((item) => {
          this.testCaseWrapperResult = item.testCaseResults;
          item.testCaseResults.forEach(i => this.idTestCaseList.push(i.testResultId));
          return of({});
        }),
        concatMap(() => {
          return this.loadTestCasesResults();
        }),
        concatMap(() => {
          return of(this.loadStatusForFinishedTestCases());
        }),
        concatMap(() => {
          this.isLoading = false;
          return this.isAllTestCasesCompleted() ? of({}) : of(this.openWebSocketWithActionResults());
        })
      ).subscribe(
        () => {
        },
        () => this.isError = true
      )
    );
  }

  loadTestCasesResults(): Observable<TestCaseResultModel[]> {
    const observables: Observable<TestCaseResultModel>[] = [];

    this.testCaseWrapperResult.forEach((item) => {
        item.actionWrapperList.forEach(i => this.idWrapperList.push(i.id));

        observables.push(
          this.analyzeService.loadTestCasesResults(item.testResultId).pipe(
            map((result) => {
              const innerResultsTemp: ActionResultModel[] = [];

              result.innerResults.forEach(i => innerResultsTemp.push(i));

              innerResultsTemp.forEach(res => this.convertActionToJson(res));

              item.actionWrapperList.slice(result.innerResults.length)
                .forEach(i => innerResultsTemp.push(new ActionResultModel(i)));

              result.innerResults = innerResultsTemp;

              this.resultList.push(result);
              return result;
            }))
        );
      }
    );

    return forkJoin(observables);
  }

  openWebSocketWithActionResults(): Observable<any> {
    return of(this.websocketsService.connect(() => {

        this.subscription.add(
          of(this.subscribeToResult()).pipe(
            mergeMap(() => {
              return this.testCaseService.runTestCase(this.idToRun);
            })
          ).subscribe(
            () => {
            },
            () => this.isError = true
          )
        );
      })
    );
  }

  subscribeToResult(): void {
    for (const testCase of this.testCaseWrapperResult) {
      for (const wrapper of testCase.actionWrapperList) {
        this.websocketsService.getStompClient()
          .subscribe(this.socketEndpoint + wrapper.id, (actionFromMessage) => {
            this.onMessageReceive(actionFromMessage, wrapper.id, testCase.testResultId);
          });
      }
    }
  }

  onMessageReceive(actionFromMessage, wrapperId: number, testCaseId: number): void {
    const actionToAdd: ActionResultModel = JSON.parse(actionFromMessage.body);

    actionToAdd.startDate = actionToAdd.startDateStr;
    actionToAdd.endDate = actionToAdd.endDateStr;

    this.convertActionToJson(actionToAdd);

    const indexOfTestCase: number = this.resultList
      .findIndex(e => e.id === testCaseId);

    const testCase = this.resultList[indexOfTestCase];

    const actionIndex = testCase.innerResults
      .findIndex(item => item.id === wrapperId);

    actionIndex === -1
      ? testCase.innerResults.push(actionToAdd)
      : testCase.innerResults[actionIndex] = actionToAdd;

    if (actionToAdd.last) {
      testCase.endDate = actionToAdd.endDate;
      testCase.status = actionToAdd.status;
      if (actionToAdd.status === StatusModel.FAILED && testCase.innerResults[actionIndex + 1]) {
        testCase.innerResults.slice(actionIndex + 1).forEach(item => item.status = StatusModel.NOT_STARTED);
      }
      this.onAllTestCasesAreFinished();
    }

    this.testCaseInfoComponents
      .forEach((child) => child.refreshTree());
  }

  isAllTestCasesCompleted(): boolean {
    return this.resultList.every(item => item.status !== StatusModel.IN_PROGRESS);
  }

  onAllTestCasesAreFinished(): void {
    if (this.isAllTestCasesCompleted()) {
      this.disconnectClient();
    }
  }

  loadStatusForFinishedTestCases(): void {
    for (const test of this.resultList) {
      for (let actionIndex = 0; actionIndex < test.innerResults.length; actionIndex++) {
        if (test.innerResults[actionIndex].status === StatusModel.FAILED && test.innerResults[actionIndex + 1]) {
          test.innerResults.slice(actionIndex + 1).forEach(item => item.status = StatusModel.NOT_STARTED);
        }
      }
    }
  }

  convertActionToJson(action: ActionResultModel): void {
    if (action.request) {
      action.request = JSON.parse(action.request);
    }
    if (action.response) {
      action.response = JSON.parse(action.response);
    }
  }

  disconnectClient(): void {
    this.websocketsService.disconnectClient();
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
    this.disconnectClient();
  }
}

