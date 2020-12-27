import {Component, Inject, OnDestroy, OnInit} from '@angular/core';
import {AbstractControl, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Subscription} from 'rxjs';
import {Router} from '@angular/router';
import {ProjectService} from '../../../../services/project.service';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {ListComponent} from '../list/list.component';

@Component({
  selector: 'app-edit',
  templateUrl: './edit.component.html',
  styleUrls: ['./edit.component.css']
})
export class EditComponent implements OnInit, OnDestroy {

  projectUpdateForm: FormGroup;
  errorMessage = '';
  submitted = false;
  isError = false;
  projectId: number;
  private subscription: Subscription;

  constructor(private router: Router,
              private formBuilder: FormBuilder,
              private projectService: ProjectService,
              private dialogRef: MatDialogRef<ListComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any) {
    this.projectId = data.id;
    this.subscription = new Subscription();
  }

  ngOnInit(): void {
    this.createProjectCreateForm();

    this.subscription.add(
      this.projectService.getProjectById(this.projectId).subscribe(
        data => this.projectUpdateForm.setValue(data),
        () => this.isError = true)
    );
  }

  get f(): { [p: string]: AbstractControl } {
    return this.projectUpdateForm.controls;
  }

  createProjectCreateForm(): void {
    this.projectUpdateForm = this.formBuilder.group({
      projectId: [''],
      creatorUsername: [''],
      creatorId: [''],
      projectName: ['', Validators.compose([
        Validators.minLength(4),
        Validators.maxLength(50)])
      ],
      projectWebsiteLink: ['', Validators.compose([Validators.required])],
      projectActive: ['', Validators.compose([Validators.required])],
    });
  }

  onSubmit(): void {
    this.submitted = true;

    if (this.projectUpdateForm.invalid) {
      return;
    }

    const projectUpdateResponse = {
      projectId: this.f.projectId.value,
      projectName: this.f.projectName.value,
      projectWebsiteLink: this.f.projectWebsiteLink.value,
      projectActive: this.f.projectActive.value
    };

    this.subscription.add(
      this.projectService.update(projectUpdateResponse)
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
