import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {ViewComponent} from "./view/view.component";
import {CreateComponent} from "./create/create.component";
import {EditComponent} from "./edit/edit.component";
import {ListComponent} from "./list/list.component";

const routes: Routes = [
  {
    path: '',
    data: {
      breadcrumb: 'Libraries'
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
        path: 'view',
        data: {
          breadcrumb: 'View'
        },
        component: ViewComponent
      },
      {
        path: 'create',
        data: {
          breadcrumb: 'Create'
        },
        component: CreateComponent
      },
      {
        path: 'edit',
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
export class LibrariesRoutingModule { }
