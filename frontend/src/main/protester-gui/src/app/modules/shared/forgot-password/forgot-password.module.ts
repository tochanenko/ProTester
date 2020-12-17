import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ForgotPasswordRoutingModule } from './forgot-password-routing.module';
import { CreateNewComponent } from './create-new/create-new.component';
import { InvalidTokenComponent } from './invalid-token/invalid-token.component';
import { RestoreRequestComponent } from './restore-request/restore-request.component';
import { TokenSentComponent } from './token-sent/token-sent.component';
import {MaterialModule} from "../../../services/material.module";
import {ReactiveFormsModule} from "@angular/forms";


@NgModule({
  declarations: [
    CreateNewComponent,
    InvalidTokenComponent,
    RestoreRequestComponent,
    TokenSentComponent
  ],
  imports: [
    CommonModule,
    ForgotPasswordRoutingModule,
    MaterialModule,
    ReactiveFormsModule
  ]
})
export class ForgotPasswordModule { }
