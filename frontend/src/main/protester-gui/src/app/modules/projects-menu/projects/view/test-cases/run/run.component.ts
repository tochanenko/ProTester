import {Component, OnDestroy, OnInit} from '@angular/core';
import {TestCaseModel} from '../../../../../../models/test-case/test-case.model';
import {PageEvent} from '@angular/material/paginator';
import {TestCaseFilter} from '../../../../../../models/test-case/test-case-filter';
import {SelectionModel} from '@angular/cdk/collections';
import {RunTestCaseModel} from '../../../../../../models/run-analyze/run-test-case.model';
import {forkJoin, Observable, of, Subscription} from 'rxjs';
import {ActivatedRoute, Router} from '@angular/router';
import {SelectEnvComponent} from '../select-env/select-env.component';
import {EnvironmentModel} from '../../../../../../models/environment/environment.model';
import {TestScenario} from '../../../../../../models/test-scenario';
import {TestCaseService} from '../../../../../../services/test-case/test-case-service';
import {TestScenarioService} from '../../../../../../services/test-scenario/test-scenario-service';
import {MatDialog} from '@angular/material/dialog';
import {StorageService} from '../../../../../../services/auth/storage.service';
import {EnvironmentService} from '../../../../../../services/environment/environment.service';
import {
  ValidationDataSetResponseModel,
  ValidationDataSetStatusModel
} from '../../../../../../models/run-analyze/validation-data-set-response.model';
import {ValidationComponent} from '../validation/validation.component';
import {map, mergeMap, switchMap} from 'rxjs/operators';
import {DatasetService} from '../../../../../../services/dataset.service';
import {TestCaseResponse} from '../../../../../../models/test-case/test-case-response';

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
  isError = false;
  isLoading = true;
  isDisabled = false;

  constructor(private testCaseService: TestCaseService,
              private testScenarioService: TestScenarioService,
              private dataSetService: DatasetService,
              private router: Router,
              private route: ActivatedRoute,
              private environmentService: EnvironmentService,
              public dialog: MatDialog,
              private storageService: StorageService) {
    route.params.subscribe(params => this.projectId = params[`id`]);
  }

  ngOnInit(): void {
    this.subscription.add(
      this.searchCases().pipe(
        mergeMap(() => this.loadEnvironments())
      ).subscribe(
        () => this.isLoading = false,
        () => this.isError = true
      )
    );
  }

  searchCases(): Observable<any> {
    return this.testCaseService.getAll(this.projectId, this.testCaseFilter).pipe(
      mergeMap((data: TestCaseResponse) => {
        this.dataSource = data.list;
        this.testCasesCount = data.totalItems;
        return this.loadTestCaseProperties();
      }));
  }

  loadTestCaseProperties(): Observable<any> {
    const observables: Observable<any>[] = [];

    this.dataSource.forEach((item) => {
        observables.push(
          this.testScenarioService.getById(item.scenarioId).pipe(
            map(sc => item.scenarioName = sc.name))
        );
        observables.push(
          this.dataSetService.getDataSetById(item.dataSetId).pipe(
            map(ds => item.dataSetName = ds.name))
        );
      }
    );

    return forkJoin(observables);
  }

  loadEnvironments(): Observable<EnvironmentModel[]> {
    return this.environmentService.findAll(this.projectId).pipe(map(
      data => this.environmentList = data,
      () => this.isError = true)
    );
  }

  selectTestCase(testCase: TestCaseModel): void {
    if (this.selection.isSelected(testCase)) {
      this.selection.deselect(testCase);
    } else {
      this.isDisabled = true;
      this.subscription.add(
        this.testCaseService.validateTestCaseDataSet(testCase).pipe(
          switchMap(validationResult => {
            if (validationResult.status === ValidationDataSetStatusModel.FAILED) {
              return of(this.openTestCaseDataSetErrorForm(validationResult, testCase));
            } else {
              return this.testCaseService.isEnvRequired(testCase.scenarioId).pipe(
                map((data: boolean) => {
                  data ? this.isDisabled = true : this.isDisabled = false;
                  return data
                    ? of(this.openSelectEnvironmentView(testCase))
                    : of(this.selection.select(testCase));
                }));
            }
          })
        ).subscribe(
          () => {
          },
          () => this.isError = true)
      );
      this.selection.toggle(testCase);
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
        } else {
          testCase.environmentId = result;
          this.selection.select(testCase);
        }
        this.isDisabled = false;
      }));
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

  runTestCases(): void {

    this.runTestCaseModel.testCaseResponseList = this.selection.selected;
    this.runTestCaseModel.userId = this.storageService.getUser.id;

    if (this.runTestCaseModel.testCaseResponseList.length === 0) {
      return;
    }

    this.subscription.add(
      this.testCaseService.saveTestCaseResult(this.runTestCaseModel).subscribe(
        result => this.router.navigate(['projects-menu/results/', result.id]).then(),
        () => this.isError = true
      )
    );
  }

  showProjectEnvironments(): void {
    this.router.navigateByUrl(`projects-menu/projects/${this.projectId}/environment`).then();
  }


  isAllSelected(): boolean {
    const numSelected = this.selection.selected.length;
    const numRows = this.dataSource.length;
    return numSelected === numRows;
  }

  masterToggle(): void {
    this.isAllSelected()
      ? this.selection.clear()
      : this.dataSource.forEach(row => this.selectTestCase(row));
  }

  checkboxLabel(row?: TestCaseModel): string {
    if (!row) {
      return `${this.isAllSelected() ? 'select' : 'deselect'} all`;
    }
    return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.id + 1}`;
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

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
}
