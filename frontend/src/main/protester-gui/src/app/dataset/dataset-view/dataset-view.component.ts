import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {Router} from "@angular/router";
import {DatasetService} from "../../services/dataset.service";
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import {DataSet, DataSetParameter} from "../dataset.model";
import {takeUntil} from "rxjs/operators";
import {Unsubscribe} from "../unsubscribe";
import {HttpErrorResponse} from "@angular/common/http";
import {log} from "util";

@Component({
  selector: 'app-dataset-view',
  templateUrl: './dataset-view.component.html',
  styleUrls: ['./dataset-view.component.css']
})
export class DatasetViewComponent extends Unsubscribe implements OnInit {

  public errorMessage: string;
  public dataset: DataSet;
  public datasetId: number;
  public displayedColumns: string[] = ['NAME', 'VALUE'];
  public datasetParameters: DataSetParameter[];

  constructor(private router: Router,
              private formBuilder: FormBuilder,
              private datasetService: DatasetService,
              @Inject(MAT_DIALOG_DATA) public data: any) {
    super();
    this.datasetId = data.id
  }

  public ngOnInit(): void {
    this.datasetService.getDataSetById(this.datasetId)
      .pipe(takeUntil(this.destroy$))
      .subscribe(
        (dataset: DataSet) => {
          this.dataset = dataset;
          this.datasetParameters = this.convertMapToDatasetParameters(dataset.parameters);
        },
        (error: HttpErrorResponse) => this.errorMessage = error.message,
      )
  }

  private convertMapToDatasetParameters(rawMap: object): DataSetParameter[] {
    const map = new Map(Object.entries(rawMap));
    const datasetParameters: DataSetParameter[] = [];
    map.forEach(((value, key) => datasetParameters.push({ name: key, value: value })));
    return datasetParameters;
  }
}
