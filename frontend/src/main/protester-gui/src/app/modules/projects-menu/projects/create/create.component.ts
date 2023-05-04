import {Component, Inject, OnDestroy, OnInit} from '@angular/core';
import {AbstractControl, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {ProjectService} from '../../../../services/project.service';
import {Subscription} from 'rxjs';

@Component({
  selector: 'app-create',
  templateUrl: './create.component.html',
  styleUrls: ['./create.component.css']
})
export class CreateComponent implements OnInit, OnDestroy {
  projectCreateForm: FormGroup;
  errorMessage = '';
  submitted = false;
  isError = false;
  subscription: Subscription;

  constructor(public dialogRef: MatDialogRef<CreateComponent>,
              @Inject(MAT_DIALOG_DATA) public data: { userId: number },
              private formBuilder: FormBuilder,
              private projectService: ProjectService) {
    this.subscription = new Subscription();
  }

  ngOnInit(): void {
    this.createProjectCreateForm();
  }

  get f(): { [p: string]: AbstractControl } {
    return this.projectCreateForm.controls;
  }

  createProjectCreateForm(): void {
    this.projectCreateForm = this.formBuilder.group({
      projectName: [null, Validators.compose([
        Validators.minLength(4),
        Validators.maxLength(50)])
      ],
      projectWebsiteLink: [null, Validators.compose([
        Validators.required
      ])],
      projectActive: [null, Validators.compose([
        Validators.required
      ])]
    });
  }

  onSubmit(): void {
    this.submitted = true;

    if (this.projectCreateForm.invalid) {
      return;
    }

    const projectCreateResponse = {
      projectName: this.f.projectName.value,
      projectWebsiteLink: this.f.projectWebsiteLink.value,
      creatorId: this.data.userId,
      projectActive: this.f.projectActive.value,
    };

    this.subscription.add(
      this.projectService.create(projectCreateResponse)
        .subscribe(
          () => this.dialogRef.close(),
          () => this.isError = true
        )
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
