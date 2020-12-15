import {Subscription} from "rxjs";
import {Component, OnInit} from '@angular/core';
import {DataSet} from "../dataset.model";
import {PageEvent} from "@angular/material/paginator";
import {DatasetService} from "../../services/dataset.service";
import {Router} from "@angular/router";
import {StorageService} from "../../services/auth/storage.service";
import {MatDialog} from "@angular/material/dialog";
import {DataSetFilterModel} from "../dataset-filter.model";
import {DatasetEditComponent} from "../dataset-edit/dataset-edit.component";
import {DatasetDeleteComponent} from "../dataset-delete/dataset-delete.component";
import {DatasetCreateComponent} from "../dataset-create/dataset-create.component";
import {DatasetViewComponent} from "../dataset-view/dataset-view.component";
import {takeUntil} from "rxjs/operators";
import {Unsubscribe} from "../unsubscribe";

@Component({
  selector: 'app-dataset-list',
  templateUrl: './dataset-list.component.html',
  styleUrls: ['./dataset-list.component.css']
})
export class DatasetListComponent extends Unsubscribe implements OnInit {

  public dataSource: DataSet[];
  public pageEvent: PageEvent;

  public datasetFilter: DataSetFilterModel = new DataSetFilterModel();
  public datasetsCount = 10;
  public pageSizeOptions: number[] = [5, 10, 25, 50];
  public displayedColumns: string[] = ['NAME', 'DESCRIPTION', 'CONF'];
  private subscription: Subscription;

  constructor(private datasetService: DatasetService,
              private router: Router,
              private storageService: StorageService,
              public dialog: MatDialog) {
    super();
  }

  public ngOnInit(): void {
    this.searchDataset();
  }

  public onPaginateChange(event: PageEvent): void {
    this.datasetFilter.pageNumber = event.pageIndex;
    this.datasetFilter.pageSize = event.pageSize;
    this.datasetFilter.pageNumber = this.datasetFilter.pageNumber + 1;

    this.searchDataset();
  }

  public onFilterChange(event: any): void {
    this.searchDataset();
  }

  public openCreateDialog(): void {
    const createDialogRef = this.dialog.open(DatasetCreateComponent, {
      height: '85%',
      width: '50%'
    });

    createDialogRef.afterClosed().pipe(takeUntil(this.destroy$)).subscribe(() => {
      this.searchDataset();
    })
  }

  public openViewDialog(id: number): void {
    const viewDialogRef = this.dialog.open(DatasetViewComponent, {
      height: 'auto',
      width: '50%',
      data: {id: id}
    });

    viewDialogRef.afterClosed().pipe(takeUntil(this.destroy$)).subscribe(() => {
      this.searchDataset();
    })
  }

  public openEditDialog(id: number): void {
    const editDialogRef = this.dialog.open(DatasetEditComponent, {
      height: '65%',
      width: '50%',
      data: {id: id}
    });

    editDialogRef.afterClosed().pipe(takeUntil(this.destroy$)).subscribe(() => {
      this.searchDataset();
    })
  }

  public openDeleteDialog(id: number): void {
    const deleteDialogRef = this.dialog.open(DatasetDeleteComponent, {
      height: '25%',
      width: '30%',
      data: {id: id}
    });

    deleteDialogRef.afterClosed().pipe(takeUntil(this.destroy$)).subscribe(() => {
      this.searchDataset();
    })
  }

  private searchDataset(): void {
    this.subscription = this.datasetService.getAll(this.datasetFilter).subscribe(
      data => {
        console.log(data.list);
        this.dataSource = data.list;
        this.datasetsCount = data.totalItems;
      },
      error => console.log('error in initDataSource')
    );
  }
}
