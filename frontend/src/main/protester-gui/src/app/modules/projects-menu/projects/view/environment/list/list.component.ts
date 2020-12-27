import {Component, OnDestroy, OnInit} from '@angular/core';
import {EnvironmentModel} from '../../../../../../models/environment/environment.model';
import {PageEvent} from '@angular/material/paginator';
import {Subscription} from 'rxjs';
import {EnvironmentService} from '../../../../../../services/environment/environment.service';
import {ActivatedRoute, Router} from '@angular/router';
import {MatDialog} from '@angular/material/dialog';
import {EditComponent} from '../edit/edit.component';
import {CreateComponent} from '../create/create.component';
import {EnvironmentFilterModel} from '../../../../../../models/environment/environment-filter.model';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.css']
})
export class ListComponent implements OnInit, OnDestroy {

  dataSource: EnvironmentModel[];
  pageEvent: PageEvent;
  projectId: number;
  isError = false;
  isLoading = true;

  environmentFilter: EnvironmentFilterModel;
  environmentsCount = 10;
  pageSizeOptions: number[] = [5, 10, 25, 50];
  displayedColumns: string[] = ['NAME', 'DESCRIPTION', 'USERNAME', 'PASSWORD', 'URL', 'CONF'];
  subscription: Subscription;

  constructor(private environmentService: EnvironmentService,
              private router: Router,
              public dialog: MatDialog,
              private route: ActivatedRoute) {
    this.route.params.subscribe(params => this.projectId = params[`id`]);
    this.environmentFilter = new EnvironmentFilterModel();
    this.subscription = new Subscription();
  }

  ngOnInit(): void {
    this.searchEnvironments();
  }

  onPaginateChange(event: PageEvent): void {
    this.environmentFilter.pageNumber = event.pageIndex;
    this.environmentFilter.pageSize = event.pageSize;
    this.environmentFilter.pageNumber = this.environmentFilter.pageNumber + 1;

    this.searchEnvironments();
  }


  onFilterChange(event: any): void {
    this.searchEnvironments();
  }

  searchEnvironments(): void {
    this.subscription.add(
      this.environmentService.findAllPaginated(this.projectId, this.environmentFilter).subscribe(
        data => {
          this.dataSource = data.list;
          this.environmentsCount = data.totalItems;
          this.isLoading = false;
        },
        () => this.isError = true
      )
    );
  }

  openUpdateDialog(environmentToUpdate: EnvironmentModel): void {

    const updateDialogRef = this.dialog.open(EditComponent, {
      height: 'auto',
      width: '50%',
      data: {environment: environmentToUpdate}
    });

    this.subscription.add(
      updateDialogRef.afterClosed()
        .subscribe(env => {
          this.environmentService.update(env).subscribe(
            () => this.searchEnvironments(),
            () => this.isError = true
          );
        })
    );
  }

  openCreateDialog(): void {

    const createDialogRef = this.dialog.open(CreateComponent, {
      height: 'auto',
      width: '50%',
      data: {projectId: this.projectId}
    });

    this.subscription.add(
      createDialogRef.afterClosed().subscribe(env => {
        this.environmentService.create(env).subscribe(
          () => this.searchEnvironments(),
          () => this.isError = true
        );
      })
    );
  }

  deleteEnvironment(id: number): void {
    this.subscription.add(this.environmentService.delete(id).subscribe(
      () => this.searchEnvironments(),
      () => this.isError = true
      )
    );
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

}
