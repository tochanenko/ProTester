import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Subscription} from "rxjs";
import {Router} from "@angular/router";
import {ProjectService} from "../../services/project.service";
import {StorageService} from "../../services/auth/storage.service";
import {TestCaseService} from "../../services/test-case/test-case-service";

@Component({
  selector: 'app-test-case-create',
  templateUrl: './test-case-create.component.html',
  styleUrls: ['./test-case-create.component.css']
})
export class TestCaseCreateComponent implements OnInit {

  testCaseForm: FormGroup;
  errorMessage = '';
  submitted = false;
  isSuccessful = false;
  isFailed = false;
  private subscription: Subscription;

  constructor(private router: Router,
              private formBuilder: FormBuilder,
              private projectService: ProjectService,
              private storageService: StorageService,
              private testCaseService: TestCaseService) {
  }

  ngOnInit(): void {
    this.createTestCaseCreateForm();
  }

  get f() {
    return this.testCaseForm.controls;
  }

  createTestCaseCreateForm(): void {
    this.testCaseForm = this.formBuilder.group({
      name: [null, Validators.compose([
        Validators.minLength(4),
        Validators.maxLength(50)])
      ],
      description: [null, Validators.compose([
        Validators.required
      ])],
      scenarioId: [null, Validators.compose([
        Validators.required
      ])],
      dataSet: [null, Validators.compose([
        Validators.required
      ])]
    });
  }

  onSubmit(): void {
    this.submitted = true;

    if (this.testCaseForm.invalid) {
      return;
    }

    const testCaseCreateResponse = {
      name: this.f.name.value,
      description: this.f.description.value,
      scenarioId: this.f.scenarioId.value,
      authorId: this.storageService.getUser.id,
      dataSetId: this.f.dataSet.value
    };

    this.subscription = this.testCaseService.create(testCaseCreateResponse)
      .subscribe(
        data => {
          this.isSuccessful = true;
          this.router.navigateByUrl('/test-case-list').then();
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
