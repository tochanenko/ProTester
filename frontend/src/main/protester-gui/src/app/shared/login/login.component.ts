import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../../services/auth/auth.service";
import {Router} from "@angular/router";
import {StorageService} from "../../services/auth/storage.service";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit, OnDestroy {

  loginForm: FormGroup;
  hide = true;
  isLoggedIn = false;
  isLoginFailed = false;
  errorMessage = '';
  submitted = false;
  hidePasswordField = true;
  image = 'assets/logo.png';
  private subscription: Subscription;

  constructor(private authService: AuthService,
              private router: Router,
              private storageService: StorageService,
              private formBuilder: FormBuilder) {
  }

  ngOnInit(): void {

    this.loginForm = this.formBuilder.group({
      email: [null, [Validators.required]],
      password: [null, [Validators.required]],
    });

    if (window.sessionStorage.getItem('token')) {
      this.isLoggedIn = true;
      this.router.navigateByUrl('/projects-menu').then();
    } else {
      this.isLoggedIn = false;
    }
  }

  get f() {
    return this.loginForm.controls;
  }

  onSubmit(): void {
    this.submitted = true;
    if (this.loginForm.invalid) {
      console.log('invalid');
      return;
    }

    const loginResponse = {
      email: this.f.email.value,
      password: this.f.password.value,
    };

    this.subscription = this.authService.login(loginResponse).subscribe(
      user => {
        this.isLoggedIn = true;
        window.sessionStorage.setItem('token', user.token);
        window.sessionStorage.setItem('user', JSON.stringify(user));
        this.storageService.setUser(user);
        this.router.navigateByUrl('/projects-menu').then();
      },
      err => {
        this.errorMessage = err.error.message;
        this.isLoginFailed = true;
      }
    );
  }

  forgotPassword(): void {
    this.router.navigateByUrl('/account/forgot-password').then();
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
}
