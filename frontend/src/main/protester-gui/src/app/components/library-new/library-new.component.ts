import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MatBottomSheet} from "@angular/material/bottom-sheet";
import {BottomSheetComponent} from "../bottom-sheet/bottom-sheet.component";
import {LibraryManageService} from "../../services/library/library-manage.service";
import {Action} from "../../models/action.model";
import {OuterComponent} from "../../models/outer.model";
import {LibraryBottomsheetInteractionService} from "../../services/library/library-bottomsheet-interaction.service";
import {Subscription} from "rxjs";
import {Step} from "../../models/step.model";
import {Router} from "@angular/router";

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
  validatorsConfig = {
    name: {
      minLength: 5,
      maxLength: 15
    },
    description: {
      maxLength: 200
    }
  }

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
    private interactionService: LibraryBottomsheetInteractionService,
    private router: Router
    ) {
  }

  ngOnInit(): void {
    this.createForm();
    this.getAllActionsForBottomSheet();
    this.getAllCompoundsForBottomSheet();
    this.updateActionsArray();
    this.updateCompoundsArray();
  }

  createForm(): void {
    this.libraryCreateForm = this.formBuilder.group({
      name: ['', [Validators.required, Validators.minLength(this.validatorsConfig.name.minLength), Validators.maxLength(this.validatorsConfig.name.maxLength)]],
      description: ['', [Validators.required, Validators.maxLength(this.validatorsConfig.description.maxLength)]]
    })
  }

  onSubmit(): void {
    const f = this.formControls;


    if (this.libraryCreateForm.invalid) {
      return;
    }

    if (this.actions.length === 0 && this.compounds.length === 0) {
      console.error("Set action or compound");
      return;
    }
    let libraryCreateRequest = {};
    let action_step = new Step();
    let compound_step = new Step();

    libraryCreateRequest['description'] = f.description.value;
    libraryCreateRequest['name'] = f.name.value;
    libraryCreateRequest['id'] = 1;
    libraryCreateRequest['components'] = [];
    if (this.actions.length > 0) {
      this.actions.map(action => {
        action_step.isAction = true;
        action_step.id = action.id;
        libraryCreateRequest['components'].push(action_step);
      })
    }

    if (this.compounds.length > 0) {
      this.compounds.map(compound => {
        compound_step.id = compound.id;
        compound_step.isAction = false;
        libraryCreateRequest['components'].push(compound_step);
      })
    }

    this.libraryService.createLibrary(libraryCreateRequest).subscribe(() => {
        this.router.navigateByUrl('/library/search').then();
      },
      ()=> {
        console.error("Error of creation");
      })
  }

  get formControls() {
    return this.libraryCreateForm.controls;
  }

  updateActionsArray(): void {
    this.actionSubscription = this.interactionService.actionsArrayObserver.subscribe(action => {
      this.actions.push(action);
    });
  }

  updateCompoundsArray(): void {
    this.compoundSubscription = this.interactionService.compoundArrayObserver.subscribe(compound => {
      this.compounds.push(compound);
    });
  }

  deleteComponentFromArray(components, id): void {
    components.find((component, index, components) => {
      if (component.id === id) {
        components.splice(index, 1);
      }
    })
  }

  openBottomSheetWithActions(): void {
    this._bottomSheet.open(BottomSheetComponent, {
      data: {
        components: {actions: this.bottomSheetData['actions']}
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

  getAllActionsForBottomSheet(): void {
    this.libraryService.getAllActions().subscribe(data => {
      this.bottomSheetData['actions'] = data['list'];
    });
  }

  getAllCompoundsForBottomSheet(): void {
    this.libraryService.getAllCompounds().subscribe(data => {
      console.log(data)
      this.bottomSheetData['compounds'] = data['list'];
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
