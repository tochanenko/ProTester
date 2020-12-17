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
import {ProjectMenuComponent} from './components/project/project-menu/project-menu.component'
import {ProjectCreateComponent} from './components/project/project-create/project-create.component';
import {ProjectListComponent} from './components/project/project-list/project-list.component';
import {UsersListComponent} from "./components/users-list/users-list.component";
import {ActionsListComponent} from "./actions/actions-list/actions-list.component";
import {TestCaseListComponent} from "./test-case/test-case-list/test-case-list.component";
import {ViewUserComponent} from "./components/view-user/view-user.component";
import {EditUserComponent} from "./components/edit-user/edit-user.component";
import {LibraryMenuComponent} from './components/library-menu/library-menu.component';
import {LibraryNewComponent} from './components/library-new/library-new.component';
import {LibrarySearchComponent} from "./components/library-search/library-search.component";
import {CompoundSearchComponent} from "./components/compound-search/compound-search.component";
import {LibraryEditComponent} from "./components/library-edit/library-edit.component";
import {LibraryViewComponent} from "./components/library-view/library-view.component";
import {CompoundNewComponent} from "./components/compound-new/compound-new.component";
import {CompoundViewComponent} from "./components/compound-view/compound-view.component";

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
    component: ActionsListComponent
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
    path: 'users_list',
    component: UsersListComponent,
    canActivate: [AuthGuard],
    data: {
      roles: ['ADMIN', 'MANAGER', 'ENGINEER']
    }
  },
  {
    path: 'test-case-list/:id',
    component: TestCaseListComponent,
    canActivate: [AuthGuard],
    data: {
      roles: ['ADMIN', 'MANAGER', 'ENGINEER']
    }
  },
  {
    path: 'user/:id',
    component: ViewUserComponent,
    canActivate: [AuthGuard],
    data: {
      roles: ['ADMIN', 'MANAGER', 'ENGINEER']
    }
  },
  {
    path: 'user/edit/:id',
    component: EditUserComponent,
    canActivate: [AuthGuard],
    data: {
      roles: ['ADMIN', 'MANAGER', 'ENGINEER']
    }
  },
  {
    path: 'library',
    component: LibraryMenuComponent
  },
  {
    path: 'library/new',
    component: LibraryNewComponent
  },
  {
    path: 'library/search',
    component: LibrarySearchComponent
  },
  {
    path: 'library/edit',
    component: LibraryEditComponent
  },
  {
    path: 'library/view',
    component: LibraryViewComponent
  },
  {
    path: 'compound',
    component: CompoundSearchComponent
  },
  {
    path: 'compound/new',
    component: CompoundNewComponent
  },
  {
    path: 'compound/view',
    component: CompoundViewComponent
  },

  {path: '', redirectTo: 'login', pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
