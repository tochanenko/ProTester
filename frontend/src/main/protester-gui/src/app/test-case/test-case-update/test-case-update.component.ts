import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {Subscription} from 'rxjs';
import {Router} from '@angular/router';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {TestCaseService} from '../../services/test-case/test-case-service';
import {StorageService} from '../../services/auth/storage.service';
import {TestCaseListComponent} from "../test-case-list/test-case-list.component";
import {TestScenario} from "../../models/test-scenario";
import {TestScenarioService} from "../../services/test-scenario/test-scenario-service";
import {DataSetResponse} from "../../models/data-set-response";

@Component({
  selector: 'app-test-case-update',
  templateUrl: './test-case-update.component.html',
  styleUrls: ['./test-case-update.component.css']
})
export class TestCaseUpdateComponent implements OnInit {
  testCaseUpdateForm: FormGroup;
  selectedDataSet: DataSetResponse;
  testScenario: TestScenario[] = [];
  dataSet: DataSetResponse[] = [];
  scenarioId: number;
  testCaseId: number;
  errorMessage = '';
  submitted = false;
  isSuccessful = false;
  isFailed = false;
  private subscription: Subscription;

  constructor(private router: Router,
              private formBuilder: FormBuilder,
              private testCaseService: TestCaseService,
              private testScenarioService: TestScenarioService,
              private dialogRef: MatDialogRef<TestCaseListComponent>,
              private storageService: StorageService,
              @Inject(MAT_DIALOG_DATA) public data: any) {
    this.testCaseId = data.id;
  }
  ngOnInit(): void {
    this.createTestCaseUpdateForm();
    this.testCaseService.getFilterById(this.testCaseId).subscribe(
      data => {
        console.log(`DATASET ${JSON.stringify(data.dataSetResponseList)}`);
        this.dataSet = data.dataSetResponseList;
        this.testCaseUpdateForm.patchValue(data);
      },
      error => {
        console.log(error);
        this.isFailed = true;
        this.errorMessage = error;
      });
    this.testScenarioService.getAll().subscribe( data =>
    {
      console.log(data);
      this.testScenario = data; }
    );
  }
  get f() {
    return this.testCaseUpdateForm.controls;
  }

  createTestCaseUpdateForm(): void {
    this.testCaseUpdateForm = this.formBuilder.group({
      id: [''],
      name: ['', Validators.required],
      description: [''],
      projectId: [''],
      authorId: [''],
      scenarioId: [''],
      dataSetResponse: ''
    });
  }

  onSubmit(): void {
    this.submitted = true;

    if (this.testCaseUpdateForm.invalid) {
      return;
    }

    console.log(JSON.stringify(this.testCaseUpdateForm.value));

    const testCaseUpdateResponse = {
      id: this.testCaseId,
      description: this.f.description.value,
      name: this.f.name.value,
      scenarioId: this.f.scenarioId.value,
      projectId: this.f.projectId.value,
      authorId: this.storageService.getUser.id,
      dataSetResponseList: this.f.dataSetResponse.value
    };


    this.subscription = this.testCaseService.update(testCaseUpdateResponse)
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
}
