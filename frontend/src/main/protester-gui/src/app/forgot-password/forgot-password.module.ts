import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ForgotPasswordRoutingModule } from './forgot-password-routing.module';
import { RestoreRequestComponent } from './restore-request/restore-request.component';
import { TokenSentComponent } from './token-sent/token-sent.component';
import { CreateNewComponent } from './create-new/create-new.component';
import { InvalidTokenComponent } from './invalid-token/invalid-token.component';
import {MaterialModule} from "../services/material.module";
import {ReactiveFormsModule} from "@angular/forms";


@NgModule({
  declarations: [RestoreRequestComponent, TokenSentComponent, CreateNewComponent, InvalidTokenComponent],
  imports: [
    CommonModule,
    ForgotPasswordRoutingModule,
    MaterialModule,
    ReactiveFormsModule
  ]
})
export class ForgotPasswordModule { }
