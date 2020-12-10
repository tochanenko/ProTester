import { Component, OnDestroy, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {DatasetService} from "../../services/dataset.service";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-dataset-create',
  templateUrl: './dataset-create.component.html',
  styleUrls: ['./dataset-create.component.css']
})

export class DatasetCreateComponent implements OnInit, OnDestroy {

  datasetCreateForm: FormGroup;
  errorMessage = '';
  submitted = false;
  isSuccessful = false;
  isFailed = false;
  private subscription: Subscription;

  constructor(private router: Router,
              private formBuilder: FormBuilder,
              private datasetService: DatasetService) { }

  ngOnInit(): void {
  this.createDataSetCreateForm();
  }

  get f() {
    return this.datasetCreateForm.controls;
  }

  createDataSetCreateForm(): void {
    this.datasetCreateForm = this.formBuilder.group({
      name: [null, Validators.compose([Validators.minLength(4),
                                               Validators.maxLength(50)])],
      description: [null, Validators.compose([Validators.required])],
    });
  }

  onSubmit(): void {
    this.submitted = true;

    if (this.datasetCreateForm.invalid) {
      return;
    }

    const datasetCreateResponse = {
      name: this.f.name.value,
      description: this.f.description.value,
      parameters: this.f.parameters.value
    };

    this.subscription = this.datasetService.create(datasetCreateResponse)
      .subscribe(
        data => {
          this.isSuccessful = true;
          this.router.navigateByUrl('/datasetList').then();
        },
        err => {
          this.errorMessage = err.error.message;
          this.isFailed = true;
        }
      );
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
}
