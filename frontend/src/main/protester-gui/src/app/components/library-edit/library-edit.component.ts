import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MatBottomSheet} from "@angular/material/bottom-sheet";
import {BottomSheetComponent} from "../bottom-sheet/bottom-sheet.component";
import {LibraryManageService} from "../../services/library/library-manage.service";
import {Action} from "../../models/action.model";
import {OuterComponent} from "../../models/outer.model";
import {LibraryBottomsheetInteractionService} from "../../services/library/library-bottomsheet-interaction.service";
import {Subscription} from "rxjs";
import {Library} from "../../models/library.model";
import {Step} from "../../models/step.model";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-library-edit',
  templateUrl: './library-edit.component.html',
  styleUrls: ['./library-edit.component.css']
})
export class LibraryEditComponent implements OnInit {
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
  private componentSubscription: Subscription;
  libraryUpdateForm: FormGroup;

  actions: Action[] = [];
  compounds: OuterComponent[] = [];
  bottomSheetData = {};
  library_id: number;
  library: Library;

  constructor(
    private formBuilder: FormBuilder,
    private _bottomSheet: MatBottomSheet,
    private libraryService: LibraryManageService,
    private interactionService: LibraryBottomsheetInteractionService,
    private router: Router,
    private activateRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.getIdFromParams();
    this.getLibraryById(this.library_id);
    this.createForm();
    this.getAllActionsForBottomSheet();
    this.getAllCompoundsForBottomSheet();
    this.updateActionsArray();
    this.updateCompoundsArray();
  }

  createForm(): void {
    this.libraryUpdateForm = this.formBuilder.group({
      name: ['', [Validators.required, Validators.minLength(this.validatorsConfig.name.minLength), Validators.maxLength(this.validatorsConfig.name.maxLength)]],
      description: ['', [Validators.required, Validators.maxLength(this.validatorsConfig.description.maxLength)]]
    })
  }

  getIdFromParams(): void {
    this.componentSubscription = this.activateRoute.queryParams.subscribe(params=>this.library_id=params['id']);
  }

  getLibraryById(id: number): void {
    this.libraryService.getLibraryById(id).subscribe(library => {
      this.library = library;
      console.log(library);

      let f = this.formControls;
      f.name.setValue(library.name);
      f.description.setValue(library.description);

      library.components.forEach(component => {
        let inner;
        if (component['action']) {
          inner = <Action> component.component;
          this.actions.push(new Action(inner.name, inner.description, inner.type, inner.parameterNames, inner.id, inner.className, inner.prepared, inner.preparedParams))
        } else
          {
            inner = <OuterComponent> component.component;
            this.compounds.push(new OuterComponent(inner.name, inner.description, inner.type, inner.parameterNames, inner.id, inner.steps))
        }
      })
    });
  }

  onSubmit(): void {
    console.error("SUBMIT")
    const f = this.formControls;

    if (this.libraryUpdateForm.invalid) {
      return;
    }

    if (this.actions.length === 0 && this.compounds.length === 0) {
      console.error("Set action or compound");
      return;
    }
    let libraryUpdateRequest = {};
    console.log(libraryUpdateRequest);


    libraryUpdateRequest['description'] = f.description.value;
    libraryUpdateRequest['name'] = f.name.value;
    libraryUpdateRequest['id'] = this.library_id;
    libraryUpdateRequest['components'] = [];
    if (this.actions.length > 0) {
      this.actions.map(action => {
        let action_step = new Step();
        action_step.isAction = true;
        action_step.id = action.id;
        libraryUpdateRequest['components'].push(action_step);
      })
    }

    if (this.compounds.length > 0) {
      this.compounds.map(compound => {
        let compound_step = new Step();
        compound_step.id = compound.id;
        compound_step.isAction = false;
        libraryUpdateRequest['components'].push(compound_step);
      })
    }
    this.libraryService.updateLibrary(libraryUpdateRequest, this.library.id).subscribe(() => {
        this.router.navigateByUrl('/library/search').then();
      },
      () => {
        console.error("Error of creation");
      })
  }

  get formControls() {
    return this.libraryUpdateForm.controls;
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

  getAllActionsForBottomSheet(): void {
    this.libraryService.getAllActions().subscribe(data => {
      this.bottomSheetData['actions'] = data;
    });
  }

  getAllCompoundsForBottomSheet(): void {
    this.libraryService.getAllCompounds().subscribe(data => {
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
