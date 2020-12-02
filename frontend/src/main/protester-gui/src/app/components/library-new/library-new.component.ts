import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
export interface Tile {
  rows: number;
  text: string;
}


@Component({
  selector: 'app-library-new',
  templateUrl: './library-new.component.html',
  styleUrls: ['./library-new.component.css']
})
export class LibraryNewComponent implements OnInit {
  libraryCreateForm: FormGroup;
  tiles: Tile[] = [
    {text: 'COMPOUNDS', rows: 1},
    {text: 'ACTIONS',rows: 1},
    {text: 'Three', rows: 2},
    {text: 'Four', rows: 2},
  ];

  states = [
    ]

  constructor(private formBuilder: FormBuilder) {
  }

  ngOnInit(): void {
    this.createForm();
  }

  onSubmit(): void {

  }

  createForm(): void {
    this.libraryCreateForm = this.formBuilder.group({
      name: ['', Validators.maxLength(50)],
      description: ['', null]
    })
  }
}
