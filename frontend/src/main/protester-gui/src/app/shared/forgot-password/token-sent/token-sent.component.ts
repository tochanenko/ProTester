import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {PasswordService} from "../../../services/password.service.ts.service";
import {Router} from "@angular/router";
import {StorageService} from "../../../services/auth/storage.service";

@Component({
  selector: 'app-token-sent',
  templateUrl: './token-sent.component.html',
  styleUrls: ['./token-sent.component.css']
})
export class TokenSentComponent implements OnInit {

  recoveryForm: FormGroup;
  submitted = false;
  image = 'assets/logo.png';

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
    this.submitted = true;

    if (this.recoveryForm.invalid) {
      console.log('invalid');
      return;
    }

    const recoveryResponse = {
      email: this.f.email.value
    }

    this.passwordService.forgotPassword(recoveryResponse).subscribe(
      email => {
        this.router.navigateByUrl('/pending-password').then();
      }
    )
  }
}
