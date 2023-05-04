import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {PasswordService} from "../../../../services/password.service.ts.service";
import {StorageService} from "../../../../services/auth/storage.service";
import {CustomValidator} from "../../../../services/customVaidator.service";
import {mergeMap} from "rxjs/operators";

@Component({
  selector: 'app-create-new',
  templateUrl: './create-new.component.html',
  styleUrls: ['./create-new.component.css']
})
export class CreateNewComponent implements OnInit, OnDestroy {
  recoveryForm: FormGroup;
  submitted = false;
  hideNewPasswordField = true;
  hideConfirmPasswordField = true;
  hide = true;
  image = 'assets/logo.png';
  token = '';
  email = '';

  subscriptions = [];

  constructor(private route: ActivatedRoute,
              private passwordService: PasswordService,
              private router: Router,
              private storageService: StorageService,
              private formBuilder: FormBuilder
  ) {
  }

  ngOnInit(): void {
    this.subscriptions.push(
      this.route.queryParams.pipe(
        mergeMap(params => {
          this.token = params['t'];
          return this.passwordService.confirmReset(this.token);
        })
      ).subscribe(
        email => {
          if (email == null || email.length == 0) {
            this.router.navigateByUrl('/account/forgot-password/invalid-token').then();
          } else {
            this.email = email;
          }
        },
        err => this.router.navigateByUrl('/account/forgot-password/invalid-token').then()
      )
    );

    this.recoveryForm = this.formBuilder.group({
      password: [null, Validators.compose([
        Validators.minLength(8),
        Validators.maxLength(30),
        Validators.pattern('[A-Za-z0-9]*')])
      ],
      passwordConfirm: [null, Validators.compose([Validators.required])]
    }, {
      validator: CustomValidator.passwordMatchValidator('password', 'passwordConfirm')
    });
  }

  get f() {
    return this.recoveryForm.controls;
  }

  comparePasswords(): boolean {
    return this.f.password.value === this.f.passwordConfirm.value
  }

  onSubmit(): void {
    this.submitted = true;

    if (this.recoveryForm.invalid) {
      return;
    }

    const recoveryResponse = {
      email: this.email,
      password: this.f.password.value
    }

    this.subscriptions.push(
      this.passwordService.resetPassword(recoveryResponse).subscribe(
        data => {
          this.router.navigateByUrl('/account/login').then();
        }
      )
    );
  }

  ngOnDestroy() {
    this.subscriptions.forEach(subscription => subscription.unsubscribe());
  }
}
