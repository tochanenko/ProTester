import {Component, OnDestroy, OnInit} from '@angular/core';
import {User} from "../../../models/user.model";
import {StorageService} from "../../../services/auth/storage.service";
import {Subscription} from "rxjs";
import {UserService} from "../../../services/user/user.service";
import {switchMap} from "rxjs/operators";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit, OnDestroy {
  user: User = new User();
  subscription: Subscription;

  constructor(
    private storageService: StorageService,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    // this.subscription = this.storageService.currentUser.subscribe(user => {
    //   this.userService.getUserById(user.id).subscribe(currentUser => {
    //     this.user.id = currentUser['id'];
    //     this.user.email = currentUser['email'];
    //     this.user.firstName = currentUser['firstName'];
    //     this.user.lastName = currentUser['lastName'];
    //     this.user.isActive = currentUser['active'];
    //     this.user.role = currentUser['role'];
    //     this.user.username = currentUser['username'];
    //   })
    // });

    this.subscription = this.storageService.currentUser.pipe(
      switchMap(user => {
        return this.userService.getUserById(user.id);
      })
    ).subscribe(
      currentUser => {
        this.user.id = currentUser['id'];
        this.user.email = currentUser['email'];
        this.user.firstName = currentUser['firstName'];
        this.user.lastName = currentUser['lastName'];
        this.user.isActive = currentUser['active'];
        this.user.role = currentUser['role'];
        this.user.username = currentUser['username'];
      }
    );
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }
}
