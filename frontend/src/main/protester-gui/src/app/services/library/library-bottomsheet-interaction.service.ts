import { Injectable } from '@angular/core';
import {Subject} from "rxjs";
import {Action} from "../../models/action.model";
import {OuterComponent} from "../../models/outer.model";

@Injectable({
  providedIn: 'root'
})
export class LibraryBottomsheetInteractionService {
  public actionsArrayObserver: Subject<Action>;
  public compoundArrayObserver: Subject<OuterComponent>;

  constructor() {
    this.actionsArrayObserver = new Subject<Action>();
    this.compoundArrayObserver = new Subject<OuterComponent>();
  }

  updateActionsArray(action: Action) {
    this.actionsArrayObserver.next(action);
  }

  updateCompoundsArray(compound: OuterComponent) {
    this.compoundArrayObserver.next(compound);
  }
}
