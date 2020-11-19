import {BehaviorSubject, Observable} from 'rxjs';
import {User} from '../../models/user.model';
import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class StorageService {
  public currentUser: Observable<User>;
  private currentUserSubject: BehaviorSubject<User>;

  constructor() {
    this.currentUserSubject = new BehaviorSubject<User>(JSON.parse(sessionStorage.getItem('user')));
    this.currentUser = this.currentUserSubject.asObservable();
  }

  get getUser(): User {
    return this.currentUserSubject.getValue();
  }

  setUser(value: any): void {
    this.currentUserSubject.next(value);
  }
}
