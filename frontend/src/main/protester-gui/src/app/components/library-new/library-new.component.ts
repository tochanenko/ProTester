import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MatBottomSheet, MatBottomSheetRef} from "@angular/material/bottom-sheet";
import {BottomSheetComponent} from "../bottom-sheet/bottom-sheet.component";
import {LibraryManageService} from "../../services/library/library-manage.service";
import {Action} from "../../models/action.model";
import {OuterComponent} from "../../models/outer.model";
import {LibraryBottomsheetInteractionService} from "../../services/library/library-bottomsheet-interaction.service";
import {Subscription} from "rxjs";

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
  private actionSubscription: Subscription;
  private compoundSubscription: Subscription;
  libraryCreateForm: FormGroup;
  tiles: Tile[] = [
    {text: 'ACTIONS & COMPOUNDS', rows: 1},
    {text: 'Three', rows: 7},
  ];

  actions: Action[] = [];
  compounds: OuterComponent[] = [];
  bottomSheetData = {};

  constructor(
    private formBuilder: FormBuilder,
    private _bottomSheet: MatBottomSheet,
    private libraryService: LibraryManageService,
    private interactionService: LibraryBottomsheetInteractionService
    ) {
  }

  ngOnInit(): void {
    this.createForm();
    this.getAllActionsForBottomSheet();
    this.getAllCompoundsForBottomSheet();
    this.updateActionsArray();
    this.updateCompoundsArray();
  }

  onSubmit(): void {
  }

  updateActionsArray(): void {
    this.actionSubscription = this.interactionService.actionsArrayObserver.subscribe(action => {
      this.actions.push(action);
    });
  }

  updateCompoundsArray(): void {
    this.compoundSubscription = this.interactionService.compoundArrayObserver.subscribe(compound => {
      console.log(compound);
      this.compounds.push(compound);
    });
  }

  openBottomSheetWithActions(): void {
    this._bottomSheet.open(BottomSheetComponent, {
      data: {
        components: this.bottomSheetData['actions'],
        isAction: true
      }
    });
  }

  openBottomSheetWithCompounds(): void {
    this._bottomSheet.open(BottomSheetComponent, {
      data: {
        components: this.bottomSheetData['compounds'],
        isAction: false
      }
    });
  }

  createForm(): void {
    this.libraryCreateForm = this.formBuilder.group({
      name: ['', Validators.maxLength(50)],
      description: ['', null]
    })
  }

  getAllActionsForBottomSheet(): void {
    this.libraryService.getAllActions().subscribe(data => {
      this.bottomSheetData['actions'] = data;
    });
  }

  getAllCompoundsForBottomSheet(): void {
    this.libraryService.getAllCompounds().subscribe(data => {
      this.bottomSheetData['compounds'] = data;
    });
  }

  ngOnDestroy(): void {
    if (this.actionSubscription) {
      this.actionSubscription.unsubscribe();
    }
    if (this.compoundSubscription) {
      this.compoundSubscription.unsubscribe();
    }
  }

}
