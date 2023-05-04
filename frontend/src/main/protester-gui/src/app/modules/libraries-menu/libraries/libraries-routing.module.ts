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
        children: [
          {
            path: '',
            data: {
              breadcrumb: 'Library'
            },
            children: [
              {
                path: '',
                data: {
                  breadcrumb: null
                },
                component: ViewComponent
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
        ]
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class LibrariesRoutingModule { }
