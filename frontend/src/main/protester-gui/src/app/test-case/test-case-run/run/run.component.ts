import {Component, OnDestroy, OnInit} from '@angular/core';
import {TestCaseModel} from '../../test-case.model';
import {PageEvent} from '@angular/material/paginator';
import {TestCaseFilter} from '../../test-case-filter';
import {SelectionModel} from '@angular/cdk/collections';
import {TestCaseRunModel} from '../../test-case-run.model';
import {EnvironmentModel} from '../../environment.model';
import {Subscription} from 'rxjs';
import {TestCaseService} from '../../../services/test-case/test-case-service';
import {TestScenarioService} from '../../../services/test-scenario/test-scenario-service';
import {ActivatedRoute} from '@angular/router';
import {EnvironmentService} from '../../environment.service';
import {MatDialog} from '@angular/material/dialog';
import {SelectEnvComponent} from '../select-env/select-env.component';
import {TestScenario} from '../../../models/test-scenario';

@Component({
  selector: 'app-run',
  templateUrl: './run.component.html',
  styleUrls: ['./run.component.css']
})
export class RunComponent implements  OnInit, OnDestroy {

  public projectId: number;
  dataSource: TestCaseModel[];
  pageEvent: PageEvent;
  selection = new SelectionModel<TestCaseRunModel>(true, []);
  environmentList: EnvironmentModel[];

  testScenario: TestScenario = new TestScenario();
  testCaseFilter: TestCaseFilter = new TestCaseFilter();
  testCasesCount = 10;
  pageSizeOptions: number[] = [5, 10, 25, 50];
  displayedColumns: string[] = ['select', 'NAME', 'DESCRIPTION', 'SCENARIO', 'DATASET'];
  private subscription: Subscription;

  constructor(private testCaseService: TestCaseService,
              private testScenarioService: TestScenarioService,
              private route: ActivatedRoute,
              private environmentService: EnvironmentService,
              public dialog: MatDialog) {
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

  checkboxLabel(row?: TestCaseRunModel): string {
    if (!row) {
      return `${this.isAllSelected() ? 'select' : 'deselect'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.id + 1}`;
  }


  searchCases(): void {
    // this.subscription = this.testCaseService.getAll(this.projectId, this.testCaseFilter).subscribe(
    //   data => {
    //     this.dataSource = data.list;
    //     this.testCasesCount = data.totalItems;
    //   },
    //   error => console.log('error in initDataSource')
    // );

    this.dataSource = [
      {
        name: 'testcase1',
        description: 'd1',
        scenarioId: 1,
        dataSetResponseList: [
          {
            name: 'ds1'
          },
          {
            name: 'ds2'
          }
        ]
      },
      {
        name: 'testcase2',
        description: 'd2',
        scenarioId: 1
      },
      {
        name: 'testcase3',
        description: 'd3',
        scenarioId: 1
      },
      {
        name: 'testcase4',
        description: 'd4',
        scenarioId: 1
      }
    ];

    this.testCasesCount = 3;
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
    const testCaseRunModel = new TestCaseRunModel();
    testCaseRunModel.testCaseId = testCase.id;
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
        testCaseRunModel.env = result;
      });
    }

    this.selection.toggle(testCase);
    console.log(this.selection.selected.length + '------------------------------');
  }

  searchTestCases($event: KeyboardEvent): void {
    this.searchCases();
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
}
