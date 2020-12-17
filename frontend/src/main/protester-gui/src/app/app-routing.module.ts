import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AuthGuard} from "./services/auth/auth.guard";
import {ProjectCreateComponent} from './components/project/project-create/project-create.component';
import {ProjectListComponent} from './components/project/project-list/project-list.component';
import {UsersListComponent} from "./components/users-list/users-list.component";
import {ActionsListComponent} from "./actions/actions-list/actions-list.component";
import {TestCaseListComponent} from "./test-case/test-case-list/test-case-list.component";
import {DatasetListComponent} from "./dataset/dataset-list/dataset-list.component";
import {ViewUserComponent} from "./components/view-user/view-user.component";
import {EditUserComponent} from "./components/edit-user/edit-user.component";
import {CompoundSearchComponent} from "./components/compound-search/compound-search.component";
import {CompoundNewComponent} from "./components/compound-new/compound-new.component";

const routes: Routes = [
  {
    path: '',
    children: [
      {
        path: '',
        redirectTo: 'account',
        pathMatch: 'full'
      },
      {
        path: 'account',
        loadChildren: 'src/app/shared/shared.module#SharedModule'
      },
      {
        path: 'projects-menu',
        loadChildren: 'src/app/projects-menu/projects-menu.module#ProjectsMenuModule'
      },
      {
        path: 'libraries-menu',
        loadChildren: 'src/app/libraries-menu/libraries-menu.module#LibrariesMenuModule'
      },
      // {
      //   path: 'profile',
      //   component: ProfileComponent,
      //   canActivate: [AuthGuard],
      //   data: {
      //     roles: ['ADMIN', 'MANAGER', 'ENGINEER']
      //   }
      // },
      {
        path: 'actions',
        component: ActionsListComponent
      },
      // {
      //   path: 'projectMenu',
      //   component: ProjectMenuComponent,
      //   canActivate: [AuthGuard],
      //   data: {
      //     roles: ['ADMIN', 'MANAGER', 'ENGINEER']
      //   }
      // },

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
        path: 'datasetList',
        component: DatasetListComponent,
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
        path: 'compound',
        component: CompoundSearchComponent
      },
      {
        path: 'compound/new',
        component: CompoundNewComponent
      },
      {
        path: '**',
        redirectTo: '404'
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
