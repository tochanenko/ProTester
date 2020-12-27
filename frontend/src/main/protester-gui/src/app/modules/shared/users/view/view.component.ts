import {Component, OnDestroy, OnInit} from '@angular/core';
import {User} from "../../../../models/user.model";
import {ActivatedRoute, Router} from "@angular/router";
import {UserService} from "../../../../services/user/user.service";
import {switchMap} from "rxjs/operators";

@Component({
  selector: 'app-view',
  templateUrl: './view.component.html',
  styleUrls: ['./view.component.css']
})
export class ViewComponent implements OnInit, OnDestroy {
  userId: number = -1;
  user: User = null;

  completed = false;

  subscriptions = [];

  constructor(private route: ActivatedRoute,
              private router: Router,
              private userService: UserService
  ) {
    this.user = new User();
    this.user.id = 0;
    this.user.email = 'example@gmail.com';
    this.user.firstName = 'First';
    this.user.lastName = 'Last';
    this.user.isActive = true;
    this.user.role = 'NOBODY';
    this.user.username = 'user';
  }

  ngOnInit(): void {
    // this.route.params.subscribe(params => {
    //   this.userId = params['id'];
    //
    //   this.userService.getUserById(this.userId).subscribe(
    //     user => {
    //       this.user.id = user['id'];
    //       this.user.email = user['email'];
    //       this.user.firstName = user['firstName'];
    //       this.user.lastName = user['lastName'];
    //       this.user.isActive = user['active'];
    //       this.user.role = user['role'];
    //       this.user.username = user['username'];
    //
    //       this.completed = true;
    //     },
    //     err => console.log(err)
    //   )
    // });

    this.subscriptions.push(
      this.route.params.pipe(
        switchMap(params => {
          this.userId = params['id'];
          return this.userService.getUserById(this.userId);
        })
      ).subscribe(user => {
          this.user.id = user['id'];
          this.user.email = user['email'];
          this.user.firstName = user['firstName'];
          this.user.lastName = user['lastName'];
          this.user.isActive = user['active'];
          this.user.role = user['role'];
          this.user.username = user['username'];

          this.completed = true;
        },
        err => console.log(err)
      )
    );
  }

  ngOnDestroy() {
    this.subscriptions.forEach(subscription => subscription.unsubscribe);
  }
}
