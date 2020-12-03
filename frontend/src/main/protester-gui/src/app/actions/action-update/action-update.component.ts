import {Component, Inject, OnDestroy, OnInit} from '@angular/core';
import {ActionService} from "../../services/action/action.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {Subscription} from "rxjs";
import {ActionsListComponent} from "../actions-list/actions-list.component";

@Component({
  selector: 'app-action-update',
  templateUrl: './action-update.component.html',
  styleUrls: ['./action-update.component.css']
})
export class ActionUpdateComponent implements OnInit, OnDestroy {

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
              private dialogRef: MatDialogRef<ActionsListComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any) {
    this.actionId = data.id;
  }

  ngOnInit() {
    this.createActionUpdateForm();
    this.subscription = this.actionService.getActionById(this.actionId).subscribe(
      data => {
        console.log(`Changing data`+data);
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
      description:['',Validators.required],
      id:[''],
      name:[''],
      type:[''],
      parameterNames:[''],
      preparedParams:['']
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
