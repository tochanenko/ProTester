import {Component, OnDestroy, OnInit, QueryList, ViewChildren} from '@angular/core';
import {ActionResult, Status, TestCaseResult} from './result.model';
import {TestCaseAnalyzeService} from './test-case-analyze.service';
import {TestCaseInfoComponent} from './test-case-info/test-case-info.component';
import {TestCaseService} from '../services/test-case/test-case-service';
import {WebsocketsService} from './websockets.service';
import {forkJoin, Observable, of, Subscription} from 'rxjs';
import {ActivatedRoute} from '@angular/router';
import {concatMap, map, mergeMap} from 'rxjs/operators';
import {TestCaseWrapperResult} from './wrapper.model';

@Component({
  selector: 'app-test-case-run',
  templateUrl: './test-case-analyze.component.html',
  styleUrls: ['./test-case-analyze.component.css']
})
export class TestCaseAnalyzeComponent implements OnInit, OnDestroy {

  public resultList: TestCaseResult[] = [];
  private idWrapperList: number[] = [];
  private idTestCaseList: number[] = [];
  public isLoading = true;
  public isError = false;
  private idToRun: number;
  private subscription: Subscription = new Subscription();
  private testCaseWrapperResult: TestCaseWrapperResult[];

  @ViewChildren('child') testCaseInfoComponents: QueryList<TestCaseInfoComponent>;

  constructor(private analyzeService: TestCaseAnalyzeService,
              private testCaseService: TestCaseService,
              private websocketsService: WebsocketsService,
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
          this.isLoading = false;
          return this.isAllTestCasesCompleted() ? of({}) : of(this.openWebSocketWithActionResults());
        })
      ).subscribe(
        () => console.log('successfully'),
        () => console.log('error')
      )
    );
  }

  loadTestCasesResults(): Observable<TestCaseResult[]> {
    const observables: Observable<TestCaseResult>[] = [];

    this.testCaseWrapperResult.forEach((item) => {
        item.actionWrapperList.forEach(i => this.idWrapperList.push(i.id));

        observables.push(this.analyzeService.loadTestCasesResults(item.testResultId).pipe(
          map((result) => {
            const innerResultsTemp: ActionResult[] = [];
            result.innerResults.forEach(i => innerResultsTemp.push(i));

            item.actionWrapperList.slice(result.innerResults.length)
              .forEach(i => innerResultsTemp.push(new ActionResult(i)));

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
              console.log('before run');
              return this.testCaseService.runTestCase(this.idToRun);
            })
          ).subscribe(
            () => console.log('running'),
            () => console.log('error')
          )
        );
      })
    );
  }

  subscribeToResult(): void {
    for (const testCase of this.testCaseWrapperResult) {
      for (const wrapper of testCase.actionWrapperList) {
        this.websocketsService.getStompClient()
          .subscribe('/topic/public/' + wrapper.id, (actionFromMessage) => {
            this.onMessageReceive(actionFromMessage, wrapper.id, testCase.testResultId);
          });
      }
    }
  }

  onMessageReceive(actionFromMessage, wrapperId: number, testCaseId: number): void {
    const actionToAdd: ActionResult = JSON.parse(actionFromMessage.body);

    actionToAdd.startDate = actionToAdd.startDateStr;
    actionToAdd.endDate = actionToAdd.endDateStr;

    const indexOfTestCase: number = this.resultList
      .findIndex(e => e.id === testCaseId);

    const testCase = this.resultList[indexOfTestCase];

    const actionIndex = testCase.innerResults
      .findIndex(item => item.id === wrapperId);

    actionIndex === -1
      ? testCase.innerResults.push(actionToAdd)
      : testCase.innerResults[actionIndex] = actionToAdd;

    if (actionToAdd.isLastAction) {
      testCase.endDate = actionToAdd.endDate;
      testCase.status = actionToAdd.status;
    }

    this.onAllTestCasesAreFinished();

    this.testCaseInfoComponents
      .forEach((child) => child.refreshTree());
  }

  isAllTestCasesCompleted(): boolean {
    return this.resultList.some(item => item.status !== Status.IN_PROGRESS);
  }

  onAllTestCasesAreFinished(): void {

    if (this.isAllTestCasesCompleted()) {
      this.disconnectClient();
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

