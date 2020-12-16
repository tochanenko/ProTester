import {Component, OnDestroy} from "@angular/core";
import {
  Observable,
  Subject,
} from "rxjs";

@Component({
  template: ''
})

export abstract class Unsubscribe implements OnDestroy {
  protected constructor() {
    this.destroy$ = this._destroy$.asObservable();
  }

  public destroy$: Observable<void>;
  private _destroy$: Subject<void> = new Subject();

  public ngOnDestroy(): void {
    this._destroy$.next();
    this._destroy$.complete();
  }
}
