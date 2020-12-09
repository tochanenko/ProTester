import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from "./components/login/login.component";
import {RegistrationComponent} from "./components/registration/registration.component";
import {ProfileComponent} from "./components/profile/profile.component";
import {AuthGuard} from "./services/auth/auth.guard";
import {ForgotPasswordComponent} from "./components/forgot-password/forgot-password.component";
import {PendingPasswordComponent} from "./components/pending-password/pending-password.component";
import {ChangePasswordComponent} from "./components/change-password/change-password.component";
import {TokenExpiredComponent} from "./components/token-expired/token-expired.component";
import {ProjectMenuComponent} from "./project/project-menu/project-menu.component";
import {ProjectCreateComponent} from "./project/project-create/project-create.component";
import {ProjectListComponent} from "./project/project-list/project-list.component";
import {ManageActionComponent} from "./components/manage-action/manage-action.component";
import {UsersListComponent} from "./components/users-list/users-list.component";
import {DatasetListComponent} from "./dataset/dataset-list/dataset-list.component";


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
  {
    path: 'forgot-password',
    component: ForgotPasswordComponent
  },
  {
    path: 'pending-password',
    component: PendingPasswordComponent
  },
  {
    path: 'change-password',
    component: ChangePasswordComponent
  },
  {
    path: 'token-expired',
    component: TokenExpiredComponent
  },
  {
    path: 'actions',
    component: ManageActionComponent
  },
  {
    path: 'projectMenu',
    component: ProjectMenuComponent,
    canActivate: [AuthGuard],
    data: {
      roles: ['ADMIN', 'MANAGER', 'ENGINEER']
    }
  },

  {
    path: 'projectCreate',
    component: ProjectCreateComponent,
    canActivate: [AuthGuard],
    data: {
      roles: ['ADMIN', 'MANAGER', 'ENGINEER']
    }
  },

  {
    path: 'projectList',
    component: ProjectListComponent,
    canActivate: [AuthGuard],
    data: {
      roles: ['ADMIN', 'MANAGER', 'ENGINEER']
    }
  },

  {
    path: 'datasetList',
    component: DatasetListComponent,
    canActivate: [AuthGuard],
    data: {
      roles: ['ADMIN', 'MANAGER', 'ENGINEER']
    }
  },

  {
    path: 'users_list',
    component: UsersListComponent,
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
export class AppRoutingModule {
}
