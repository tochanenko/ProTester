import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {Subscription} from "rxjs";
import {Router} from "@angular/router";
import {DatasetService} from "../../services/dataset.service";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {DatasetListComponent} from "../dataset-list/dataset-list.component";

@Component({
  selector: 'app-dataset-delete',
  templateUrl: './dataset-delete.component.html',
  styleUrls: ['./dataset-delete.component.css']
})

export class DatasetDeleteComponent implements OnInit {

  datasetDeleteForm: FormGroup;
  errorMessage = '';
  submitted = false;
  isSuccessful = false;
  isFailed = false;
  datasetId: number;
  private subscription: Subscription;

  constructor(private router: Router,
              private formBuilder: FormBuilder,
              private datasetService: DatasetService,
              private dialogRef: MatDialogRef<DatasetListComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any) {
    this.datasetId = data.id;
  }

  ngOnInit(): void {

    this.subscription = this.datasetService.getDataSetById(this.datasetId).subscribe(
      data => {
        this.datasetDeleteForm.setValue(data);
      },
      error => {
        console.log(error);
        this.isFailed = true;
        this.errorMessage = error;
      }
    );
  }

  get f(){
    return this.datasetDeleteForm.controls;
  }

  onSubmit(): void {
    this.submitted = true;

    if(this.datasetDeleteForm.invalid) {
      return;
    }

    this.subscription = this.datasetService.delete(this.datasetId)
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
