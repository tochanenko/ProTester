import {Component, OnDestroy, OnInit, QueryList, ViewChildren} from '@angular/core';
import {ActionResult, TestCaseResult} from './result.model';
import {TestCaseAnalyzeService} from './test-case-analyze.service';
import {TestCaseInfoComponent} from './test-case-info/test-case-info.component';
import {TestCaseService} from '../services/test-case/test-case-service';
import {WebsocketsService} from './websockets.service';
import {forkJoin, Observable, of, Subscription} from 'rxjs';
import {ActivatedRoute} from '@angular/router';
import {concatMap, map} from 'rxjs/operators';
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
  private isTestCasesCompleted: number[] = [];
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

          // const testCaseResultsIdList: number[] = [];
          //
          // item.testCaseResults.forEach(
          //   (testCase) => testCaseResultsConverted.push(testCase.testResultId)
          // );
          //
          // this.idList = testCaseResultsIdList;
          return of({});
        }),
        concatMap(() => {
          return this.loadTestCasesResults();
        }),
        concatMap(() => {
          this.isLoading = false;
          return of(this.openWebSocketWithActionResults());
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
          map((item2) => {
            if (item2.innerResults.length === 0) {
              const innerResultsTemp: ActionResult[] = [];

              item.actionWrapperList.forEach((wrapper) => innerResultsTemp.push(new ActionResult(wrapper)));

              item2.innerResults = innerResultsTemp;
            }
            this.resultList.push(item2);
            // console.log('_________________________' + JSON.stringify(this.resultList));
            return item2;
          }))
        );
      }
    );

    // for (const id of this.idList) {
    //   observables.push(this.analyzeService.loadTestCasesResults(id).pipe(
    //     map((item) => {
    //
    //       this.resultList.push(item);
    //       return item;
    //     }))
    //   );
    // }

    return forkJoin(observables);
  }

  openWebSocketWithActionResults(): Observable<any> {

    // todo
    // if (!this.isAllTestCasesCompleted()) {
    console.log('IN openWebSocketWithActionResults - not all completed');

    return of(this.websocketsService.connect(() => {

        // todo delete inner subscription
        this.subscription.add(
          of(this.subscribeToResult()).pipe(
            concatMap(() => {
              console.log('2 run');
              return this.testCaseService.runTestCase(this.idToRun);
            })
          ).subscribe(
            () => console.log('running'),
            () => console.log('error')
          )
        );
      })
    );

    // } else {
    //   console.log('IN openWebSocketWithActionResults - all completed');
    // }

  }

  subscribeToResult(): void {
    for (const testCase of this.testCaseWrapperResult) {
      for (const wrapper of testCase.actionWrapperList) {
        console.log('IN subscribe d-' + testCase.id + ' w- ' + wrapper.id);
        this.websocketsService.getStompClient()
          .subscribe('/topic/public/' + wrapper.id, (actionFromMessage) => {
            this.onMessageReceive(actionFromMessage, wrapper.id, testCase.testResultId);
          });
      }
    }
  }


  onMessageReceive(actionFromMessage, wrapperId: number, testCaseId: number): void {

    const actionToAdd: ActionResult = JSON.parse(actionFromMessage.body);

    const indexOfTestCase: number = this.resultList
      .findIndex(e => e.id === testCaseId);

    const actionIndex = this.resultList[indexOfTestCase].innerResults
      .findIndex(item => item.id === wrapperId);

    actionIndex === -1
      ? this.resultList[indexOfTestCase].innerResults.push(actionToAdd)
      : this.resultList[indexOfTestCase].innerResults[actionIndex] = actionToAdd;


    if (actionIndex === -1) {
      this.resultList[indexOfTestCase].innerResults.push(actionToAdd);
    } else {
      this.resultList[indexOfTestCase].innerResults[actionIndex] = actionToAdd;
    }

    // todo
    // if (this.findCompletedTestCases(id, actionToAdd)) {
    //   this.isTestCasesCompleted.push(id);
    // }
    //

    // todo
    // if (this.isAllTestCasesCompleted) {
    //   this.onTestCasesCompleted();
    // }

    this.testCaseInfoComponents
      .forEach((child) => child.refreshTree());
  }


  findCompletedTestCases(testCaseId: number, action: ActionResult): boolean {
    console.log('IN findCompletedTestCases');
    return action.isLastAction && !this.isTestCasesCompleted.some((element) => element === testCaseId);
  }

  onTestCasesCompleted(): void {
    // if (this.idList.sort() === this.isTestCasesCompleted.sort()) {
    //   console.log('IN onTestCasesCompleted --- true');
    //   this.loadTestCasesResults();
    //   this.websocketsService.disconnectClient();
    // } else {
    //   console.log('IN onTestCasesCompleted --- false');
    // }
  }

  ngOnDestroy(): void {
    this.websocketsService.disconnectClient();
    this.subscription.unsubscribe();
  }

}
