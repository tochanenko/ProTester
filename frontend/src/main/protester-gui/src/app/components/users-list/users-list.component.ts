import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
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

  displayedColumns: string[] = ['username', 'email', 'firstName', 'lastName', 'role', 'status', 'actions'];
  dataSource = new MatTableDataSource();
  usersList = null;

  pageEvent: PageEvent;
  pageIndex = 0;
  pageSize = 10;
  length: number;

  currentUser: any;

  constructor(private router: Router,
              private route: ActivatedRoute,
              private storageService: StorageService,
              private userService: UserService
  ) {
    this.currentUser = window.sessionStorage.getItem('user');
    this.getUsersList();
  }

  updateList(event?: PageEvent): PageEvent {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.dataSource = this.usersList.slice(event.pageIndex * event.pageSize, (event.pageIndex + 1) * event.pageSize);
    return event;
  }

  activate(id: any) {
    this.userService.activateUser(id).subscribe(
      () => {
        this.getUsersList();
      },
      err => console.log(err)
    );
  }

  deactivate(id: any) {
    this.userService.deactivateUser(id).subscribe(
      () => {
        this.getUsersList();
      },
      err => console.log(err)
    );
  }

  goToUser(id: any) {
    this.router.navigate(['/user', id]).then();
  }

  ngOnInit(): void {

  }

  getUsersList() {
    this.userService.getAll().subscribe(
      users => {
        this.length = users.length;
        this.usersList = users;
        this.dataSource = this.usersList.slice(this.pageIndex * this.pageSize, (this.pageIndex + 1) * this.pageSize);
      },
      err => console.log(err)
    );
  }

}
