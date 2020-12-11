import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {UserService} from "../../services/user/user.service";

interface RoleView {
  value: string,
  viewValue: string
}

@Component({
  selector: 'app-edit-user',
  templateUrl: './edit-user.component.html',
  styleUrls: ['./edit-user.component.css']
})
export class EditUserComponent implements OnInit {

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
      role: [new FormControl(this.roles[1].value)],
      active: [this.active]
    });

    this.route.params.subscribe(
      params => {
        console.log(params);
        this.id = params['id'];
        this.userService.getUserById(this.id).subscribe(
          user => {
            console.log(user);

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
              role: [new FormControl(this.roles[1].value)],
              active: [this.active]
            });
          }
        );
      }
    );
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
      role: this.f.role.value.value,
      active: this.f.active.value
    };

    console.log(editUserResponse);

    this.userService.updateUser(this.id, editUserResponse).subscribe(
      res => this.router.navigate(['/users_list']),
      err => console.log(err)
    );
  }

}
