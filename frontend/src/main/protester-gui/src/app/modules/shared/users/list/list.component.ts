import {Component, OnDestroy, OnInit} from '@angular/core';
import {MatTableDataSource} from "@angular/material/table";
import {PageEvent} from "@angular/material/paginator";
import {ActivatedRoute, Router} from "@angular/router";
import {StorageService} from "../../../../services/auth/storage.service";
import {UserService} from "../../../../services/user/user.service";

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.css']
})
export class ListComponent implements OnInit, OnDestroy {

  displayedColumns: string[] = ['username', 'email', 'firstName', 'lastName', 'role', 'status', 'actions'];
  dataSource = new MatTableDataSource();
  usersList = null;

  pageEvent: PageEvent;
  pageIndex = 0;
  pageSize = 10;
  length: number;

  completed = false;

  currentUser: any;

  subscriptions = [];

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
    this.subscriptions.push(this.userService.activateUser(id).subscribe(
      () => {
        this.getUsersList();
      },
      err => console.log(err)
    ));
  }

  deactivate(id: any) {
    this.subscriptions.push(this.userService.deactivateUser(id).subscribe(
      () => {
        this.getUsersList();
      },
      err => console.log(err)
    ));
  }

  goToUser(id: any) {
    this.router.navigate(['/user', id]).then();
  }

  ngOnInit(): void {

  }

  getUsersList() {
    this.subscriptions.push(this.userService.getAll().subscribe(
      users => {
        this.length = users.length;
        this.usersList = users;
        this.dataSource = this.usersList.slice(this.pageIndex * this.pageSize, (this.pageIndex + 1) * this.pageSize);
        this.completed = true;
      },
      err => console.log(err)
    ));
  }

  ngOnDestroy() {
    this.subscriptions.forEach(subscription => subscription.unsubscribe);
  }
}
