import {Component, OnDestroy, OnInit} from '@angular/core';
import {Project} from "../../../models/project/project.model";
import {PageEvent} from "@angular/material/paginator";
import {ProjectFilter} from "../../../models/project/project-filter.model";
import {Subscription} from "rxjs";
import {ProjectService} from "../../../services/project.service";
import {Router} from "@angular/router";
import {StorageService} from "../../../services/auth/storage.service";
import {MatDialog} from "@angular/material/dialog";
import {EditComponent} from "../edit/edit.component";

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.css']
})
export class ListComponent implements OnInit, OnDestroy {

  dataSource: Project[];
  pageEvent: PageEvent;

  projectFilter: ProjectFilter = new ProjectFilter();
  projectsCount = 10;
  pageSizeOptions: number[] = [5, 10, 25, 50];
  displayedColumns: string[] = ['NAME', 'LINK', 'CREATOR', 'STATUS', 'CONF'];
  private subscription: Subscription;

  constructor(private projectService: ProjectService,
              private router: Router,
              private storageService: StorageService,
              public dialog: MatDialog) {
  }

  ngOnInit(): void {
    this.searchProjects();
  }

  searchProjects(): void {
    if (this.projectFilter.projectActive === '') {
      this.subscription = this.projectService.getAll(this.projectFilter).subscribe(
        data => {
          this.dataSource = data.list;
          this.projectsCount = data.totalItems;
        },
        error => console.log('error in initDataSource')
      );
    } else {
      this.subscription = this.projectService.getAllFiltered(this.projectFilter).subscribe(
        data => {
          this.dataSource = data.list;
          this.projectsCount = data.totalItems;
        },
        error => console.log('error in initDataSource')
      );
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
    this.subscription = this.projectService.changeStatus(id).subscribe(
      data => this.searchProjects(),
      error => console.log('not changed')
    );
  }

  openUpdateDialog(projectId: number): void {

    const updateDialogRef = this.dialog.open(EditComponent, {
      height: 'auto',
      width: '50%',
      data: {id: projectId}
    });

    this.subscription = updateDialogRef.afterClosed().subscribe(() => {
      this.searchProjects();
    });
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

}
