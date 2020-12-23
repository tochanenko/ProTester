import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {AbstractControl, FormBuilder, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-create',
  templateUrl: './create.component.html',
  styleUrls: ['./create.component.css']
})
export class CreateComponent implements OnInit {

  envCreateForm: FormGroup;
  submitted = false;

  constructor( public dialogRef: MatDialogRef<CreateComponent>,
               @Inject(MAT_DIALOG_DATA) public data: { projectId: number},
               private formBuilder: FormBuilder) { }

  ngOnInit(): void {
    this.createEnvCreateForm();
  }

  get f(): {[p: string]: AbstractControl} {
    return this.envCreateForm.controls;
  }

  createEnvCreateForm(): void {
    this.envCreateForm = this.formBuilder.group({
      name: [null, Validators.compose([
        Validators.minLength(4),
        Validators.maxLength(50)])
      ],
      description: [null, Validators.compose([
        Validators.minLength(4),
        Validators.maxLength(50)])
      ],
      username: [null, Validators.compose([
        Validators.minLength(4),
        Validators.maxLength(50)])
      ],
      password: [null, Validators.compose([
        Validators.minLength(4),
        Validators.maxLength(50)])
      ],
      url: [null, Validators.compose([
        Validators.minLength(4),
        Validators.maxLength(50)])
      ],
      projectId: []
    });
  }

  onSubmit(): void {
    this.submitted = true;

    if (this.envCreateForm.invalid) {
      return;
    }

    const envCreateResponse = {
      name: this.f.name.value,
      description: this.f.description.value,
      username: this.f.username.value,
      password: this.f.password.value,
      url: this.f.url.value,
      projectId: this.data.projectId
    };

    this.dialogRef.close(envCreateResponse);
  }

}
