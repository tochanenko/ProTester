import {Component, OnDestroy, OnInit} from '@angular/core';
import {TestCaseModel} from '../test-case.model';
import {PageEvent} from '@angular/material/paginator';
import {TestCaseFilter} from '../test-case-filter';
import {SelectionModel} from '@angular/cdk/collections';
import {RunTestCaseModel} from '../run-test-case.model';
import {Subscription} from 'rxjs';
import {ActivatedRoute, Router} from '@angular/router';
import {SelectEnvComponent} from '../select-env/select-env.component';
import {EnvironmentModel} from '../../../../../../models/environment.model';
import {TestScenario} from '../../../../../../models/test-scenario';
import {TestCaseService} from '../../../../../../services/test-case/test-case-service';
import {TestScenarioService} from '../../../../../../services/test-scenario/test-scenario-service';
import {MatDialog} from '@angular/material/dialog';
import {StorageService} from '../../../../../../services/auth/storage.service';
import {TestCaseRunAnalyzeService} from '../../../../../../services/test-case/run-analyze/test-case-run-analyze.service';
import {EnvironmentService} from '../../../../../../services/environment/environment.service';
import {
  ValidationDataSetResponseModel,
  ValidationDataSetStatusModel
} from '../../../../../../models/run-analyze/validation-data-set-response.model';
import {ValidationComponent} from '../validation/validation.component';

@Component({
  selector: 'app-run',
  templateUrl: './run.component.html',
  styleUrls: ['./run.component.css']
})
export class RunComponent implements OnInit, OnDestroy {

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
  private subscription: Subscription = new Subscription();

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
    this.subscription.add(
      this.testCaseService.getAll(this.projectId, this.testCaseFilter).subscribe(
        data => {
          this.dataSource = data.list;
          this.testCasesCount = data.totalItems;
        },
        error => console.log('error in initDataSource')
      )
    );
  }

  loadEnvironments(): void {
    this.subscription.add(
      this.testCaseService.loadEnvironments()
        .subscribe(
          data => {
            this.environmentList = data;
          },
          error => console.log('env load error')
        )
    );
  }

  selectTestCase(testCase: TestCaseModel): void {
    console.log(this.testCaseService.isEnvRequired(this.projectId, testCase.id)._isScalar + '000000000');

    if (this.selection.isSelected(testCase)) {
      this.selection.deselect(testCase);
    } else {

      this.testCaseService.validateTestCaseDataSet(testCase).subscribe(
        validationResult => {
          if (validationResult.status === ValidationDataSetStatusModel.FAILED) {
            this.openTestCaseDataSetErrorForm(validationResult, testCase);
          } else {
            if (testCase.name === 'shhshs' || testCase.name === 'testcase32') {
              // if (this.testCaseService.isEnvRequired(this.projectId, testCase.id)._isScalar) {

              this.openSelectEnvironmentView(testCase);
            }
          }
        },
        () => console.log('error')
      );

    }
  }

  openSelectEnvironmentView(testCase: TestCaseModel): void {

    const updateDialogRef = this.dialog.open(SelectEnvComponent, {
      height: 'auto',
      width: '40%',
      data: {environments: this.environmentList, testCaseName: testCase.name}
    });

    this.subscription.add(
      updateDialogRef.afterClosed().subscribe(result => {

          if (result === undefined) {
            this.selection.deselect(testCase);
          }
          testCase.environment = result;
        }
      )
    );

    this.selection.toggle(testCase);
  }

  openTestCaseDataSetErrorForm(validationResult: ValidationDataSetResponseModel, testCase: TestCaseModel): void {
    const errorDialogRef = this.dialog.open(ValidationComponent, {
      data: {result: validationResult}
    });

    this.subscription.add(
      errorDialogRef.afterClosed().subscribe(
        () => this.selection.deselect(testCase)
      )
    );

    this.selection.toggle(testCase);
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

    this.runTestCaseModel.testCaseResponseList = this.selection.selected;
    this.runTestCaseModel.userId = this.storageService.getUser.id;

    if (this.runTestCaseModel.testCaseResponseList.length === 0) {
      return;
    }

    this.subscription.add(
      this.testCaseService.saveTestCaseResult(this.runTestCaseModel).subscribe(
        result => {
          this.router.navigate(['projects-menu/results/', result.id]).then();

        }, error => console.log('IN saveTestCaseResult - error')
      )
    );
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }
}
