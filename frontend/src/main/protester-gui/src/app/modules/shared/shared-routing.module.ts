import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {LoginComponent} from "./login/login.component";
import {RegistrationComponent} from "./registration/registration.component";
import {ProfileComponent} from "./profile/profile.component";
import {AuthGuard} from "../../services/auth/auth.guard";

const routes: Routes = [
  {
    path: '',
    data: {
      breadcrumb: 'Account'
    },
    children: [
      {
        path: '',
        redirectTo: 'login',
        pathMatch: 'full'
      },
      {
        path: 'login',
        data: {
          breadcrumb: 'Login'
        },
        component: LoginComponent
      },
      {
        path: 'registration',
        data: {
          breadcrumb: 'Registration'
        },
        component: RegistrationComponent
      },
      {
        path: 'profile',
        data: {
          roles: ['ADMIN', 'MANAGER', 'ENGINEER'],
          breadcrumb: 'Profile'
        },
        component: ProfileComponent,
        canActivate: [AuthGuard]
      },
      {
        path: 'users',
        data: {
          roles: ['ADMIN'],
          breadcrumb: null
        },
        loadChildren: 'src/app/modules/shared/users/users.module#UsersModule',
        canActivate: [AuthGuard]
      },
      {
        path: 'forgot-password',
        data: {
          breadcrumb: null
        },
        loadChildren: 'src/app/modules/shared/forgot-password/forgot-password.module#ForgotPasswordModule'
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SharedRoutingModule { }
