import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";
import {UserService} from "../../../../services/user/user.service";
import {ActivatedRoute, Router} from "@angular/router";
import {concatMap, switchMap} from "rxjs/operators";

interface RoleView {
  value: string,
  viewValue: string
}

@Component({
  selector: 'app-edit',
  templateUrl: './edit.component.html',
  styleUrls: ['./edit.component.css']
})
export class EditComponent implements OnInit, OnDestroy {
  editUserForm: FormGroup;
  selectedValue = 'ADMIN';
  isActive = true;
  username = '';
  email = '';
  firstName = '';
  lastName = '';
  role = '';
  active = true;
  id = 0;

  subscriptions = [];

  roles: RoleView[] = [
    {value: 'ADMIN', viewValue: 'Administrator'},
    {value: 'MANAGER', viewValue: 'Manager'},
    {value: 'ENGINEER', viewValue: 'Engineer'}
  ];

  constructor(private userService: UserService,
              private router: Router,
              private route: ActivatedRoute,
              private formBuilder: FormBuilder
  ) {
    this.editUserForm = this.formBuilder.group({
      username: [this.username],
      email: [this.email],
      firstName: [this.firstName],
      lastName: [this.lastName],
      role: new FormControl(this.roles[2].value),
      active: [this.active]
    });

    this.subscriptions.push(this.route.params.pipe(
      switchMap((params) => {
        this.id = params['id'];
        return this.userService.getUserById(this.id);
      })
    ).subscribe(user => {
      this.username = user['username'];
      this.email = user['email'];
      this.firstName = user['firstName'];
      this.lastName = user['lastName'];
      this.role = user['role'];
      this.active = user['active'];

      this.isActive = this.active;
      this.selectedValue = this.role;

      this.editUserForm = this.formBuilder.group({
        username: [this.username],
        email: [this.email],
        firstName: [this.firstName],
        lastName: [this.lastName],
        role: this.role,
        active: [this.active]
      });
    }));
  }

  ngOnInit(): void {
  }

  get f() {
    return this.editUserForm.controls;
  }

  onSubmit() {
    const editUserResponse = {
      email: this.f.email.value,
      username: this.f.username.value,
      firstName: this.f.firstName.value,
      lastName: this.f.lastName.value,
      role: this.role,
      active: this.f.active.value
    };

    this.subscriptions.push(this.userService.updateUser(this.id, editUserResponse).subscribe(
      res => this.router.navigate([`/account/users/${this.id}`]),
      err => console.log(err)
    ));
  }

  updateRole(event: string) {
    this.role = event;
  }

  ngOnDestroy() {
    this.subscriptions.forEach(subscription => subscription.unsubscribe());
  }
}
