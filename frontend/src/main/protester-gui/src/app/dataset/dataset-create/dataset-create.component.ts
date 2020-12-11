import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {DatasetService} from "../../services/dataset.service";
import {MatDialogRef} from "@angular/material/dialog";
import {DatasetListComponent} from "../dataset-list/dataset-list.component";
import {DataSet, DataSetParameter} from "../dataset.model";
import {Unsubscribe} from "../unsubscribe";
import {takeUntil} from "rxjs/operators";
import {log} from "util";

@Component({
  selector: 'app-dataset-create',
  templateUrl: './dataset-create.component.html',
  styleUrls: ['./dataset-create.component.css']
})

export class DatasetCreateComponent extends Unsubscribe implements OnInit {

  public datasetCreateForm: FormGroup;
  public errorMessage = '';
  public submitted = false;
  public isSuccessful = false;
  public isFailed = false;
  public displayedColumns: string[] = ['NAME', 'VALUE'];
  public newDatasetParameters: DataSetParameter[] = [];

  constructor(private router: Router,
              private formBuilder: FormBuilder,
              private datasetService: DatasetService,
              private dialogRef: MatDialogRef<DatasetListComponent>) {
    super();
  }

  public ngOnInit(): void {
    this.createDataSetCreateForm();
    this.newDatasetParameters.push({name: "test", value: "value"});
  }

  public onSubmit(): void {
    this.submitted = true;

    const datasetCreateResponse: DataSet = {
      name: this.controls.name.value,
      description: this.controls.description.value,
      parameters: this.convertToMap(this.newDatasetParameters),
    };

    this.datasetService.create(datasetCreateResponse)
      .pipe(takeUntil(this.destroy$))
      .subscribe(
        (data) => {
          log(data);
          this.isSuccessful = true;
          this.dialogRef.close();
        },
        err => {
          this.errorMessage = err.error.message;
          this.isFailed = true;
        }
      );
  }

  public get controls() {
    return this.datasetCreateForm.controls;
  }

  private createDataSetCreateForm(): void {
    this.datasetCreateForm = this.formBuilder.group({
      name: [null, Validators.compose([Validators.minLength(4),
        Validators.maxLength(50)])],
      description: [null, Validators.compose([Validators.required])]
    });
  }

  private convertToMap(array: DataSetParameter[]): object {
    const parametersMap = {};
    array.map((parameter: DataSetParameter) => parametersMap[parameter.name] = parameter.value);
    return parametersMap;
  }
}
