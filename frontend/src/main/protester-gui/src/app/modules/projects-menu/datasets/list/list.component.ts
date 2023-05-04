import { Component, OnInit } from '@angular/core';
import {PageEvent} from "@angular/material/paginator";
import {Subscription} from "rxjs";
import {DatasetService} from "../../../../services/dataset.service";
import {MatDialog} from "@angular/material/dialog";
import {takeUntil} from "rxjs/operators";
import {CreateComponent} from "../create/create.component";
import {ViewComponent} from "../view/view.component";
import {EditComponent} from "../edit/edit.component";
import {DeleteComponent} from "../delete/delete.component";
import {Unsubscribe} from "../unsubscribe";
import {DataSet} from "../dataset.model";
import {DataSetFilterModel} from "../dataset-filter.model";

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.css']
})
export class ListComponent extends Unsubscribe implements OnInit {
  public dataSource: DataSet[];
  public pageEvent: PageEvent;

  public datasetFilter: DataSetFilterModel = new DataSetFilterModel();
  public datasetsCount = 10;
  public pageSizeOptions: number[] = [5, 10, 25, 50];
  public displayedColumns: string[] = ['NAME', 'DESCRIPTION', 'CONF'];
  private subscription: Subscription;

  constructor(private datasetService: DatasetService,
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
    const createDialogRef = this.dialog.open(CreateComponent, {
      height: 'auto',
      width: 'auto'
    });

    createDialogRef.afterClosed().pipe(takeUntil(this.destroy$)).subscribe(() => {
      this.searchDataset();
    })
  }

  public openViewDialog(id: number): void {
    const viewDialogRef = this.dialog.open(ViewComponent, {
      height: 'auto',
      width: '50%',
      data: {id: id}
    });

    viewDialogRef.afterClosed().pipe(takeUntil(this.destroy$)).subscribe(() => {
      this.searchDataset();
    })
  }

  public openEditDialog(id: number): void {
    const editDialogRef = this.dialog.open(EditComponent, {
      height: 'auto',
      width: 'auto',
      data: {id: id}
    });

    editDialogRef.afterClosed().pipe(takeUntil(this.destroy$)).subscribe(() => {
      this.searchDataset();
    })
  }

  public openDeleteDialog(id: number): void {
    const deleteDialogRef = this.dialog.open(DeleteComponent, {
      height: 'auto',
      width: 'auto',
      data: {id: id}
    });

    deleteDialogRef.afterClosed().pipe(takeUntil(this.destroy$)).subscribe(() => {
      this.searchDataset();
    });
  }

  private searchDataset(): void {
    this.subscription = this.datasetService.getAll(this.datasetFilter).subscribe(
      data => {
        this.dataSource = data.list;
        this.datasetsCount = data.totalItems;
      }
    );
  }
}
