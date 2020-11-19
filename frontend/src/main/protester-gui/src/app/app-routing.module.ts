import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from "./components/login/login.component";
import {RegistrationComponent} from "./components/registration/registration.component";
import {ProfileComponent} from "./components/profile/profile.component";
import {AuthGuard} from "./services/auth/auth.guard";


const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'register',
    component: RegistrationComponent
    // canActivate: [AuthGuard],
    // data: {
    //   roles: ['ADMIN']
    // }
  },
  {
    path: 'profile',
    component: ProfileComponent,
    canActivate: [AuthGuard],
    data: {
      roles: ['ADMIN', 'MANAGER', 'ENGINEER']
    }
  },

  {path: '', redirectTo: 'login', pathMatch: 'full'}
];
@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
