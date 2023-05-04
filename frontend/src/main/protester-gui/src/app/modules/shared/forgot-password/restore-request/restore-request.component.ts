import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {PasswordService} from "../../../../services/password.service.ts.service";
import {StorageService} from "../../../../services/auth/storage.service";

@Component({
  selector: 'app-restore-request',
  templateUrl: './restore-request.component.html',
  styleUrls: ['./restore-request.component.css']
})
export class RestoreRequestComponent implements OnInit, OnDestroy {

  recoveryForm: FormGroup;
  submitted = false;
  image = 'assets/logo.png';
  notFound = false;

  subscriptions = [];

  constructor(private passwordService: PasswordService,
              private router: Router,
              private storageService: StorageService,
              private formBuilder: FormBuilder
  ) {}

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
      email => {
        this.router.navigateByUrl('account/forgot-password/token-sent').then();
      },
      error => {
        this.notFound = true;
      }
    ));
  }

  ngOnDestroy() {
    this.subscriptions.forEach(subscription => subscription.unsubscribe());
  }

}
