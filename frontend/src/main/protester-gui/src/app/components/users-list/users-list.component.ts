import {Component, Input, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {StorageService} from "../../services/auth/storage.service";
import {UserService} from "../../services/user/user.service";
import {User} from "../../models/user.model";
import {DataSource} from "@angular/cdk/collections";
import {MatTableDataSource} from "@angular/material/table";

@Component({
  selector: 'app-users-list',
  templateUrl: './users-list.component.html',
  styleUrls: ['./users-list.component.css']
})
export class UsersListComponent implements OnInit {

  displayedColumns: string[] = ['username', 'email', 'firstName', 'lastName', 'role', 'status'];
  dataSource = new MatTableDataSource();

  constructor(private router: Router,
              private storageService: StorageService,
              private userService: UserService
  ) {
    this.userService.getAll().subscribe(
      users => {
        this.dataSource = users

      },
      err => console.log(err)
    );
  }

  ngOnInit(): void {

  }

}
