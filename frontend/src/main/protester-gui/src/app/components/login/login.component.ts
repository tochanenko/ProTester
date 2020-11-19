import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../../services/auth/auth.service";
import {Router} from "@angular/router";
import {StorageService} from "../../services/auth/storage.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loginForm: FormGroup;
  hide = true;
  isLoggedIn = false;
  isLoginFailed = false;
  errorMessage = '';
  submitted = false;
  hidePasswordField = true;
  image = 'assets/logo.png';

  constructor(private authService: AuthService,
              private router: Router,
              private storageService: StorageService,
              private formBuilder: FormBuilder) {
  }

  ngOnInit(): void {

    this.loginForm = this.formBuilder.group({
      name: [null, [Validators.required]],
      password: [null, [Validators.required]]
    });

    if (window.sessionStorage.getItem('token')) {
      this.isLoggedIn = true;
      console.log('є токен');
      this.router.navigateByUrl('/profile').then();
    } else {
      console.log('немає токена');
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
      name: this.f.name.value,
      password: this.f.password.value,
    };

    this.authService.login(loginResponse).subscribe(
      user => {
        this.isLoggedIn = true;
        window.sessionStorage.setItem('token', user.accessToken);
        window.sessionStorage.setItem('user', JSON.stringify(user));
        this.storageService.setUser(user);
        this.router.navigateByUrl('/profile').then();
      },
      err => {
        this.errorMessage = err.error.message;
      }
    );
  }

  forgotPassword(): void {
    //   this.router.navigateByUrl('/forgotPassword').then();
  }
}
