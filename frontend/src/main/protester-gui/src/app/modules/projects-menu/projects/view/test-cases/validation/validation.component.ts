import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {ValidationDataSetResponseModel} from '../../../../../../models/run-analyze/validation-data-set-response.model';
import {RunComponent} from '../run/run.component';

@Component({
  selector: 'app-validation',
  templateUrl: './validation.component.html',
  styleUrls: ['./validation.component.css']
})
export class ValidationComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<RunComponent>,
              @Inject(MAT_DIALOG_DATA) public data: ValidationDataSetResponseModel) {
  }

  ngOnInit(): void {
  }

}
