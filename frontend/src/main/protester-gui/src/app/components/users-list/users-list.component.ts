import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {StorageService} from "../../services/auth/storage.service";
import {UserService} from "../../services/user/user.service";
import {MatTableDataSource} from "@angular/material/table";
import {PageEvent} from "@angular/material/paginator";

@Component({
  selector: 'app-users-list',
  templateUrl: './users-list.component.html',
  styleUrls: ['./users-list.component.css']
})
export class UsersListComponent implements OnInit {

  displayedColumns: string[] = ['username', 'email', 'firstName', 'lastName', 'role', 'status'];
  dataSource = new MatTableDataSource();
  usersList = null;

  pageEvent: PageEvent;
  pageIndex = 1;
  pageSize = 10;
  length: number;

  constructor(private router: Router,
              private storageService: StorageService,
              private userService: UserService
  ) {
    this.userService.getAll().subscribe(
      users => {
        this.length = users.length;
        this.usersList = users;
        this.dataSource = users;
      },
      err => console.log(err)
    );
  }

  updateList(event?:PageEvent): PageEvent {
    this.dataSource = this.usersList.slice(event.pageIndex * event.pageSize, (event.pageIndex + 1) * event.pageSize);
    return event;
  }

  ngOnInit(): void {

  }

}
