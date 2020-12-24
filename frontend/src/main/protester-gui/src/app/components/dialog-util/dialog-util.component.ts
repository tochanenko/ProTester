import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {DialogWarningModel} from "../../models/dialog-warning.model";

@Component({
  selector: 'app-dialog-util',
  templateUrl: './dialog-util.component.html',
  styleUrls: ['./dialog-util.component.css']
})
export class DialogUtilComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<DialogUtilComponent>,
              @Inject(MAT_DIALOG_DATA) public data: DialogWarningModel) { }

  ngOnInit(): void {
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

}
