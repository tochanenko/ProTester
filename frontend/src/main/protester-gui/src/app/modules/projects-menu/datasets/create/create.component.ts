import {Component, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MatTable} from "@angular/material/table";
import {Router} from "@angular/router";
import {DatasetService} from "../../../../services/dataset.service";
import {MatDialogRef} from "@angular/material/dialog";
import {takeUntil} from "rxjs/operators";
import {Unsubscribe} from "../unsubscribe";
import {DataSet, DataSetParameter} from "../dataset.model";
import {ListComponent} from "../list/list.component";

@Component({
  selector: 'app-create',
  templateUrl: './create.component.html',
  styleUrls: ['./create.component.css']
})
export class CreateComponent extends Unsubscribe implements OnInit {
  public datasetCreateForm: FormGroup;
  public errorMessage = '';
  public submitted = false;
  public isSuccessful = false;
  public isFailed = false;
  public displayedColumns: string[] = ['NAME', 'VALUE', 'CONF'];
  @ViewChild(MatTable) table: MatTable<any>;
  public newDatasetParameters: DataSetParameter[] = [];

  constructor(private router: Router,
              private formBuilder: FormBuilder,
              private datasetService: DatasetService,
              private dialogRef: MatDialogRef<ListComponent>) {
    super();
  }

  public get controls() {
    return this.datasetCreateForm.controls;
  }

  public ngOnInit(): void {
    this.createDataSetCreateForm();
    this.newDatasetParameters.push({name: "nameExample", value: "valueExample"});
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
        () => {
          this.isSuccessful = true;
          this.dialogRef.close();
        },
        err => {
          this.errorMessage = err.error.message;
          this.isFailed = true;
        }
      );
  }

  public createParameter(): DataSetParameter {
    return {
      name: 'nameExample',
      value: 'valueExample'
    };
  }

  public addRowTable() {
    this.newDatasetParameters.push(this.createParameter());
    this.table.renderRows();
  }

  public deleteRow(row: any): void {
    const index = this.newDatasetParameters.indexOf(row, 0);
    if (index > -1) {
      this.newDatasetParameters.splice(index, 1);
    }
    this.table.renderRows();
  }

  public newParameterNameChanged(inputNameElement: HTMLInputElement, index: number): void {
    this.newDatasetParameters[index] = {
      ...this.newDatasetParameters[index],
      name: inputNameElement.value,
    };
  }

  public newParameterValueChanged(inputValueElement: HTMLInputElement, index: number): void {
    this.newDatasetParameters[index] = {
      ...this.newDatasetParameters[index],
      value: inputValueElement.value,
    };
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
