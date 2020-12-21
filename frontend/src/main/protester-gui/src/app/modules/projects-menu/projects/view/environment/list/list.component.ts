import {Component, OnDestroy, OnInit} from '@angular/core';
import {EnvironmentModel} from '../../../../../../models/environment.model';
import {PageEvent} from '@angular/material/paginator';
import {Subscription} from 'rxjs';
import {EnvironmentService} from '../../../../../../services/environment.service';
import {Router} from '@angular/router';
import {MatDialog} from '@angular/material/dialog';
import {EditComponent} from '../edit/edit.component';
import {CreateComponent} from '../create/create.component';
import {EnvironmentFilterModel} from '../environment-filter.model';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.css']
})
export class ListComponent implements OnInit, OnDestroy {

  dataSource: EnvironmentModel[];
  pageEvent: PageEvent;

  environmentFilter: EnvironmentFilterModel;
  projectsCount = 10;
  pageSizeOptions: number[] = [5, 10, 25, 50];
  displayedColumns: string[] = ['NAME', 'DESCRIPTION', 'USERNAME', 'PASSWORD', 'URL', 'CONF'];
  subscription: Subscription;

  constructor(private environmentService: EnvironmentService,
              private router: Router,
              public dialog: MatDialog) {
    this.subscription = new Subscription();
    this.environmentFilter = new EnvironmentFilterModel();
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
    this.subscription.add(this.environmentService.findAll().subscribe(
      data => {
        this.dataSource = data;
      },
      error => console.log('error in initDataSource')
    ));
  }

  openUpdateDialog(environmentToUpdate: EnvironmentModel): void {

    const updateDialogRef = this.dialog.open(EditComponent, {
      height: 'auto',
      width: '50%',
      data: {environment: environmentToUpdate}
    });

    this.subscription.add(
      updateDialogRef.afterClosed().subscribe(env => {
        console.log(JSON.stringify(env));
        this.environmentService.update(env)
          .subscribe(
            () => {
              this.searchEnvironments();
            },
            error => console.log('error')
          );
      })
    );
  }

  openCreateDialog(): void {

    const createDialogRef = this.dialog.open(CreateComponent, {
      height: 'auto',
      width: '50%',
      data: {}
    });

    this.subscription.add(
      createDialogRef.afterClosed().subscribe(env => {
        this.environmentService.create(env)
          .subscribe(
            () => this.searchEnvironments(),
            error => console.log('error')
          );
      })
    );
  }

  deleteEnvironment(id: number): void {
    this.subscription.add(this.environmentService.delete(id).subscribe(
      () => this.searchEnvironments(),
      () => console.log('error')
      )
    );
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

}
