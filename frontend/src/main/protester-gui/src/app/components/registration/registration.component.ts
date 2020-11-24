import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../../services/auth/auth.service";
import {Router} from "@angular/router";
import {Role} from "../../models/role.model";
import {CustomValidator} from "../../services/customVaidator.service";

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {

  registrationForm: FormGroup;
  isSuccessful = false;
  isSignUpFailed = false;
  errorMessage = '';
  submitted = false;
  roles: Array<Role>;
  hidePasswordField = true;
  hideConfirmPasswordField = true;
  image = 'assets/logo.png';

  constructor(private authService: AuthService, private router: Router, private formBuilder: FormBuilder) {
  }

  get f() {
    return this.registrationForm.controls;
  }

  ngOnInit(): void {
    this.createRegistrationForm();
    this.getAllRoles();
  }

  onSubmit(): void {
    this.submitted = true;

    if (this.registrationForm.invalid) {
      return;
    }

    console.log('valid');
    const registrationResponse = {
      username: this.f.username.value,
      email: this.f.email.value,
      firstName: this.f.firstName.value,
      lastName: this.f.lastName.value,
      password: this.f.password.value,
      role: this.f.role.value
    };

    this.authService.register(registrationResponse)
      .subscribe(
        data => {
          this.isSuccessful = true;
          this.router.navigateByUrl('/login').then();
        },
        err => {
          this.errorMessage = err.error.message;
          this.isSignUpFailed = true;
        }
      );
  }

  createRegistrationForm(): void {
    this.registrationForm = this.formBuilder.group({
      username: [null, Validators.compose([
        Validators.minLength(4),
        Validators.maxLength(20),
        Validators.pattern('[A-Za-z0-9]*')])
      ],
      email: [null, Validators.compose([
        Validators.email,
        Validators.maxLength(128)])
      ],
      firstName: [null, Validators.compose([
        Validators.minLength(1),
        Validators.maxLength(30)])
      ],
      lastName: [null, Validators.compose([
        Validators.minLength(1),
        Validators.maxLength(30)])
      ],
      password: [null, Validators.compose([
        Validators.minLength(8),
        Validators.maxLength(30),
        Validators.pattern('[A-Za-z0-9]*')])
      ],
      passwordConfirm: [null, Validators.compose([Validators.required])],
      role: [null, Validators.compose([Validators.required])]
    }, {
      validator: CustomValidator.passwordMatchValidator('password', 'passwordConfirm')
    });
  }

  getAllRoles(): void {
    this.authService.getRoles().subscribe(
      data => this.roles = data,
      error => this.errorMessage = error);
  }
}
