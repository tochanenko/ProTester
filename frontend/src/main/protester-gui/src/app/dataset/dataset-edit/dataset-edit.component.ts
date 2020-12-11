import {Component, Inject, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {DatasetService} from "../../services/dataset.service";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {DatasetListComponent} from "../dataset-list/dataset-list.component";
import {Unsubscribe} from "../unsubscribe";
import {takeUntil} from "rxjs/operators";
import {DataSetParameter} from "../dataset.model";

@Component({
  selector: 'app-dataset-edit',
  templateUrl: './dataset-edit.component.html',
  styleUrls: ['./dataset-edit.component.css']
})
export class DatasetEditComponent extends Unsubscribe implements OnInit {

  public datasetUpdateForm: FormGroup;
  public errorMessage: '';
  public submitted = false;
  public isSuccessful = false;
  public isFailed = false;
  public datasetId: number;
  public displayedColumns: string[] = ['NAME', 'VALUE'];
  public editDatasetParameters: DataSetParameter[] = [];

  constructor(private router: Router,
              private formBuilder: FormBuilder,
              private datasetService: DatasetService,
              private dialogRef: MatDialogRef<DatasetListComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any) {
    super()
    this.datasetId = data.id;
  }

  public ngOnInit(): void {
    this.updateDatasetForm();

    this.datasetService.getDataSetById(this.datasetId)
      .pipe(takeUntil(this.destroy$))
      .subscribe(
        data => {
          this.datasetUpdateForm.setValue(data);
        },
        error => {
          this.isFailed = true;
          this.errorMessage = error
        }
      )
  }

  public get controls() {
    return this.datasetUpdateForm.controls;
  }

  public onSubmit(): void {
    this.submitted = true;

    if (this.datasetUpdateForm.invalid) {
      return;
    }

    const datasetUpdateResponse = {
      id: this.controls.id.value,
      name: this.controls.name.value,
      description: this.controls.description.value,
      parameters: this.convertToMap(this.editDatasetParameters),
    };

    this.datasetService.update(datasetUpdateResponse)
      .pipe(takeUntil(this.destroy$))
      .subscribe(
        data => {
          this.isSuccessful = true;
          this.dialogRef.close();
        },
        err => {
          this.errorMessage = err.error.message;
          this.isFailed = true;
        }
      );
  }

  public onNoClick(): void {
    this.dialogRef.close();
  }

  private updateDatasetForm(): void {
    this.datasetUpdateForm = this.formBuilder.group({
      id: [''],
      name: ['', Validators.compose([
        Validators.minLength(4),
        Validators.maxLength(50)])],
      description: ['', Validators.compose([Validators.required])]
    });
  }

  private convertToMap(array: DataSetParameter[]): object {
    const parametersMap = {};
    array.map((parameter: DataSetParameter) => parametersMap[parameter.name] = parameter.value);
    return parametersMap;
  }

  private convertMapToDatasetParameters(rawMap: object): DataSetParameter[] {
    const map = new Map(Object.entries(rawMap));
    const datasetParameters: DataSetParameter[] = [];
    map.forEach(((value, key) => datasetParameters.push({ name: key, value: value })));
    return datasetParameters;
  }
}
