import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {ListComponent} from "./list/list.component";
import {ViewComponent} from "./view/view.component";
import {EditComponent} from "./edit/edit.component";

const routes: Routes = [
  {
    path: '',
    data: {
      breadcrumb: 'Users'
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
        path: ':id',
        data: {
          breadcrumb: 'View'
        },
        component: ViewComponent
      },
      {
        path: ':id/edit',
        data: {
          breadcrumb: 'Edit'
        },
        component: EditComponent
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UsersRoutingModule { }
