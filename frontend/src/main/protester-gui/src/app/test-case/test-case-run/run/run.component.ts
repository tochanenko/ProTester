import {Component, OnDestroy, OnInit} from '@angular/core';
import {TestCaseModel} from '../../test-case.model';
import {PageEvent} from '@angular/material/paginator';
import {TestCaseFilter} from '../../test-case-filter';
import {SelectionModel} from '@angular/cdk/collections';
import {RunTestCaseModel} from '../../run-test-case.model';
import {EnvironmentModel} from '../../environment.model';
import {Subscription} from 'rxjs';
import {TestCaseService} from '../../../services/test-case/test-case-service';
import {TestScenarioService} from '../../../services/test-scenario/test-scenario-service';
import {ActivatedRoute, Router} from '@angular/router';
import {EnvironmentService} from '../../environment.service';
import {MatDialog} from '@angular/material/dialog';
import {SelectEnvComponent} from '../select-env/select-env.component';
import {TestScenario} from '../../../models/test-scenario';
import {StorageService} from '../../../services/auth/storage.service';
import {TestCaseRunAnalyzeService} from '../../../services/test-case-run-analyze.service';

@Component({
  selector: 'app-run',
  templateUrl: './run.component.html',
  styleUrls: ['./run.component.css']
})
export class RunComponent implements  OnInit, OnDestroy {

  runTestCaseModel = new RunTestCaseModel();
  public projectId: number;
  dataSource: TestCaseModel[];
  pageEvent: PageEvent;
  selection = new SelectionModel<TestCaseModel>(true, []);
  environmentList: EnvironmentModel[];

  testScenario: TestScenario = new TestScenario();
  testCaseFilter: TestCaseFilter = new TestCaseFilter();
  testCasesCount = 10;
  pageSizeOptions: number[] = [5, 10, 25, 50];
  displayedColumns: string[] = ['select', 'NAME', 'DESCRIPTION', 'SCENARIO', 'DATASET'];
  private subscription: Subscription;

  constructor(private testCaseService: TestCaseService,
              private testScenarioService: TestScenarioService,
              private router: Router,
              private route: ActivatedRoute,
              private environmentService: EnvironmentService,
              public dialog: MatDialog,
              private storageService: StorageService,
              private runAnalyzeService: TestCaseRunAnalyzeService) {
    route.params.subscribe(params => this.projectId = params[`id`]);
  }

  ngOnInit(): void {
    this.searchCases();
    this.loadEnvironments();
  }

  isAllSelected(): boolean {
    const numSelected = this.selection.selected.length;
    const numRows = this.dataSource.length;
    return numSelected === numRows;
  }

  masterToggle(): void {
    this.isAllSelected() ?
      this.selection.clear() :
      this.dataSource.forEach(row => this.selectTestCase(row));
  }

  checkboxLabel(row?: TestCaseModel): string {
    if (!row) {
      return `${this.isAllSelected() ? 'select' : 'deselect'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.id + 1}`;
  }


  searchCases(): void {
    this.subscription = this.testCaseService.getAll(this.projectId, this.testCaseFilter).subscribe(
      data => {
        this.dataSource = data.list;
        this.testCasesCount = data.totalItems;
      },
      error => console.log('error in initDataSource')
    );

    // this.dataSource = [
    //   {
    //     name: 'testcase1',
    //     description: 'd1',
    //     scenarioId: 1,
    //     dataSetResponseList: [
    //       {
    //         name: 'ds1'
    //       },
    //       {
    //         name: 'ds2'
    //       }
    //     ]
    //   },
    //   {
    //     name: 'testcase2',
    //     description: 'd2',
    //     scenarioId: 1
    //   },
    //   {
    //     name: 'testcase3',
    //     description: 'd3',
    //     scenarioId: 1
    //   },
    //   {
    //     name: 'testcase4',
    //     description: 'd4',
    //     scenarioId: 1
    //   }
    // ];
    //
    // this.testCasesCount = 3;
  }

  loadEnvironments(): void {
    this.environmentList = [
      {
        id: 1,
        url: 'url1'
      },
      {
        id: 2,
        url: 'url2'
      }
    ];

    console.log('environmentList init');
  }

  selectTestCase(testCase: TestCaseModel): void {
    console.log(this.environmentList);

    // todo to it when env is not set, add name of test case to dialog view
    if (testCase.name === 'testcase2' || testCase.name === 'testcase3') {
      const updateDialogRef = this.dialog.open(SelectEnvComponent, {
        height: 'auto',
        width: '40%',
        data: {environments: this.environmentList, testCaseName: testCase.name}
      });

      this.subscription = updateDialogRef.afterClosed().subscribe(result => {
        console.log('after closed idenv =' + result);

        if (result === undefined) {
          this.selection.deselect(testCase);
        }
        this.runTestCaseModel.env = result;
      });
    }

    this.selection.toggle(testCase);
    console.log(this.selection.selected.length + '------------------------------');
  }

  searchTestCases($event: KeyboardEvent): void {
    this.searchCases();
  }

  onPaginateChange(event: PageEvent): void {
    this.testCaseFilter.pageNumber = event.pageIndex;
    this.testCaseFilter.pageSize = event.pageSize;
    this.testCaseFilter.pageNumber = this.testCaseFilter.pageNumber + 1;

    this.searchCases();
  }

  runTestCases(): void {

    this.runTestCaseModel.testCaseRequestList = this.selection.selected;
    this.runTestCaseModel.userId = this.storageService.getUser.id;

    this.runTestCaseModel.testCaseRequestList[0].dataSetId = [1];

    console.log(this.runTestCaseModel.userId + '-------------------');

    this.testCaseService.saveTestCaseResult(this.runTestCaseModel).subscribe(
      result => {
        console.log('-------------successful id='  + result.id + ', ' + result.testCaseResult + ', ' + result.userId);
        this.runAnalyzeService.runResultModel = result;
        this.router.navigateByUrl('/test-case-analyze').then();

      }, error => console.log('---------------error')
    );
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
}
