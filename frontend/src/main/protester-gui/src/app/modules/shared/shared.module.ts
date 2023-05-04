import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {SharedRoutingModule} from './shared-routing.module';
import {LoginComponent} from "./login/login.component";
import {RegistrationComponent} from "./registration/registration.component";
import {ReactiveFormsModule} from "@angular/forms";
import {MaterialModule} from "../../services/material.module";
import {ForgotPasswordModule} from "./forgot-password/forgot-password.module";
import {UsersModule} from "./users/users.module";
import {ProfileComponent} from './profile/profile.component';


@NgModule({
  declarations: [
    LoginComponent,
    RegistrationComponent,
    ProfileComponent
  ],
  imports: [
    CommonModule,
    SharedRoutingModule,
    ReactiveFormsModule,
    MaterialModule,
    ForgotPasswordModule,
    UsersModule
  ]
})
export class SharedModule { }
