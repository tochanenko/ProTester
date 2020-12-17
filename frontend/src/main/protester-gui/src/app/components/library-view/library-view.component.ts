import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {MatBottomSheet} from "@angular/material/bottom-sheet";
import {LibraryManageService} from "../../services/library/library-manage.service";
import {Action} from "../../models/action.model";
import {OuterComponent} from "../../models/outer.model";
import {Subscription} from "rxjs";
import {Library} from "../../models/library.model";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-library-view',
  templateUrl: './library-view.component.html',
  styleUrls: ['./library-view.component.css']
})
export class LibraryViewComponent implements OnInit {
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
    private router: Router,
    private activateRoute: ActivatedRoute
  ) {

  }

  ngOnInit(): void {
    this.getIdFromParams();
    this.getLibraryById(this.library_id);
  }

  getIdFromParams(): void {
    this.componentSubscription = this.activateRoute.queryParams.subscribe(params=>this.library_id=params['id']);
  }

  goToEdit(id): void {
    if (id) {
      this.router.navigate(['library/edit'], {queryParams: {id: id}}).then();
    }
  }

  getLibraryById(id: number): void {
    this.libraryService.getLibraryById(id).subscribe(library => {
      this.library = library;

      library.components.forEach(component => {
        let inner;
        if (component['action']) {
          inner = <Action>component.component;
          this.actions.push(new Action(inner.name, inner.description, inner.type, inner.parameterNames, inner.id, inner.className, inner.prepared, inner.preparedParams))
        } else
        {
          inner = <OuterComponent>component.component;
          this.compounds.push(new OuterComponent(inner.name, inner.description, inner.type, inner.parameterNames, inner.id, inner.steps))
        }
      })
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
