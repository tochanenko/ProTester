import {Component, Inject, OnInit} from '@angular/core';
import {AbstractControl, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {EnvironmentModel} from '../../../../../../models/environment/environment.model';

@Component({
  selector: 'app-edit',
  templateUrl: './edit.component.html',
  styleUrls: ['./edit.component.css']
})
export class EditComponent implements OnInit {

  envUpdateForm: FormGroup;
  submitted = false;

  constructor( public dialogRef: MatDialogRef<EditComponent>,
               @Inject(MAT_DIALOG_DATA) public data: { environment: EnvironmentModel},
               private formBuilder: FormBuilder) { }

  ngOnInit(): void {
    this.createEnvUpdateForm();
    this.envUpdateForm.setValue(this.data.environment);
  }

  get f(): {[p: string]: AbstractControl} {
    return this.envUpdateForm.controls;
  }

  createEnvUpdateForm(): void {
    this.envUpdateForm = this.formBuilder.group({
      id: [],
      name: [null, Validators.compose([
        Validators.required,
        Validators.minLength(4),
        Validators.maxLength(50)])
      ],
      description: [null, Validators.compose([
        Validators.required,
        Validators.minLength(4),
        Validators.maxLength(50)])
      ],
      username: [null, Validators.compose([
        Validators.required,
        Validators.minLength(4),
        Validators.maxLength(50)])
      ],
      password: [null, Validators.compose([
        Validators.required,
        Validators.minLength(4),
        Validators.maxLength(50)])
      ],
      url: [null, Validators.compose([
        Validators.required,
        Validators.minLength(4),
        Validators.maxLength(50)])
      ],
      projectId: []
    });
  }

  onSubmit(): void {
    this.submitted = true;

    if (this.envUpdateForm.invalid) {
      return;
    }

    const envUpdateResponse = {
      id: this.data.environment.id,
      name: this.f.name.value,
      description: this.f.description.value,
      username: this.f.username.value,
      password: this.f.password.value,
      url: this.f.url.value,
      projectId: this.data.environment.projectId
    };

    this.dialogRef.close(envUpdateResponse);
  }
}
