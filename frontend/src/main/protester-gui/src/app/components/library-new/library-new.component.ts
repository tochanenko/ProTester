import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MatBottomSheet, MatBottomSheetRef} from "@angular/material/bottom-sheet";
import  {BottomSheetComponent} from "../bottom-sheet/bottom-sheet.component";

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
    {name: 'Alabama', capital: 'Montgomery'},
    {name: 'Alaska', capital: 'Juneau'},
    {name: 'Arizona', capital: 'Phoenix'},
    {name: 'Arkansas', capital: 'Little Rock'},
    {name: 'California', capital: 'Sacramento'},
    {name: 'Colorado', capital: 'Denver'},
    {name: 'Connecticut', capital: 'Hartford'},
    {name: 'Delaware', capital: 'Dover'},
    {name: 'Florida', capital: 'Tallahassee'},
    {name: 'Georgia', capital: 'Atlanta'},
    {name: 'Hawaii', capital: 'Honolulu'},
    {name: 'Idaho', capital: 'Boise'},
    {name: 'Illinois', capital: 'Springfield'},
    {name: 'Indiana', capital: 'Indianapolis'},
    {name: 'Iowa', capital: 'Des Moines'},
    {name: 'Kansas', capital: 'Topeka'},
    {name: 'Kentucky', capital: 'Frankfort'},
    {name: 'Louisiana', capital: 'Baton Rouge'},
    {name: 'Maine', capital: 'Augusta'}
    ]

  constructor(private formBuilder: FormBuilder, private _bottomSheet: MatBottomSheet) {
  }

  ngOnInit(): void {
    this.createForm();
  }

  onSubmit(): void {
  }

  openBottomSheet(): void {
    this._bottomSheet.open(BottomSheetComponent);
  }

  createForm(): void {
    this.libraryCreateForm = this.formBuilder.group({
      name: ['', Validators.maxLength(50)],
      description: ['', null]
    })
  }
}
