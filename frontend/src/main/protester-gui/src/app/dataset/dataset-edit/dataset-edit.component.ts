import {Component, Inject, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {DatasetService} from "../../services/dataset.service";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
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

  constructor() {

  }

  ngOnInit(): void {
  }
}
