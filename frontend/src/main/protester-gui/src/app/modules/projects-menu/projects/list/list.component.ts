import {Component, OnDestroy, OnInit} from '@angular/core';
import {Project} from '../../../../models/project/project.model';
import {PageEvent} from '@angular/material/paginator';
import {ProjectFilter} from '../../../../models/project/project-filter.model';
import {Subscription} from 'rxjs';
import {ProjectService} from '../../../../services/project.service';
import {Router} from '@angular/router';
import {StorageService} from '../../../../services/auth/storage.service';
import {MatDialog} from '@angular/material/dialog';
import {EditComponent} from '../edit/edit.component';
import {CreateComponent} from '../create/create.component';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.css']
})
export class ListComponent implements OnInit, OnDestroy {

  dataSource: Project[];
  pageEvent: PageEvent;

  projectFilter: ProjectFilter;
  projectsCount = 10;
  pageSizeOptions: number[] = [5, 10, 25, 50];
  displayedColumns: string[] = ['NAME', 'LINK', 'CREATOR', 'STATUS', 'CONF'];
  subscription: Subscription;
  isError = false;
  isLoading = true;

  constructor(private projectService: ProjectService,
              private router: Router,
              private storageService: StorageService,
              public dialog: MatDialog) {
    this.subscription = new Subscription();
    this.projectFilter = new ProjectFilter();
  }

  ngOnInit(): void {
    this.searchProjects();
  }

  searchProjects(): void {
    if (this.projectFilter.projectActive === '') {
      this.subscription.add(this.projectService.getAll(this.projectFilter).subscribe(
        data => {
          this.dataSource = data.list;
          this.projectsCount = data.totalItems;
          this.isLoading = false;
        },
        () => this.isError = true
      ));
    } else {
      this.subscription.add(this.projectService.getAllFiltered(this.projectFilter).subscribe(
        data => {
          this.dataSource = data.list;
          this.projectsCount = data.totalItems;
          this.isLoading = false;
        },
        () => this.isError = true
      ));
    }
  }

  onPaginateChange(event: PageEvent): void {
    this.projectFilter.pageNumber = event.pageIndex;
    this.projectFilter.pageSize = event.pageSize;
    this.projectFilter.pageNumber = this.projectFilter.pageNumber + 1;

    this.searchProjects();
  }

  onFilterChange(event: any): void {
    this.searchProjects();
  }

  FilterIsActive(isActive: string): void {
    this.projectFilter.projectActive = isActive;
    this.searchProjects();
  }

  canUpdateProject(creatorId: number): boolean {
    const currentUser = this.storageService.getUser;
    return creatorId === currentUser.id || currentUser.role === 'ADMIN';
  }

  changeStatus(id: number): void {
    this.subscription.add(this.projectService.changeStatus(id).subscribe(
      () => this.searchProjects(),
      () => this.isError = true
    ));
  }

  openUpdateDialog(projectId: number): void {

    const updateDialogRef = this.dialog.open(EditComponent, {
      height: 'auto',
      width: '50%',
      data: {id: projectId}
    });

    this.subscription.add(
      updateDialogRef.afterClosed().subscribe(
        () => this.searchProjects(),
        () => this.isError = true)
    );
  }

  openCreateDialog(): void {

    const createDialogRef = this.dialog.open(CreateComponent, {
      height: 'auto',
      width: '50%',
      data: {userId: this.storageService.getUser.id}
    });

    this.subscription.add(
      createDialogRef.afterClosed().subscribe(
        () => this.searchProjects(),
        () => this.isError = true)
    );
  }

  openCreatorProfile(creatorId: number): void {
    this.router.navigate(['/account/users/', creatorId]).then();
  }

  canViewCreatorProfile(): boolean {
    return this.storageService.getUser.role === 'ADMIN';
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

}
