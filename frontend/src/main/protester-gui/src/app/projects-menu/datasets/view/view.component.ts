import {Component, Inject, OnInit} from '@angular/core';
import {DataSet, DataSetParameter} from "../dataset.model";
import {Unsubscribe} from "../unsubscribe";
import {Router} from "@angular/router";
import {FormBuilder} from "@angular/forms";
import {DatasetService} from "../../../services/dataset.service";
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import {HttpErrorResponse} from "@angular/common/http";
import {takeUntil} from "rxjs/operators";

@Component({
  selector: 'app-view',
  templateUrl: './view.component.html',
  styleUrls: ['./view.component.css']
})
export class ViewComponent extends Unsubscribe implements OnInit {
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
