import {Component, Inject, OnInit,} from "@angular/core";
import {MAT_DIALOG_DATA, MatDialogRef,} from "@angular/material/dialog";
import {DatasetService} from "../../services/dataset.service";
import {DatasetListComponent} from "../dataset-list/dataset-list.component";
import {DataSet} from "../dataset.model";

@Component({
  selector: "app-dataset-delete",
  templateUrl: "./dataset-delete.component.html",
  styleUrls: ["./dataset-delete.component.css"],
})

export class DatasetDeleteComponent implements OnInit {

  public dataset: DataSet;

  constructor(private datasetService: DatasetService,
              private dialogRef: MatDialogRef<DatasetListComponent>,
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
