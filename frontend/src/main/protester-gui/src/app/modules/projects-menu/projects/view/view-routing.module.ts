import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {TestCaseResultTableComponent} from "../test-case-result-table/test-case-result-table.component";

const routes: Routes = [
  {
    path: '',
    data: {
      breadcrumb: 'Project'
    },
    children: [
      {
        path: '',
        redirectTo: 'test-cases',
        pathMatch: 'full'
      },
      {
        path: 'test-cases',
        data: {
          breadcrumb: null
        },
        loadChildren: 'src/app/modules/projects-menu/projects/view/test-cases/test-cases.module#TestCasesModule'
      },
      {
        path: 'environment',
        data: {
          breadcrumb: null
        },
        loadChildren: 'src/app/modules/projects-menu/projects/view/environment/environment.module#EnvironmentModule'
      },
      {
        path: 'results',
        data: {
          breadcrumb: 'results'
        },
        component: TestCaseResultTableComponent
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ViewRoutingModule { }
