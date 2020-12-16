import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {PageEvent} from '@angular/material/paginator';
import {Subscription} from 'rxjs';
import {StorageService} from '../../services/auth/storage.service';
import {MatDialog} from '@angular/material/dialog';
import {TestCaseModel} from '../test-case.model';
import {TestCaseService} from '../../services/test-case/test-case-service';
import {TestCaseFilter} from '../test-case-filter';
import {TestCaseUpdateComponent} from '../test-case-update/test-case-update.component';
import {TestScenario} from "../../models/test-scenario";
import {TestScenarioService} from "../../services/test-scenario/test-scenario-service";
import {TestCaseCreateComponent} from "../test-case-create/test-case-create.component";

@Component({
  selector: 'app-test-case-list',
  templateUrl: './test-case-list.component.html',
  styleUrls: ['./test-case-list.component.css']
})
export class TestCaseListComponent implements OnInit, OnDestroy {

  public projectId: number;
  dataSource: TestCaseModel[];
  pageEvent: PageEvent;

  testScenario: TestScenario[];
  testCaseFilter: TestCaseFilter = new TestCaseFilter();
  testCasesCount = 10;
  pageSizeOptions: number[] = [5, 10, 25, 50];
  displayedColumns: string[] = ['NAME', 'DESCRIPTION', 'SCENARIO', 'DATASET', 'EDIT', 'DELETE'];
  private subscription: Subscription;

  constructor(private testCaseService: TestCaseService,
              private testScenarioService: TestScenarioService,
              private route: ActivatedRoute,
              public dialog: MatDialog) {
    route.params.subscribe(params => this.projectId = params[`id`]);
    console.log(`Project id in test-case-list ${this.projectId}`);
  }

  ngOnInit(): void {
    this.searchCases();
  }

  searchCases(): void {
    this.subscription = this.testCaseService.getAll(this.projectId, this.testCaseFilter).subscribe(
      data => {
      //  this.testScenario = this.testScenarioService.getById(data.list[5]).subscribe();
        this.dataSource = data.list;
        this.testCasesCount = data.totalItems;
      },
      error => console.log('error in initDataSource')
    );
  }

  onPaginateChange(event: PageEvent): void {
    this.testCaseFilter.pageNumber = event.pageIndex;
    this.testCaseFilter.pageSize = event.pageSize;
    this.testCaseFilter.pageNumber = this.testCaseFilter.pageNumber + 1;

    this.searchCases();
  }
  openUpdateDialog(testCaseId: number): void {

    const updateDialogRef = this.dialog.open(TestCaseUpdateComponent, {
      height: 'auto',
      width: '50%',
      data: {id: testCaseId}
    });

    this.subscription = updateDialogRef.afterClosed().subscribe(() => {
      this.searchCases();
    });
  }
  openCreateDialog(): void {
    const createDialogRef = this.dialog.open(TestCaseCreateComponent, {
      height: 'auto',
      width: '50%',
      data: {id: this.projectId}
    });

    this.subscription = createDialogRef.afterClosed().subscribe(() => {
      this.searchCases();
    });
  }
  searchTestCases($event: KeyboardEvent):void {
    this.searchCases();
  }
  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  deleteCase(id: number): void {
    console.log(`deleted id ${id}`);
    this.testCaseService.deleteTestCase(id).subscribe();
    this.searchCases();
  }
}
