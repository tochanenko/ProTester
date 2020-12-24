import { Component, OnInit } from '@angular/core';
import {User} from "../../../../models/user.model";
import {ActivatedRoute, Router} from "@angular/router";
import {UserService} from "../../../../services/user/user.service";

@Component({
  selector: 'app-view',
  templateUrl: './view.component.html',
  styleUrls: ['./view.component.css']
})
export class ViewComponent implements OnInit {
  userId: number = -1;
  user: User = null;

  completed = false;

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
    this.route.params.subscribe(params => {
      this.userId = params['id'];

      this.userService.getUserById(this.userId).subscribe(
        user => {
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
    });
  }
}
