import {Component, Inject, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Subscription} from "rxjs";
import {Router} from "@angular/router";
import {ActionService} from "../../../../services/action/action.service";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {ListComponent} from "../list/list.component";

@Component({
  selector: 'app-edit',
  templateUrl: './edit.component.html',
  styleUrls: ['./edit.component.css']
})
export class EditComponent implements OnInit, OnDestroy {
  actionUpdateForm: FormGroup;
  actionId: number;
  errorMessage = '';
  submitted = false;
  isSuccessful = false;
  isFailed = false;
  private subscription: Subscription;

  constructor(private router: Router,
              private formBuilder: FormBuilder,
              private actionService: ActionService,
              private dialogRef: MatDialogRef<ListComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any) {
    this.actionId = data.id;
  }

  ngOnInit(): void {
    this.createActionUpdateForm();
    this.subscription = this.actionService.getActionById(this.actionId).subscribe(
      data => {
        console.log(data);
        console.log(`Changing data` + data);
        this.actionUpdateForm.setValue(data);
      },
      error => {
        console.log(error);
        this.isFailed = true;
        this.errorMessage = error;
      });
  }


  get f() {
    return this.actionUpdateForm.controls;
  }

  createActionUpdateForm(): void {
    this.actionUpdateForm = this.formBuilder.group({
      description: ['', Validators.required],
      id: [''],
      name: [''],
      type: [''],
      parameterNames: [''],
      preparedParams: [''],
      className: [''],
      prepared: ['']
    });
  }

  onSubmit(): void {
    this.submitted = true;

    if (this.actionUpdateForm.invalid) {
      return;
    }

    console.log('valid');

    const actionUpdateResponse = {
      id: this.actionId,
      description: this.f.description.value,
    };

    this.subscription = this.actionService.update(actionUpdateResponse)
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
