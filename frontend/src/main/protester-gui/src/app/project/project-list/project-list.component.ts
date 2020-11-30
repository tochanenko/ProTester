import {Component, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {ProjectService} from "../../services/project.service";
import {Router} from "@angular/router";
import {ProjectFilter} from "../project-filter.model";
import {StorageService} from "../../services/auth/storage.service";
import {PageEvent} from "@angular/material/paginator";
import {Project} from "../project.model";
import {MatDialog} from "@angular/material/dialog";
import {ProjectUpdateComponent} from "../project-update/project-update.component";

@Component({
  selector: 'app-project-list',
  templateUrl: './project-list.component.html',
  styleUrls: ['./project-list.component.css']
})
export class ProjectListComponent implements OnInit {

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
    this.searchByFilter();
    this.getPageCount();
  }

  onPaginateChange(event: PageEvent): void {
    this.projectFilter.pageNumber = event.pageIndex;
    this.projectFilter.pageSize = event.pageSize;
    this.projectFilter.pageNumber = this.projectFilter.pageNumber + 1;

    this.searchByFilter();
  }

  getPageCount(): void {
    this.subscription = this.projectService.getPageCount().subscribe(
      data => this.projectsCount = data,
      error => console.log('pageCount')
    );
  }

  changeStatus(id: number): void {
    this.subscription = this.projectService.changeStatus(id).subscribe(
      data => this.searchByFilter(),
      error => console.log('not changed')
    );
  }

  openUpdateDialog(projectId: number): void {

    const updateDialogRef = this.dialog.open(ProjectUpdateComponent, {
      height: 'auto',
      width: '50%',
      data: {id: projectId}
    });

    this.subscription = updateDialogRef.afterClosed().subscribe(() => {
      this.searchByFilter();
    });
  }

  onFilterChange(event: any): void {
    this.searchByFilter();
  }

  FilterIsActive(isActive: string): void {
    this.projectFilter.projectActive = isActive;
    this.searchByFilter();
  }

  searchByFilter(): void {
    this.subscription = this.projectService.getAll(this.projectFilter).subscribe(
      data => {
        this.dataSource = data;
      },
      error => console.log('error in initDataSource')
    );
  }

  canUpdateProject(creatorId: number): boolean {
    const currentUser = this.storageService.getUser;
    return creatorId === currentUser.id || currentUser.role === 'ADMIN';
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
}
