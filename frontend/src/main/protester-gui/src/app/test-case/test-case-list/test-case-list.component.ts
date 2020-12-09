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

@Component({
  selector: 'app-test-case-list',
  templateUrl: './test-case-list.component.html',
  styleUrls: ['./test-case-list.component.css']
})
export class TestCaseListComponent implements OnInit, OnDestroy {

  public projectId: number;
  dataSource: TestCaseModel[];
  pageEvent: PageEvent;

  testCaseFilter: TestCaseFilter = new TestCaseFilter();
  projectsCount = 10;
  pageSizeOptions: number[] = [5, 10, 25, 50];
  displayedColumns: string[] = ['NAME', 'DESCRIPTION', 'SCENARIO', 'DATASET', 'EDIT'];
  private subscription: Subscription;

  constructor(private testCaseService: TestCaseService,
              private route: ActivatedRoute,
              private storageService: StorageService,
              public dialog: MatDialog) {
    route.params.subscribe(params => this.projectId = params[`id`]);
  }

  ngOnInit(): void {
    this.searchCases();
  }

  searchCases(): void {
    this.subscription = this.testCaseService.getAll(this.projectId, this.testCaseFilter).subscribe(
      data => {
        this.dataSource = data.list;
        this.projectsCount = data.totalItems;
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
  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
}
