import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {UserService} from "../../services/user/user.service";
import {StorageService} from "../../services/auth/storage.service";
import {User} from "../../models/user.model";

@Component({
  selector: 'app-view-user',
  templateUrl: './view-user.component.html',
  styleUrls: ['./view-user.component.css']
})
export class ViewUserComponent implements OnInit {

  userId: number = -1;
  user: User = null;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private userService: UserService,
              private storageService: StorageService
  ) {
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.userId = params['id'];

      this.userService.getUserById(this.userId).subscribe(
        user => {
          this.user = new User();
          this.user.id = user['id'];
          this.user.email = user['email'];
          this.user.firstName = user['firstName'];
          this.user.lastName = user['lastName'];
          this.user.isActive = user['active'];
          this.user.role = user['role'];
        },
        err => console.log(err)
      )
    });
  }

}
