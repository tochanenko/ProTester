import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {EnvironmentModel} from '../../../../../../models/environment/environment.model';

@Component({
  selector: 'app-select-env',
  templateUrl: './select-env.component.html',
  styleUrls: ['./select-env.component.css']
})
export class SelectEnvComponent implements OnInit {

  selectedEnv: string;

  constructor( public dialogRef: MatDialogRef<SelectEnvComponent>,
               @Inject(MAT_DIALOG_DATA) public data: { environments: EnvironmentModel[], testCaseName: string}) { }

  ngOnInit(): void {
  }

  onSubmit(): void {
    this.dialogRef.close(this.selectedEnv);
  }

}
