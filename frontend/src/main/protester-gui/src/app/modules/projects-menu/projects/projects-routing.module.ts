import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {ListComponent} from "./list/list.component";
import {CreateComponent} from "./create/create.component";

const routes: Routes = [
  {
    path: '',
    data: {
      breadcrumb: 'Projects'
    },
    children: [
      {
        path: '',
        data: {
          breadcrumb: null
        },
        component: ListComponent
      },
      {
        path: 'create',
        data: {
          breadcrumb: 'Create'
        },
        component: CreateComponent
      },
      {
        path: ':id',
        data: {
          breadcrumb: null
        },
        loadChildren: 'src/app/modules/projects-menu/projects/view/view.module#ViewModule'
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ProjectsRoutingModule { }
