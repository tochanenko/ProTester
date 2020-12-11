import {Component, OnDestroy, OnInit} from '@angular/core';
import {PageEvent} from '@angular/material/paginator';
import {Subscription} from 'rxjs';
import {Router} from '@angular/router';
import {MatDialog} from '@angular/material/dialog';
import {Action} from '../action.model';
import {ActionService} from '../../services/action/action.service';
import {ActionUpdateComponent} from '../action-update/action-update.component';
import {ActionFilter} from '../action-filter.model';

@Component({
  selector: 'app-actions-list',
  templateUrl: './actions-list.component.html',
  styleUrls: ['./actions-list.component.css']
})
export class ActionsListComponent implements OnInit, OnDestroy {


  dataSource: Action[];
  pageEvent: PageEvent;

  displayedColumns: string[] = ['NAME', 'DESCRIPTION', 'TYPE', 'EDIT'];
  actionFilter: ActionFilter = new ActionFilter();
  actionsCount = 10;
  pageSizeOptions: number[] = [5, 10, 25, 50];
  private subscription: Subscription;

  constructor(private actionService: ActionService,
              private router: Router,
              public dialog: MatDialog) {
  }
  ngOnInit(): void {
    this.searchActions();
  }

  searchActions(): void {
    if (this.actionFilter.actionType === '') {
      this.subscription = this.actionService.getAll(this.actionFilter).subscribe(
        data => {
          this.dataSource = data.list;
          console.log(data.list);
          this.actionsCount = data.totalItems;
        },
        error => console.log('error in initDataSource')
      );
    } else {
      this.subscription = this.actionService.getAllFiltered(this.actionFilter).subscribe(
        data => {
          this.dataSource = data.list;
          this.actionsCount = data.totalItems;
        },
        error => console.log('error in initDataSource')
      );
    }
  }

  onPaginateChange(event: PageEvent): void {
    this.actionFilter.pageNumber = event.pageIndex;
    this.actionFilter.pageSize = event.pageSize;
    this.actionFilter.pageNumber = this.actionFilter.pageNumber + 1;

    this.searchActions();
  }
  onFilterChange(event: any): void {
    this.searchActions();
  }

  FilterIsActive(actionType: string): void {
    this.actionFilter.actionType = actionType;
    this.searchActions();
  }

  openUpdateDialog(actionId: number): void {

    const updateDialogRef = this.dialog.open(ActionUpdateComponent, {
      height: 'auto',
      width: '50%',
      data: {id: actionId}
    });

    this.subscription = updateDialogRef.afterClosed().subscribe(() => {
       this.searchActions();
    });
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
}
