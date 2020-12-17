import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

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
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ViewRoutingModule { }
