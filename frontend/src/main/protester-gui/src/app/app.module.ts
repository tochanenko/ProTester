import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {RegistrationComponent} from "./components/registration/registration.component";
import {LoginComponent} from "./components/login/login.component";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {CommonModule} from "@angular/common";
import {AuthInterceptor} from "./services/auth/auth.interceptor";
import {MatIconRegistry} from '@angular/material/icon';
import {MaterialModule} from "./services/material.module";
import {HeaderComponent} from './components/header/header.component';
import {ProfileComponent} from './components/profile/profile.component';
<<<<<<< HEAD
import {ForgotPasswordComponent} from './components/forgot-password/forgot-password.component';
import {PendingPasswordComponent} from './components/pending-password/pending-password.component';
import {ChangePasswordComponent} from './components/change-password/change-password.component';
import {TokenExpiredComponent} from './components/token-expired/token-expired.component';
=======
import { ForgotPasswordComponent } from './components/forgot-password/forgot-password.component';
import { PendingPasswordComponent } from './components/pending-password/pending-password.component';
import { ChangePasswordComponent } from './components/change-password/change-password.component';
import { TokenExpiredComponent } from './components/token-expired/token-expired.component';
import { ManageActionComponent } from './components/manage-action/manage-action.component';
import {MatTableModule} from "@angular/material/table";
import {MatPaginatorModule} from "@angular/material/paginator";
>>>>>>> ea499f547a4bb25c323a4e0cc8dd506d4df4dcf0
import {ProjectMenuComponent} from './project/project-menu/project-menu.component';
import {ProjectCreateComponent} from './project/project-create/project-create.component';
import {ProjectListComponent} from './project/project-list/project-list.component';
import {ProjectUpdateComponent} from './project/project-update/project-update.component';
<<<<<<< HEAD
import {ManageActionComponent} from "./components/manage-action/manage-action.component";
=======
>>>>>>> ea499f547a4bb25c323a4e0cc8dd506d4df4dcf0

@NgModule({
  declarations: [
    AppComponent,
    RegistrationComponent,
    LoginComponent,
    HeaderComponent,
    ProfileComponent,
    ForgotPasswordComponent,
    PendingPasswordComponent,
    ChangePasswordComponent,
    TokenExpiredComponent,
<<<<<<< HEAD
    ProjectMenuComponent,
    ProjectCreateComponent,
    ProjectListComponent,
    ProjectUpdateComponent,
    ManageActionComponent
=======
    ManageActionComponent
    ProjectMenuComponent,
    ProjectCreateComponent,
    ProjectListComponent,
    ProjectUpdateComponent
>>>>>>> ea499f547a4bb25c323a4e0cc8dd506d4df4dcf0
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    CommonModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    MaterialModule
  ],
  providers: [{provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true}, MatIconRegistry],
  bootstrap: [AppComponent]
})
export class AppModule {
}
