import {ChangeDetectorRef, Component, Inject, OnInit, ViewChild} from '@angular/core';
import {Router} from "@angular/router";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {DatasetService} from "../../services/dataset.service";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {DatasetListComponent} from "../dataset-list/dataset-list.component";
import {Unsubscribe} from "../unsubscribe";
import {takeUntil} from "rxjs/operators";
import {DataSet, DataSetParameter} from "../dataset.model";
import {HttpErrorResponse} from "@angular/common/http";
import { MatTable } from '@angular/material/table';

@Component({
  selector: 'app-dataset-edit',
  templateUrl: './dataset-edit.component.html',
  styleUrls: ['./dataset-edit.component.css']
})
export class DatasetEditComponent extends Unsubscribe implements OnInit {

  public datasetUpdateForm: FormGroup;
  public errorMessage: string;
  public submitted = false;
  public isSuccessful = false;
  public isFailed = false;
  public datasetId: number;
  public displayedColumns: string[] = ['NAME', 'VALUE', 'CONF'];
  @ViewChild(MatTable) table: MatTable<any>;
  public editDatasetParameters: DataSetParameter[] = [];

  constructor(private router: Router,
              private formBuilder: FormBuilder,
              private datasetService: DatasetService,
              private dialogRef: MatDialogRef<DatasetListComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any) {
    super();
    this.datasetId = data.id;
  }

  public ngOnInit(): void {
    this.initDatasetUpdateForm();

    this.datasetService.getDataSetById(this.datasetId)
      .pipe(takeUntil(this.destroy$))
      .subscribe(
        (dataset: DataSet) => {
          this.datasetUpdateForm.patchValue(dataset);
          this.editDatasetParameters = this.convertMapToDatasetParameters(dataset.parameters);
        },
        (error: HttpErrorResponse) => this.errorMessage = error.message,
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
      id: this.datasetId,
      name: this.controls.name.value,
      description: this.controls.description.value,
      parameters: this.convertToMap(this.editDatasetParameters),
    };

    this.datasetService.update(datasetUpdateResponse)
      .pipe(takeUntil(this.destroy$))
      .subscribe(
        data => {
          console.log(datasetUpdateResponse);
          this.isSuccessful = true;
          this.dialogRef.close();
        },
        (error: HttpErrorResponse) =>  {
          this.errorMessage = error.message;
          this.isFailed = true;
        },
      );
  }

  public createParameter(): DataSetParameter{
    return {
      name: 'nameExample',
      value: 'valueExample'
    };
  }

  public addRowTable() {
    this.editDatasetParameters.push(this.createParameter());
    console.log(this.editDatasetParameters);
    this.table.renderRows();
  }

  public deleteRow(row: any): void{
    const index = this.editDatasetParameters.indexOf(row, 0);
    if (index > -1) {
      this.editDatasetParameters.splice(index, 1);
    }
    this.table.renderRows();
  }

  private initDatasetUpdateForm(): void {
    this.datasetUpdateForm = this.formBuilder.group({
      name: ['', Validators.compose([
        Validators.minLength(4),
        Validators.maxLength(50)])],
      description: ['', Validators.compose([Validators.required])]
    });
  }

  public _editParameterNameChanged(inputNameElement: HTMLInputElement, index: number): void {
    this.editDatasetParameters[index] = {
      ...this.editDatasetParameters[index],
      name: inputNameElement.value,
    };
  }

  public _editParameterValueChanged(inputValueElement: HTMLInputElement, index: number): void {
    this.editDatasetParameters[index] = {
      ...this.editDatasetParameters[index],
      value: inputValueElement.value,
    };
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
