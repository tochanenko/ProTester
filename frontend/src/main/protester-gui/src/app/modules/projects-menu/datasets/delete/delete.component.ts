import {Component, Inject, OnInit} from '@angular/core';
import {DatasetService} from "../../../../services/dataset.service";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {DataSet} from "../dataset.model";
import {ListComponent} from "../list/list.component";

@Component({
  selector: 'app-delete',
  templateUrl: './delete.component.html',
  styleUrls: ['./delete.component.css']
})
export class DeleteComponent implements OnInit {

  public dataset: DataSet;

  constructor(private datasetService: DatasetService,
              private dialogRef: MatDialogRef<ListComponent>,
              @Inject(MAT_DIALOG_DATA) public data: DataSet) {
  }

  public ngOnInit(): void {
    this.datasetService.getDataSetById(this.data.id)
      .subscribe((dataset: DataSet) => this.dataset = dataset);
  }

  public deleteDataSet(): void {
    this.datasetService.delete(this.data.id)
      .subscribe(() => this.dialogRef.close(),
        () => {});
  }

  public onCancel(): void {
    this.dialogRef.close();
  }

}
