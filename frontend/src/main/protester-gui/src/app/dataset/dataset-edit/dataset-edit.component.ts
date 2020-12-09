import {Component, Inject, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {DatasetService} from "../../services/dataset.service";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {ProjectListComponent} from "../../project/project-list/project-list.component";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-dataset-edit',
  templateUrl: './dataset-edit.component.html',
  styleUrls: ['./dataset-edit.component.css']
})
export class DatasetEditComponent implements OnInit {

  datasetUpdateForm: FormGroup;
  errorMessage: '';
  submitted = false;
  isSuccessful = false;
  isFailed = false;
  datasetId: number;
  private subscription: Subscription;

  constructor(private router: Router,
              private formBuilder: FormBuilder,
              private datasetService: DatasetService,
              private dialogRef: MatDialogRef<ProjectListComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any) {
    this.datasetId = data.id;
  }

  ngOnInit(): void {
    this.createDatasetCreateForm();

    this.subscription = this.datasetService.getDataSetById(this.datasetId).subscribe(
      data => {
        this.datasetUpdateForm.setValue(data);
      },
      error => {
        console.log(error);
        this.isFailed = true;
        this.errorMessage = error;
      });
  }

  get f() {
    return this.datasetUpdateForm.controls;
  }

  createDatasetCreateForm(): void {
    this.datasetUpdateForm = this.formBuilder.group({
      id: [''],
      name: ['', Validators.compose([
        Validators.minLength(4),
        Validators.maxLength(50)])],
      description: ['', Validators.compose([Validators.required])]
    });
  }

  onSubmit(): void {
    this.submitted = true;

    if (this.datasetUpdateForm.invalid) {
      return;
    }

    console.log('valid');
    const datasetUpdateResponse = {
      id: this.f.id.value,
      name: this.f.name.value,
      description: this.f.description.value,
      parameters: this.f.parameters.value
    };

    this.subscription = this.datasetService.update(datasetUpdateResponse)
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

  onNoClick(): void {
    this.dialogRef.close();
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
}
