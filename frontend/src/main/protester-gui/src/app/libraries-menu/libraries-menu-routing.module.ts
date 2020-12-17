import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {ViewComponent} from "./view/view.component";

const routes: Routes = [
  {
    path: '',
    children: [
      {
        path: '',
        component: ViewComponent
      },
      {
        path: 'libraries',
        loadChildren: 'src/app/libraries-menu/libraries/libraries.module#LibrariesModule'
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class LibrariesMenuRoutingModule { }
