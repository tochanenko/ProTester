import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {DataSetResponse} from "../../../../../../models/data-set-response";
import {TestScenario} from "../../../../../../models/test-scenario";
import {Subscription} from "rxjs";
import {Router} from "@angular/router";
import {TestCaseService} from "../../../../../../services/test-case/test-case-service";
import {TestScenarioService} from "../../../../../../services/test-scenario/test-scenario-service";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {StorageService} from "../../../../../../services/auth/storage.service";
import {ListComponent} from "../list/list.component";

@Component({
  selector: 'app-edit',
  templateUrl: './edit.component.html',
  styleUrls: ['./edit.component.css']
})
export class EditComponent implements OnInit {
  testCaseUpdateForm: FormGroup;
  selectedDataSet: DataSetResponse[];
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
              private dialogRef: MatDialogRef<ListComponent>,
              private storageService: StorageService,
              @Inject(MAT_DIALOG_DATA) public data: any) {
    this.testCaseId = data.id;
  }
  ngOnInit(): void {
    this.testCaseService.getAllDataSets().subscribe( data => {
        this.dataSet = data.list;
      }
    );
    this.testScenarioService.getAll().subscribe( data =>
      {
        this.testScenario = data.list; }
    );
    this.createTestCaseUpdateForm();
    this.testCaseService.getFilterById(this.testCaseId).subscribe(
      data => {
        this.testCaseUpdateForm.patchValue(data);
      },
      error => {
        this.isFailed = true;
        this.errorMessage = error;
      });
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
      dataSetResponse: ['']
    });
  }

  onSubmit(): void {
    this.submitted = true;

    if (this.testCaseUpdateForm.invalid) {
      return;
    }

    const testCaseUpdateResponse = {
      id: this.testCaseId,
      description: this.f.description.value,
      name: this.f.name.value,
      scenarioId: this.f.scenarioId.value,
      projectId: this.f.projectId.value,
      authorId: this.storageService.getUser.id,
      dataSetId: this.f.dataSetResponse.value
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
