import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {PasswordService} from "../../../../services/password.service.ts.service";
import {Router} from "@angular/router";
import {StorageService} from "../../../../services/auth/storage.service";

@Component({
  selector: 'app-token-sent',
  templateUrl: './token-sent.component.html',
  styleUrls: ['./token-sent.component.css']
})
export class TokenSentComponent implements OnInit, OnDestroy {

  recoveryForm: FormGroup;
  submitted = false;
  image = 'assets/logo.png';
  notFound = false;

  subscriptions = [];

  constructor(private passwordService: PasswordService,
              private router: Router,
              private storageService: StorageService,
              private formBuilder: FormBuilder
  ) {
  }

  ngOnInit(): void {
    this.recoveryForm = this.formBuilder.group({
      email: [null, [Validators.required]]
    });
  }

  get f() {
    return this.recoveryForm.controls;
  }

  onSubmit(): void {
    this.notFound = false;
    this.submitted = true;

    if (this.recoveryForm.invalid) {
      return;
    }

    const recoveryResponse = {
      email: this.f.email.value
    }

    this.subscriptions.push(this.passwordService.forgotPassword(recoveryResponse).subscribe(
      email => this.router.navigateByUrl('/pending-password').then(),
      err => this.notFound = true
    ));
  }

  ngOnDestroy() {
    this.subscriptions.forEach(subscription => subscription.unsubscribe());
  }
}
