import { Component, OnInit } from '@angular/core';
import {Subscription} from "rxjs";
import {FormBuilder, FormGroup} from "@angular/forms";
import {Action} from "../../../../models/action.model";
import {OuterComponent} from "../../../../models/outer.model";
import {Library} from "../../../../models/library.model";
import {MatBottomSheet} from "@angular/material/bottom-sheet";
import {LibraryManageService} from "../../../../services/library/library-manage.service";
import {LibraryBottomsheetInteractionService} from "../../../../services/library/library-bottomsheet-interaction.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-view',
  templateUrl: './view.component.html',
  styleUrls: ['./view.component.css']
})
export class ViewComponent implements OnInit {
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
  ) {

  }

  ngOnInit(): void {
    this.getIdFromParams();
    this.getLibraryById(this.library_id);
  }

  getIdFromParams(): void {
    this.componentSubscription = this.activateRoute.params.subscribe(params=>this.library_id=params['id']);
  }

  goToEdit(id): void {
    if (id) {
      this.router.navigate([`libraries-menu/libraries/${id}/edit`]).then();
    }
  }

  getLibraryById(id: number): void {
    this.libraryService.getLibraryById(id).subscribe(library => {
      this.library = library;

      library.components.forEach(component => {
        let inner;
        if (component['action']) {
          inner = <Action>component.component;
          inner.name = this.parseDescription(inner.name);
          this.actions.push(new Action(inner.name, inner.description, inner.type, inner.parameterNames, inner.id, inner.className, inner.prepared, inner.preparedParams))
        } else
        {
          inner = <OuterComponent>component.component;
          inner.name = this.parseDescription(inner.name);
          this.compounds.push(new OuterComponent(inner.name, inner.description, inner.type, inner.parameterNames, inner.id, inner.steps))
        }
      })
    });
  }

  parseDescription(description: string | Object) {
    if (typeof description !== "object") {
      const regexp = new RegExp('(\\${\\w*})');
      let splitted = description.split(regexp);
      return splitted.map(sub_string => {
        if (sub_string.includes("${")) {
          return {
            text: sub_string.replace('${', '').replace('}', ''),
            input: true
          }
        } else {
          return {
            text: sub_string,
            input: false
          }
        }
      })
    } else {
      return description;
    }
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
