import {Component, OnDestroy, OnInit} from '@angular/core';
import {PageEvent} from "@angular/material/paginator";
import {TestScenario} from "../../../../../../models/test-scenario";
import {Subscription} from "rxjs";
import {TestCaseService} from "../../../../../../services/test-case/test-case-service";
import {TestScenarioService} from "../../../../../../services/test-scenario/test-scenario-service";
import {ActivatedRoute} from "@angular/router";
import {MatDialog} from "@angular/material/dialog";
import {TestCaseFilter} from "../test-case-filter";
import {EditComponent} from "../edit/edit.component";
import {CreateComponent} from "../create/create.component";
import {TestCaseModel} from "../test-case.model";

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.css']
})
export class ListComponent implements OnInit, OnDestroy {
  public projectId: number;
  dataSource: TestCaseModel[];
  pageEvent: PageEvent;

  testScenario: TestScenario = new TestScenario();
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

    const updateDialogRef = this.dialog.open(EditComponent, {
      height: 'auto',
      width: '50%',
      data: {id: testCaseId}
    });

    this.subscription = updateDialogRef.afterClosed().subscribe(() => {
      this.searchCases();
    });
  }
  openCreateDialog(): void {
    const createDialogRef = this.dialog.open(CreateComponent, {
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
