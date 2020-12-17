import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {ViewComponent} from "./view/view.component";

const routes: Routes = [
  {
    path: '',
    data: {
      breadcrumb: 'Libraries menu'
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
        path: 'libraries',
        data: {
          breadcrumb: null
        },
        loadChildren: 'src/app/modules/libraries-menu/libraries/libraries.module#LibrariesModule'
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class LibrariesMenuRoutingModule { }
